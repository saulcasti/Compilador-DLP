package generacionDeCodigo;

import ast.*;
import visitor.*;

/** 
 * Clase encargada de asignar direcciones a las variables 
 */
public class GestionDeMemoria extends DefaultVisitor {

		private int sumaDireccionesGlobales = 0;
		private int sumaDireccionesLocales = 0;

		//	class DefVariable { String nombre;  Tipo tipo; }
		public Object visit(DefVariable node, Object param) {
			super.visit(node, param);
			if (node.getAmbito()==0) {  //Global
				node.setDireccion(sumaDireccionesGlobales);
				sumaDireccionesGlobales += node.getTipo().getSize();
			}
			else if(node.getAmbito()==1){
				sumaDireccionesLocales += - node.getTipo().getSize();
				node.setDireccion(sumaDireccionesLocales);
			}
			return null;
		}

		//	class DefFuncion { String nombre;  List<DefParametro> parametros;  Retorno retorno;  Cuerpo cuerpo; }
		public Object visit(DefFuncion node, Object param) {
			super.visit(node, param);
			
			sumaDireccionesLocales = 0;
			int direccionesParam = 4;
			for(int i=node.getParametros().size()-1;i>=0;i--) {
				node.getParametros().get(i).setDireccion(direccionesParam);
				direccionesParam += node.getParametros().get(i).getTipo().getSize();
			}
			
			return null;
		}
		
		//	class DefEstructura { String nombre;  List<DefCampo> defcampo; }
		public Object visit(DefEstructura node, Object param) {
			super.visit(node, param);
			
			int sumaDireccionesCampos=0;
			
			for(int i=0;i<node.getDefcampo().size();i++ ) {
				node.getDefcampo().get(i).setDireccion(sumaDireccionesCampos);
				sumaDireccionesCampos += node.getDefcampo().get(i).getTipo().getSize();
			}
			
			return null;
		}
		
}
