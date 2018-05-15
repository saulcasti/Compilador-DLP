package semantico;

import java.util.List;

import ast.*;
import main.*;
import visitor.*;
/**
 * 
 * @author Sa�l Castillo - UO251370
 * 
 */
public class ComprobacionDeTipos extends DefaultVisitor {

	private GestorErrores gestorErrores;
	
	
	public ComprobacionDeTipos(GestorErrores gestor) {
		this.gestorErrores = gestor;
	}

	// class Asigna { Expresion left; Expresion right; }
		public Object visit(Asigna node, Object param) {
			super.visit(node, param);
			predicado(mismoTipo(node.getLeft(), node.getRight()), "Error. Asignaci�n - Los operandos deben ser del mismo tipo", node.getStart());
			predicado(node.getLeft().isModificable(), "Error. Asignaci�n - Se requiere expresi�n modificable", node.getLeft().getStart());
			predicado(node.getLeft().getTipo().getClass() != IdentType.class, "Error. Asignaci�n - El valor de la izquierda debe ser simple", node.getLeft().getStart());

			return null;
		}

		// class ExpresionBinaria { Expresion left; String operador; Expresion right; }
		public Object visit(ExpresionAritmetica node, Object param) {
			super.visit(node, param);
			predicado(mismoTipo(node.getLeft(), node.getRight()), "Error. Expresion aritm�tica - Los operandos deben ser del mismo tipo", node.getStart());

			node.setTipo(node.getLeft().getTipo());
			node.setModificable(false);
			return null;
		}
		
		//	class ExpresionBooleana{ Expresion left;  String operador;  Expresion right; }
		public Object visit(ExpresionBooleana node, Object param) {
			super.visit(node, param);
			predicado(mismoTipo(node.getLeft(), node.getRight()), "Error. Expresion Booleana - Los operandos deben ser del mismo tipo", node.getStart());

			if(node.getOperador() == "AND" || node.getOperador() == "OR")
				predicado(mismoTipo(node.getLeft(), node.getRight()) && node.getLeft().getTipo().getClass() == IntType.class, 
				"Error. Expresi�n Booleana - Boolean debe de ser de tipo entero",node.getStart());
			
			node.setTipo(new IntType());
			node.setModificable(false);
			
			return null;
		}

		//	class Negacion { Expresion expresion; }
		public Object visit(Negacion node, Object param) {
			super.visit(node, param);
			predicado(node.getExpresion().getTipo().getClass() == IntType.class, 
					"Error. Expresi�n Negaci�n - Boolean debe de ser de tipo entero",node.getStart());
			
			node.setTipo(new IntType());
			node.setModificable(false);
			return null;
		}
		
