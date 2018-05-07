package semantico;

import java.util.*;

import ast.*;
import main.*;
import visitor.*;


public class Identificacion extends DefaultVisitor {

	private GestorErrores gestorErrores;
	private Map<String, DefFuncion> funciones = new HashMap<String, DefFuncion>();
	private ContextMap<String, DefVariable> variables = new ContextMap<String, DefVariable>();
	private Map<String, DefEstructura> estructuras = new HashMap<String, DefEstructura>();
	
	public Identificacion(GestorErrores gestor) {
		this.gestorErrores = gestor;
	}

	
	
//	class DefVariable { Tipo tipo;  String nombre; }
	public Object visit(DefVariable node, Object param) {
		
		DefVariable definicion = variables.getFromTop(node.getNombre());
		predicado(definicion == null, "Variable ya definida: " + node.getNombre(), node.getStart());
		variables.put(node.getNombre(), node);
		return super.visit(node, param);
	}
	
	//	class DefParametro { String nombre;  Tipo tipo; }
	public Object visit(DefParametro node, Object param) {
		
		DefVariable definicion = variables.getFromTop(node.getNombre());
		predicado(definicion == null, "Variable ya definida: " + node.getNombre(), node.getStart());
		
		variables.put(node.getNombre(), new DefVariable(node.getNombre(), node.getTipo(), false)); // false == local
		return super.visit(node, param);
	}
	
//	class Variable { String nombre; }
	public Object visit(Variable node, Object param) {
		DefVariable definicion = variables.getFromAny(node.getNombre());
		predicado(definicion != null,"Variable no definida: " + node.getNombre(), node.getStart());
		node.setDefinicion(definicion); // Enlazar referencia con definición
		
		
		
		return null;
	}


	
//	class DefFuncion { String nombre;  List<DefParametro> parametros;  Retorno retorno;  Cuerpo cuerpo; }
	public Object visit(DefFuncion node, Object param) {
		DefFuncion definicion = funciones.get(node.getNombre());
		predicado(definicion == null, "Función ya definida: " + node.getNombre(), node.getStart());
		funciones.put(node.getNombre(), node);
		
		variables.set();
		super.visit(node, funciones.get(node.getNombre()));
		variables.reset();
		
		return null;
	}
//	class Return { Expresion expresion; }
	public Object visit(Return node, Object param) {
		node.setFuncion((DefFuncion) param);
		return super.visit(node, param);
	}
	
	//	class Invocacion { String nombre;  List<Expresion> argumentos; }
	public Object visit(Invocacion node, Object param) {
		DefFuncion definicion = funciones.get(node.getNombre());
		predicado(definicion != null,"Funcion no definida: " + node.getNombre(), node.getStart());
		
		node.setDefFuncion(definicion); // Enlazar referencia con definición

		return super.visit(node, param);
	}

	//	class LlamadaFuncionSentencia { String nombre;  List<Expresion> argumentos; }
	public Object visit(LlamadaFuncionSentencia node, Object param) {
		DefFuncion definicion = funciones.get(node.getNombre());
		predicado(definicion != null,"Funcion no definida: " + node.getNombre(), node.getStart());

		node.setDefFuncion(definicion); // Enlazar referencia con definición
		return super.visit(node, param);
	}
	
	
	//	class DefEstructura { String nombre;  List<DefCampo> defcampo; }
	public Object visit(DefEstructura node, Object param) {
		DefEstructura definicion = estructuras.get(node.getNombre());
		predicado(definicion == null, "Estructura ya definida: " + node.getNombre(), node.getStart());
		
		Map<String, DefCampo> campos = new HashMap<String, DefCampo>();
		for(DefCampo campo:node.getDefcampo()) {
			DefCampo defCampo = campos.get(campo.getNombre());
			predicado(defCampo == null, "Campo ya definido: " + campo.getNombre(), campo.getStart());
			
			campos.put(campo.getNombre(), campo);
		}
		
		estructuras.put(node.getNombre(), node);
		
		return super.visit(node, param);
	}
	
	
	//	class IdentType { String nombre; }
	public Object visit(IdentType node, Object param) {
		DefEstructura definicion = estructuras.get(node.getNombre());
		predicado(definicion != null,"Estructura no definida: " + node.getNombre(), node.getStart());
		
		node.setDefinicion(definicion); // Enlazar referencia con definición
		return super.visit(node, param);
	}
	
	/*
	 * Poner aquí los visit necesarios.
	 * Si se ha usado VGen solo hay que copiarlos de la clase 'visitor/_PlantillaParaVisitors.txt'.
	 */

	// public Object visit(Programa prog, Object param) {
	// ...
	// }

	
	
	
	
	/**
	 * Método auxiliar opcional para ayudar a implementar los predicados de la Gramática Atribuida.
	 * 
	 * Ejemplo de uso:
	 * 	predicado(variables.get(nombre), "La variable no ha sido definida", expr.getStart());
	 * 	predicado(variables.get(nombre), "La variable no ha sido definida", null);
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
			gestorErrores.error("Identificación", mensajeError, posicionError);
	}


}
