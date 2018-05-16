package semantico;

import java.util.*;

import ast.*;
import main.*;
import visitor.*;

/**
 * 
 * @author Sa�l Castillo - UO251370
 * 
 */
public class Identificacion extends DefaultVisitor {

	private GestorErrores gestorErrores;
	
	//Conjuntos auxiliares
	private Map<String, DefFuncion> funciones = new HashMap<String, DefFuncion>();
	private ContextMap<String, DefVariable> variables = new ContextMap<String, DefVariable>();
	private Map<String, DefEstructura> estructuras = new HashMap<String, DefEstructura>();
	
	public Identificacion(GestorErrores gestor) {
		this.gestorErrores = gestor;
	}

	
	/**
	 * Nodo DefVariable
	 * 
	 * defVariable:definicion -> nombre:String tipo:tipo ambito:int
	 * 			Predicado: 			variables.buscarActual(nombre) == null
	 * 			Reglas Sem�nticas: 	variables[nombre] = defVariable
	 * 
	 * class DefVariable { Tipo tipo;  String nombre; }
	 */
	public Object visit(DefVariable node, Object param) {
		
		DefVariable definicion = variables.getFromTop(node.getNombre());
		predicado(definicion == null, "Variable ya definida: " + node.getNombre(), node.getStart());
		variables.put(node.getNombre(), node);
		return super.visit(node, param);
	}

	
	/**
	 * Nodo DefFuncion
	 * 
	 * defFuncion:definicion -> nombre:String parametros:defVariable* retorno:retorno cuerpo:cuerpo
	 * 		Predicado:			funciones[nombre] == null
	 * 		Reglas Sem�nticas:	funciones[nombre]=defFuncion
	 * 											{
	 * 												variables.set()
	 * 												visit(par�metros i)
	 * 												visit(cuerpo, funciones[nombre])
	 * 												variable.reset()
	 * 											}
	 * 
	 * 
	 * class DefFuncion { String nombre;  List<DefParametro> parametros;  Retorno retorno;  Cuerpo cuerpo; }
	 */
public Object visit(DefFuncion node, Object param) {
		DefFuncion definicion = funciones.get(node.getNombre());
		predicado(definicion == null, "Funci�n ya definida: " + node.getNombre(), node.getStart());
		funciones.put(node.getNombre(), node);
		
		variables.set();
		super.visit(node, funciones.get(node.getNombre()));
		variables.reset();
		
		return null;
	}
	
	

	/**
	 * Nodo DefEstructura
	 * 
	 * defEstructura:definicion -> nombre:String defcampo:defCampo*
	 * 			Predicado: 			estructuras[nombre] == null
	 * 			Reglas Sem�nticas: 	estructuras[nombre]=defEstructura
	 *												{
	 *												    visit(defCampo i)
	 *												}
	 * 
	 * class DefEstructura { String nombre;  List<DefCampo> defcampo; }
	 */
	public Object visit(DefEstructura node, Object param) {
		DefEstructura definicion = estructuras.get(node.getNombre());
		predicado(definicion == null, "Estructura ya definida: " + node.getNombre(), node.getStart());
		
	
		// DefCampo--> Predicado: campos[nombre] == null |�Reglas Sem�nticas: campos[nombre] = defCamp
		Map<String, DefCampo> campos = new HashMap<String, DefCampo>();
		for(DefCampo campo:node.getDefcampo()) {
			DefCampo defCampo = campos.get(campo.getNombre());
			predicado(defCampo == null, "Campo ya definido: " + campo.getNombre(), campo.getStart());
			
			campos.put(campo.getNombre(), campo);
		}
		
		estructuras.put(node.getNombre(), node);
		
		return super.visit(node, param);
	}
	
	
	/**
	 * Nodo IdentType
	 * 
	 * identType:tipo ->nombre:String
	 * 		Predicado:			estructuras[nombre] != null
	 * 		Reglas Sem�nticas:	identType.definicion = estructuras[nombre]
	 * 
	 * class IdentType { String nombre; }
	 */
	public Object visit(IdentType node, Object param) {
		DefEstructura definicion = estructuras.get(node.getNombre());
		predicado(definicion != null,"Estructura no definida: " + node.getNombre(), node.getStart());
		
		node.setDefinicion(definicion);
		return super.visit(node, param);
	}
	
	/**
	 * Nodo Return
	 * 
	 * return:sentencia -> expresion:expresion
	 * 		Regla Sem�tica:	return.definicion = defFuncion
	 * 
	 * class Return { Expresion expresion; }
	 */
	public Object visit(Return node, Object param) {
		node.setFuncion((DefFuncion) param);
		return super.visit(node, param);
	}
	
	
	/**
	 * Nodo LlamadaFuncionSentencia
	 * 
	 * llamadaFuncionSentencia:sentencia -> nombre:String argumentos:expresion*
	 * 		Predicado:			funciones[nombre] != null
	 * 		Regla Sem�ntica:	llamadaFuncionSentencia.definicion = funciones[nombre]
	 * 
	 * class LlamadaFuncionSentencia { String nombre;  List<Expresion> argumentos; }
	 */
	public Object visit(LlamadaFuncionSentencia node, Object param) {
		DefFuncion definicion = funciones.get(node.getNombre());
		predicado(definicion != null,"Funcion no definida: " + node.getNombre(), node.getStart());

		node.setDefFuncion(definicion); 
		return super.visit(node, param);
	}
	

	/**
	 * Nodo Invocacion
	 * 
	 * invocacion:expresion -> nombre:String argumentos:expresion*
	 * 		Predicado:			funciones[nombre] != null
	 * 		Regla Sem�ntica:	invocacion.definicion = funciones[nombre]
	 * 
	 * class Invocacion { String nombre;  List<Expresion> argumentos; }
	 */
	public Object visit(Invocacion node, Object param) {
		DefFuncion definicion = funciones.get(node.getNombre());
		predicado(definicion != null,"Funcion no definida: " + node.getNombre(), node.getStart());
		
		node.setDefFuncion(definicion); // Enlazar referencia con definici�n

		return super.visit(node, param);
	}
	
	
	/**
	 * Nodo Variable
	 * 
	 * variable:expresion -> nombre:String
	 * 		Predicado:			variables.buscar(nombre) != null
	 * 		Regla Sem�ntica:	variable.definici�n = variablesbuscar(nombre)
	 * 
	 * class Variable { String nombre; }
	 */
	public Object visit(Variable node, Object param) {
		DefVariable definicion = variables.getFromAny(node.getNombre());
		predicado(definicion != null,"Variable no definida: " + node.getNombre(), node.getStart());
		node.setDefinicion(definicion); // Enlazar referencia con definici�n	
		return null;
	}

	
	
	/**
	 * M�todo auxiliar opcional para ayudar a implementar los predicados de la Gram�tica Atribuida.
	 * 
	 * Ejemplo de uso:
	 * 	predicado(variables.get(nombre), "La variable no ha sido definida", expr.getStart());
	 * 	predicado(variables.get(nombre), "La variable no ha sido definida", null);
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
			gestorErrores.error("Identificaci�n", mensajeError, posicionError);
	}


}