		// class Variable { String nombre; }
		public Object visit(Variable node, Object param) {
			super.visit(node, param); // �Hace falta?

			node.setTipo(node.getDefinicion().getTipo());
			node.setModificable(true);

			if(param != null && node.getTipo().getClass() == IdentType.class) {
				IdentType struct= (IdentType) node.getTipo();
				DefEstructura defS= struct.getDefinicion();

				boolean i=false;
				for(DefCampo dC :defS.getDefcampo()) {
					if(dC.getNombre().equals((String)param)) {
						i= true;
						Tipo tipo = dC.getTipo();
						if(dC.getTipo().getClass() == ArrayType.class) {
							node.setTipo(((ArrayType)tipo).getTipo());
						}
						else
							node.setTipo(dC.getTipo());

						break;
					}
				}predicado(i,"Error. Estructura - El campo al que se hace referencia no existe en la estructura " 
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
					"Error. Variable array - la posici�n se indica mediante un entero",node.getStart());

			if(node.getPosicion().getTipo().getClass() == IntType.class ) {
				predicado(node.getIdentificacion().getTipo().getClass() == ArrayType.class, 
						"Error. Variable array - el tipo de la variable debe ser array",node.getStart());

			}
			node.setTipo(((ArrayType)node.getIdentificacion().getTipo()).getTipo());
			node.setModificable(true);

			return null;
		}

		//	class ArrayType { LiteralInt dimension;  Tipo tipo; }
		public Object visit(ArrayType node, Object param) {
			// Puede no valor para nada
			return node.getTipo();
		}
		
		
		// class DefFuncion { String nombre;  List<DefParametro> parametros;  Retorno retorno;  Cuerpo cuerpo; }
		public Object visit(DefFuncion node, Object param) {
			super.visit(node, node.getRetorno().getTipo());
			return null;
		}

		//class Retorno { Tipo tipo; }
		public Object visit(Retorno node, Object param) {
			if(node.getTipo() != null) {
				predicado(node.getTipo().getClass() != IdentType.class, "Error. Retorno - No es de tipo simple",node.getStart());

			}
			return null;
		}
		
		
		//	class Cuerpo { List<DefVariable> defvariable;  List<Sentencia> sentencia; }
		public Object visit (Cuerpo node, Object param) {
			super.visit(node, param);
			return null;
		}
		
		//	class Return { Expresion expresion; }
		public Object visit(Return node, Object param) {
			super.visit(node, param);
			if(param != null && node.getExpresion() !=null)
				predicado(node.getExpresion().getTipo().getClass() == param.getClass(), 
					"Error. Return - Retorno de la funci�n no es igual que retorno de cuerpo",node.getStart());
			else if(param == null )
				predicado(node.getExpresion() == null, 
					"Error. Return - No debe tener expresi�n en funciones void",node.getStart());
			else if(node.getExpresion() == null) 
				predicado(param ==null, 
					"Error. Return - Deber�a retornar alg�n valor",node.getStart());
			

			
			return null;
		}
		
		//	class Cast { Tipo tipo;  Expresion expresion; }
		public Object visit(Cast node, Object param) {
			super.visit(node, param);
			
			predicado(node.getTipo().getClass() !=  IdentType.class && node.getExpresion().getClass() !=  IdentType.class, 
					"Error. Cast - Solo se puede hacer cast de los tipos simples",node.getStart());
			
			predicado(node.getExpresion().getTipo().getClass() !=  IdentType.class, 
					"Error. Cast - Solo se puede hacer cast a expresiones de tipo simple",node.getStart());
			
			predicado(node.getTipo().getClass() !=  node.getExpresion().getTipo().getClass(), 
					"Error. Cast - Los tipos del cast y la expresi�n deben ser distintos",node.getStart());
			
			
			return null;
		}
		
		//class Navega { Expresion expresion;  String nombre; }
		public Object visit(Navega node, Object param) {
			super.visit(node, node.getNombre());
			predicado(node.getExpresion().getTipo().getClass() ==  IdentType.class, 
					"Error. Navegaci�n - La expresi�n de comienzo de navegaci�n debe ser tipo struct",node.getStart());
			
			if(node.getExpresion().getTipo().getClass() == IdentType.class) {
				DefEstructura defS= ((IdentType) node.getExpresion().getTipo()).getDefinicion();
				for(DefCampo dC :defS.getDefcampo()) {
					if(dC.getNombre().equals(node.getNombre())) {
							node.setTipo(dC.getTipo());
						 break;
					}
				}
				
			}else
			node.setTipo(node.getExpresion().getTipo());
			node.setModificable(true);
			
			return null;
		}
		
		
		//	class DefVariable { String nombre;  Tipo tipo; }
		public Object visit(DefVariable node, Object param) {
			if(node.getAmbito() == 3) {
				super.visit(node, param);
				predicado(node.getTipo().getClass() !=  ArrayType.class, 
						"Error. Definici�n de funci�n - Los parametros han de ser de tipos simples",node.getStart());
			}
			return null;
		}
		
		//	class While { Expresion condicion;  List<Sentencia> cierto; }
		public Object visit(While node, Object param) {
			super.visit(node, param);
			predicado(node.getCondicion().getTipo().getClass() == IntType.class, 
					"Error. Condici�n en while - La condici�n debe de ser de tipo entero",node.getStart());

			return null;
		}


		//	class IfElse { Expresion condicion;  List<Sentencia> cierto;  List<Sentencia> falso; }
		public Object visit(IfElse node, Object param) {
			super.visit(node, param);
			predicado(node.getCondicion().getTipo().getClass() == IntType.class, 
					"Error. Condici�n en if/else - La condici�n debe de ser de tipo entero",node.getStart());

			return null;
		}

		//	class Read { Expresion expresion; }
		public Object visit(Read node, Object param) {
			super.visit(node, param);

			predicado(node.getExpresion().getTipo().getClass() != IdentType.class, 
					"Error. Read - La expresi�n debe ser de tipo simple",node.getStart());
			predicado(node.getExpresion().isModificable(), 
					"Error. Read - La expresi�n debe ser modificable",node.getStart());

			return null;
		}

		//		class Print { Expresion expresion; }
		public Object visit(Print node, Object param) {
			super.visit(node, param);
			predicado(node.getExpresion().getTipo() != null, 
					"Error. Print - La expresi�n debe devolver algo",node.getStart());
			if(node.getExpresion().getTipo() != null)
				predicado(node.getExpresion().getTipo().getClass() != IdentType.class, 
				"Error. Print - La expresi�n debe ser de tipo simple",node.getStart());

			return null;
		}

		//	class Println { Expresion expresion; }
		public Object visit(Println node, Object param) {
			super.visit(node, param);
			//Este print puede no llevar nada
			if(node.getExpresion() != null)
				predicado(node.getExpresion().getTipo().getClass() != IdentType.class, 
				"Error. Println - La expresi�n debe ser de tipo simple",node.getStart());

			return null;
		}

		//	class Printsp { Expresion expresion; }
		public Object visit(Printsp node, Object param) {
			super.visit(node, param);

			predicado(node.getExpresion().getTipo() != null, 
					"Error. Print - La expresi�n debe devolver algo",node.getStart());
			if(node.getExpresion().getTipo() != null)
				predicado(node.getExpresion().getTipo().getClass() != IdentType.class, 
					"Error. Printsp - La expresi�n debe ser de tipo simple",node.getStart());

			return null;
		}


		//	class Invocacion { String nombre;  List<Expresion> argumentos; }
		public Object visit(Invocacion node, Object param) {
			super.visit(node, param);

			
				node.setTipo(node.getDefFuncion().getRetorno().getTipo());
				node.setModificable(false);

				List<DefVariable> parametros = node.getDefFuncion().getParametros();
				if(parametros.size() != node.getArgumentos().size()) {
					predicado(parametros.size() == node.getArgumentos().size(), 
							"Error. Invocaci�n - El n�mero de argumentos es incorrecto",node.getStart());
				}
				else {
					boolean parametrosOK=true;
					for(int i=0; i<parametros.size();i++) {
						if(parametros.get(i).getTipo().getClass() != node.getArgumentos().get(i).getTipo().getClass())
							parametrosOK = false;
					}
					predicado(parametrosOK, 
							"Error. Invocaci�n - El tipo de alg�n argumento es incorrecto",node.getStart());

				}
			return null;
		}

		//	class LlamadaFuncionSentencia { String nombre;  List<Expresion> argumentos; }
		public Object visit(LlamadaFuncionSentencia node, Object param) {
			super.visit(node, param);
			
				List<DefVariable> parametros = node.getDefFuncion().getParametros();
				if(parametros.size() != node.getArgumentos().size()) {
					predicado(parametros.size() == node.getArgumentos().size(), 
							"Error. Invocaci�n Sentencia - El n�mero de argumentos es incorrecto",node.getStart());
				}
				else {
					boolean parametrosOK=true;
					for(int i=0; i<parametros.size();i++) {
						if(parametros.get(i).getTipo().getClass() != node.getArgumentos().get(i).getTipo().getClass())
							parametrosOK = false;
					}
					predicado(parametrosOK, 
							"Error. Invocaci�n Sentencia - El tipo de alg�n argumento es incorrecto",node.getStart());

				}
			return null;
		}

		
		//	class ConParentesis { Expresion expresion; }
		public Object visit(ConParentesis node, Object param) {
			super.visit(node, param);
			node.setTipo(node.getExpresion().getTipo());
			node.setModificable(node.getExpresion().isModificable());
			return null;
		}
		
//		class MenosUnario { Expresion expresion; }
		public Object visit(MenosUnario node, Object param) {
			super.visit(node, param);
			node.setTipo(node.getExpresion().getTipo());
			node.setModificable(node.getExpresion().isModificable());
			return null;
		}
		// --------------------------------------------------------
		// Funciones auxiliares

		private boolean mismoTipo(Expresion a, Expresion b) {
			return (a.getTipo().getClass() == b.getTipo().getClass());
		}

	
	
	
	/**
	 * M�todo auxiliar opcional para ayudar a implementar los predicados de la Gram�tica Atribuida.
	 * 
	 * Ejemplo de uso (suponiendo implementado el m�todo "esPrimitivo"):
	 * 	predicado(esPrimitivo(expr.tipo), "La expresi�n debe ser de un tipo primitivo", expr.getStart());
	 * 	predicado(esPrimitivo(expr.tipo), "La expresi�n debe ser de un tipo primitivo", null);
	 * 
	 * NOTA: El m�todo getStart() indica la linea/columna del fichero fuente de donde se ley� el nodo.
	 * Si se usa VGen dicho m�todo ser� generado en todos los nodos AST. Si no se quiere usar getStart() se puede pasar null.
	 * 
	 * @param condicion Debe cumplirse para que no se produzca un error
	 * @param mensajeError Se imprime si no se cumple la condici�n
	 * @param posicionError Fila y columna del fichero donde se ha producido el error. Es opcional (acepta null)
	 */
	private void predicado(boolean condicion, String mensajeError, Position posicionError) {
		if (!condicion)
			gestorErrores.error("Comprobaci�n de tipos", mensajeError, posicionError);
	}
	

}
