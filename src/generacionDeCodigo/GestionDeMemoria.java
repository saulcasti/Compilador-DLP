package generacionDeCodigo;

import ast.*;
import visitor.*;

/** 
 * Clase encargada de asignar direcciones a las variables 
 */
public class GestionDeMemoria extends DefaultVisitor {

		//	class Programa { List<Definicion> definicion; }
		public Object visit(Programa node, Object param) {
			super.visit(node, 0);
			
			return null;
		}

		//	class DefVariable { String nombre;  Tipo tipo; }
		public Object visit(DefVariable node, Object param) {
			int a = (Integer)param + node.getTipo().getSize();
			node.setDireccion(a);
			return (Integer)super.visit(node, a);
		}

		// class DefFuncion { String nombre;  List<DefParametro> parametros;  Retorno retorno;  Cuerpo cuerpo; }
		public Object visit(DefFuncion node, Object param) {
			return (Integer)super.visit(node, param);
		}
}
