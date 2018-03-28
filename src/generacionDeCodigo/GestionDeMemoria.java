package generacionDeCodigo;

import ast.*;
import visitor.*;

/** 
 * Clase encargada de asignar direcciones a las variables 
 */
public class GestionDeMemoria extends DefaultVisitor {

		//	class Programa { List<Definicion> definicion; }
		public Object visit(Programa node, Object param) {

			int sumaTama�oVariables = 0;

			for (Definicion child : node.getDefinicion()) {
				sumaTama�oVariables += (Integer)super.visit(node, sumaTama�oVariables);
			}
			return null;
		}

		//	class DefVariable { String nombre;  Tipo tipo; }
		public Object visit(DefVariable node, Object param) {
			node.setDireccion((Integer)param);
			return node.getTipo().getSize();
		}

}
