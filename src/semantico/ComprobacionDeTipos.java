package semantico;

import ast.*;
import main.*;
import visitor.*;

public class ComprobacionDeTipos extends DefaultVisitor {

	public ComprobacionDeTipos(GestorErrores gestor) {
		this.gestorErrores = gestor;
	}

	// class Asigna { Expresion left; Expresion right; }
		public Object visit(Asigna node, Object param) {
			super.visit(node, param);
			predicado(mismoTipo(node.getLeft(), node.getRight()), "Asignación - Los operandos deben ser del mismo tipo", node.getStart());
			predicado(node.getLeft().isModificable(), "Asignación - Se requiere expresión modificable", node.getLeft().getStart());
			return null;
		}

		// class ExpresionBinaria { Expresion left; String operador; Expresion right; }
		public Object visit(ExpresionBinaria node, Object param) {
			super.visit(node, param);
			predicado(mismoTipo(node.getLeft(), node.getRight()), "Expresion binaria - Los operandos deben ser del mismo tipo", node.getStart());

			node.setTipo(node.getLeft().getTipo());
			node.setModificable(false);
			return null;
		}

		// class Variable { String nombre; }
		public Object visit(Variable node, Object param) {
			super.visit(node, param); // ¿Hace falta?
			
			node.setTipo(node.getDefinicion().getTipo());
			node.setModificable(true);

			if(param != null) {
				IdentType struct= (IdentType) node.getTipo();
				DefEstructura defS= struct.getDefinicion();

				boolean i=false;
				for(DefCampo dC :defS.getDefcampo()) {
					if(dC.getNombre().equals((String)param)) {
						i= true;
					}
				}predicado(i,"Estructura - El campo al que se hace referencia no existe en la estructura " 
						+ defS.getNombre(), node.getStart());
			}

			return null;
		}

		// class LiteralInt { String valor; }
		public Object visit(LiteralInt node, Object param) {
			node.setTipo(new IntType());
			node.setModificable(false);
			return null;
		}

		// class LiteralReal { String valor; }
		public Object visit(LiteralReal node, Object param) {
			node.setTipo(new RealType());
			node.setModificable(false);
			return null;
		}
		
//		class LiteralChar { String valor; }
		public Object visit(LiteralChar node, Object param) {
			node.setTipo(new RealType());
			node.setModificable(false);
			return null;
		}
		
		//	class VarArray { Expresion identificacion;  Expresion posicion; }
		public Object visit(VarArray node, Object param) {
			super.visit(node, param);

			predicado(node.getPosicion().getTipo().getClass() == IntType.class, 
					"Variable array - la posición se indica mediante un entero",node.getStart());
			
			if(node.getPosicion().getTipo().getClass() == IntType.class )
				predicado(node.getIdentificacion().getTipo().getClass() == ArrayType.class, 
						"Variable array - el tipo de la variable debe ser array",node.getStart());


			node.setTipo((Tipo)node.getIdentificacion().getTipo());
			node.setModificable(true);
			return null;
		}
		
		// class ArrayType
		public Object visit(ArrayType node, Object param) {
			
			return node.getTipo();
		}
		
		
		// class DefFuncion { String nombre;  List<DefParametro> parametros;  Retorno retorno;  Cuerpo cuerpo; }
		public Object visit(DefFuncion node, Object param) {
			super.visit(node, node.getRetorno().getTipo());
			return null;
		}

		// class Retorno
		public Object visit(Retorno node, Object param) {
			if(node.getTipo() != null) {
				predicado(node.getTipo().getClass() != IdentType.class, "Retorno - No es de tipo simple",node.getStart());

			}
			return null;
		}
		
		
		// class Cuerpo
		public Object visit (Cuerpo node, Object param) {
			super.visit(node, param);
			return null;
		}
		
		//class Return
		public Object visit(Return node, Object param) {
			super.visit(node, param);
			if(param != null && node.getExpresion() !=null)
				predicado(node.getExpresion().getTipo().getClass() != param.getClass(), 
					"Return - Retorno de la función no es igual que retorno de cuerpo",node.getStart());
			else if(node.getExpresion() == null)
				predicado(param == null, 
					"Return - No debe tener expresión en funciones void",node.getStart());
			else
				predicado(node.getExpresion().getTipo() == param, 
					"Return - Retorno de la función no es igual que retorno de cuerpo",node.getStart());
			return null;
		}
		
		//class Cast { Tipo tipo;  Expresion expresion; }
		public Object visit(Cast node, Object param) {
			super.visit(node, param);
			
			predicado(node.getTipo().getClass() !=  IdentType.class && node.getExpresion().getClass() !=  IdentType.class, 
					"Cast - Solo se puede hacer cast de los tipos simples",node.getStart());
			
			predicado(node.getExpresion().getTipo().getClass() !=  IdentType.class, 
					"Cast - Solo se puede hacer cast a expresiones de tipo simple",node.getStart());
			
			predicado(node.getTipo().getClass() !=  node.getExpresion().getTipo().getClass(), 
					"Cast - Los tipos del cast y la expresión deben ser distintos",node.getStart());
			
			
			return null;
		}
		
		//class Navega { Expresion expresion;  String nombre; }
		public Object visit(Navega node, Object param) {
			super.visit(node, node.getNombre());
//			node.getExpresion().accept(this, node.getNombre());
			predicado(node.getExpresion().getTipo().getClass() ==  IdentType.class, 
					"Navegación - La expresión de comienzo de navegación debe ser tipo struct",node.getStart());
			
			
			return null;
		}
		
		
		
	/*
	 * Poner aquí los visit necesarios.
	 * Si se ha usado VGen solo hay que copiarlos de la clase 'visitor/_PlantillaParaVisitors.txt'.
	 */

	// public Object visit(Programa prog, Object param) {
	// ...
	// }

		// --------------------------------------------------------
		// Funciones auxiliares

		private boolean mismoTipo(Expresion a, Expresion b) {
			return (a.getTipo().getClass() == b.getTipo().getClass());
		}

	
	
	
	/**
	 * Método auxiliar opcional para ayudar a implementar los predicados de la Gramática Atribuida.
	 * 
	 * Ejemplo de uso (suponiendo implementado el método "esPrimitivo"):
	 * 	predicado(esPrimitivo(expr.tipo), "La expresión debe ser de un tipo primitivo", expr.getStart());
	 * 	predicado(esPrimitivo(expr.tipo), "La expresión debe ser de un tipo primitivo", null);
	 * 
	 * NOTA: El método getStart() indica la linea/columna del fichero fuente de donde se leyó el nodo.
	 * Si se usa VGen dicho método será generado en todos los nodos AST. Si no se quiere usar getStart() se puede pasar null.
	 * 
	 * @param condicion Debe cumplirse para que no se produzca un error
	 * @param mensajeError Se imprime si no se cumple la condición
	 * @param posicionError Fila y columna del fichero donde se ha producido el error. Es opcional (acepta null)
	 */
	private void predicado(boolean condicion, String mensajeError, Position posicionError) {
		if (!condicion)
			gestorErrores.error("Comprobación de tipos", mensajeError, posicionError);
	}
	
	
	private GestorErrores gestorErrores;
}
