package generacionDeCodigo;

import java.io.*;
import java.util.*;

import ast.*;
import visitor.*;
enum Funcion {
	DIRECCION, VALOR
}

public class SeleccionDeInstrucciones extends DefaultVisitor {

	private PrintWriter writer;
	private String sourceFile;
	
	private Map<String, String> instruccion = new HashMap<String, String>();
	
	public SeleccionDeInstrucciones(Writer writer, String sourceFile) {
		
		this.writer = new PrintWriter(writer);
		this.sourceFile = sourceFile;

		instruccion.put("+", "add");
		instruccion.put("-", "sub");
		instruccion.put("*", "mul");
		instruccion.put("/", "div");
	}

	//	class Programa { List<Definicion> definicion; }
	public Object visit(Programa node, Object param) {
		genera("#source \"" + sourceFile + "\"");
		visitChildren(node.getDefinicion(), param);
		genera("halt");
		return null;
	}

	// class DefVariable { Tipo tipo; String nombre; }
	public Object visit(DefVariable node, Object param) {
		genera("#GLOBAL " + node.getNombre() + ":" + node.getTipo().getNombreMAPL());
		return null;
	}

	//	class DefEstructura { String nombre;  List<DefCampo> defcampo; }
	public Object visit(DefEstructura node, Object param) {
		String salida = "#TYPE " + node.getNombre() + ":{";
		for (DefCampo campo:node.getDefcampo()) {
			salida += "\n\t"+ campo.getNombre()+ ":" + campo.getTipo().getNombreMAPL();
		}
		genera(salida +"\n}");
		return null;
	}
	
	//	class Print { Expresion expresion; }
	public Object visit(Print node, Object param) {
		genera("#line " + node.getEnd().getLine());
		node.getExpresion().accept(this, Funcion.VALOR);
		genera("out", node.getExpresion().getTipo());
		return null;
	}
	

	//	class Asigna { Expresion left;  Expresion right; }
	public Object visit(Asigna node, Object param) {
		genera("#line " + node.getEnd().getLine());
		node.getLeft().accept(this, Funcion.DIRECCION);
		node.getRight().accept(this, Funcion.VALOR);
		genera("store", node.getLeft().getTipo());

		return null;
	}

	//	class ExpresionBinaria { Expresion left;  String operador;  Expresion right; }
	public Object visit(ExpresionBinaria node, Object param) {
		assert (param == Funcion.VALOR);
		node.getLeft().accept(this, Funcion.VALOR);
		node.getRight().accept(this, Funcion.VALOR);
		genera(instruccion.get(node.getOperador()), node.getTipo());
		return null;
	}

	// class Variable { String nombre; }
	public Object visit(Variable node, Object param) {
		if (((Funcion) param) == Funcion.VALOR) {
			visit(node, Funcion.DIRECCION);
			genera("load", node.getTipo());
		} else { // Funcion.DIRECCION
			assert (param == Funcion.DIRECCION);
			if(node.getDefinicion().getAmbito()) {
				genera("pusha " + node.getDefinicion().getDireccion());
			}
			else {
				genera("pusha BP");
				genera("push " + node.getDefinicion().getDireccion());
			}
		}
		return null;
	}

	// class LiteralInt { String valor; }
	public Object visit(LiteralInt node, Object param) {
		assert (param == Funcion.VALOR);
		genera("push " + node.getValor());
		return null;
	}

	// class LiteralReal { String valor; }
	public Object visit(LiteralReal node, Object param) {
		assert (param == Funcion.VALOR);
		genera("pushf " + node.getValor());
		return null;
	}

	
	//	class LiteralChar { String valor; }
	public Object visit(LiteralChar node, Object param) {
		assert (param == Funcion.VALOR);
		genera("push " + node.getValor());
		return null;
	}
	
	
	

	
	// Método auxiliar recomendado -------------
	private void genera(String instruccion) {
		writer.println(instruccion);
	}

	private void genera(String instruccion, Tipo tipo) {
		genera(instruccion + tipo.getSufijo());
	}


}
