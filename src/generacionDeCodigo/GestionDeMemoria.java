package generacionDeCodigo;

import ast.*;
import visitor.*;

/** 
 * Clase encargada de asignar direcciones a las variables 
 */
public class GestionDeMemoria extends DefaultVisitor {

		//	class Programa { List<Definicion> definicion; }
		public Object visit(Programa node, Object param) {

			int sumaTamañoVariables = 0;

			for (Definicion child : node.getDefinicion()) {
				sumaTamañoVariables += (Integer)super.visit(node, sumaTamañoVariables);
			}
			return null;
		}

		//	class DefVariable { String nombre;  Tipo tipo; }
		public Object visit(DefVariable node, Object param) {
			node.setDireccion((Integer)param);
			return node.getTipo().getSize();
		}

}
