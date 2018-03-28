package generacionDeCodigo;

import ast.*;
import visitor.*;

/** 
 * Clase encargada de asignar direcciones a las variables 
 */
public class GestionDeMemoria extends DefaultVisitor {

		private int sumaDirecciones = 0;
		

		//	class DefVariable { String nombre;  Tipo tipo; }
		public Object visit(DefVariable node, Object param) {
			super.visit(node, param);
			node.setDireccion(sumaDirecciones);
			sumaDirecciones += node.getTipo().getSize();
			
			return null;
		}

}
