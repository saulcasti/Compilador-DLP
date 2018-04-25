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
		instruccion.put("<", "lt");
		instruccion.put(">", "gt");
		instruccion.put("AND", "and");
		instruccion.put("OR", "or");
		instruccion.put("IGUAL", "eq");
		instruccion.put("DISTINTO", "ne");
		instruccion.put("MAYORIGUAL", "ge");
		instruccion.put("MENORIGUAL", "le");
		
	}

	//	class Programa { List<Definicion> definicion; }
	public Object visit(Programa node, Object param) {
		genera("#source \"" + sourceFile + "\"");
		genera("call main");
		genera("halt");
		visitChildren(node.getDefinicion(), param);
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
	
//	class DefFuncion { String nombre;  List<DefParametro> parametros;  Retorno retorno;  Cuerpo cuerpo; }
	public Object visit(DefFuncion node, Object param) {
		genera(node.getNombre() +":");
		super.visit(node, param);
		if(node.getRetorno().getTipo() == null) {
			int sumaVariables=getSizeVariables(node.getCuerpo().getDefvariable());
			int sumaParametros=getSizeParam(node.getParametros());
			
			genera("ret "+0 +","+sumaVariables+","+sumaParametros);
		}
		
		return null;
	}
	
	private int getSizeVariables(List<DefVariable> definiciones) {
		int suma=0;
		for(DefVariable def:definiciones) {
			suma += def.getTipo().getSize();
		}
		return suma;
	}
	
	private int getSizeParam(List<DefParametro> definiciones) {
		int suma=0;
		for(DefParametro def:definiciones) {
			suma += def.getTipo().getSize();
		}
		return suma;
	}
	
//	class Cuerpo { List<DefVariable> defvariable;  List<Sentencia> sentencia; }
	public Object visit(Cuerpo node, Object param) {
		int sumaVariablesLocales = getSizeVariables(node.getDefvariable());
		genera("enter " + sumaVariablesLocales);
		super.visit(node, param);
		return null;
	}
	
//	class Return { Expresion expresion; }
	public Object visit(Return node, Object param) {
		int sumaVariablesLocales = getSizeVariables(node.getFuncion().getCuerpo().getDefvariable());
		int sumaParametros=getSizeParam(node.getFuncion().getParametros());
		int tamanioReturn=node.getExpresion().getTipo().getSize();
		genera("#line " + node.getEnd().getLine());
		genera("ret "+tamanioReturn +","+sumaVariablesLocales+","+sumaParametros);
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
		if(node.getOperador() == "AND" || node.getOperador() == "OR")
			genera(instruccion.get(node.getOperador()));
		else
			genera(instruccion.get(node.getOperador()), node.getRight().getTipo());
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
				genera("add");
			}
		}
		return null;
	}
	
	//	class VarArray { Expresion identificacion;  Expresion posicion; }
	public Object visit(VarArray node, Object param) {
			node.getIdentificacion().accept(this, Funcion.DIRECCION);
			node.getPosicion().accept(this, Funcion.VALOR);
			genera("push "+ ((ArrayType) node.getIdentificacion().getTipo()).getTipo().getSize());
			genera("mul");
			genera("add");
			if(param == Funcion.VALOR) {
				genera("load", node.getTipo());
			}
		
		return null;
	}

	//class Navega { Expresion expresion;  String nombre; }
	public Object visit(Navega node, Object param) {
		
		super.visit(node, Funcion.DIRECCION);
		if(node.getExpresion().getTipo().getClass() == IdentType.class) {
			DefEstructura defS= ((IdentType) node.getExpresion().getTipo()).getDefinicion();
			for(DefCampo dC :defS.getDefcampo()) {
				if(dC.getNombre().equals(node.getNombre())) {
					genera("push " + dC.getDireccion());
					genera("add");
					break;
				}
			}
			if(param == Funcion.VALOR) {
				genera("load", node.getTipo());
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
		genera("pushb " + node.getValor());
		return null;
	}
	
	//	class LlamadaFuncionSentencia { String nombre;  List<Expresion> argumentos; }
	public Object visit(LlamadaFuncionSentencia node, Object param) {
		genera("#line " + node.getEnd().getLine());
		genera("call " + node.getNombre());
		if(node.getDefFuncion().getRetorno() != null) {
			genera("pop");
		}
		return null;
	}
	
	//	class Invocacion { String nombre;  List<Expresion> argumentos; }
	public Object visit(Invocacion node, Object param) {
		genera("call " + node.getNombre());
		return null;
	}

//	class Cast { Tipo tipo;  Expresion expresion; }
	public Object visit(Cast node, Object param) {
		node.getExpresion().accept(this, Funcion.VALOR);
		genera(node.getExpresion().getTipo().getSufijo()+"2"+node.getTipo().getSufijo());
		return null;
	}
	
//	class Read { Expresion expresion; }
	public Object visit(Read node, Object param) {
		node.getExpresion().accept(this, Funcion.DIRECCION);
		genera("in", node.getExpresion().getTipo());
		genera("store", node.getExpresion().getTipo());
		return null;
	}

	
	// M�todo auxiliar recomendado -------------
	private void genera(String instruccion) {
		writer.println(instruccion);
	}

	private void genera(String instruccion, Tipo tipo) {
		genera(instruccion + tipo.getSufijo());
	}


}
