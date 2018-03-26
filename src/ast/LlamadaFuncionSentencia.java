/**
 * @generated VGen 1.3.3
 */

package ast;

import java.util.*;
import visitor.*;

//	llamadaFuncionSentencia:sentencia -> nombre:String  argumentos:expresion*

public class LlamadaFuncionSentencia extends AbstractSentencia {

	public LlamadaFuncionSentencia(String nombre, List<Expresion> argumentos) {
		this.nombre = nombre;
		this.argumentos = argumentos;

		searchForPositions(argumentos);	// Obtener linea/columna a partir de los hijos
	}

	@SuppressWarnings("unchecked")
	public LlamadaFuncionSentencia(Object nombre, Object argumentos) {
		this.nombre = (nombre instanceof Token) ? ((Token)nombre).getLexeme() : (String) nombre;
		this.argumentos = (List<Expresion>) argumentos;

		searchForPositions(nombre, argumentos);	// Obtener linea/columna a partir de los hijos
	}

	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<Expresion> getArgumentos() {
		return argumentos;
	}
	public void setArgumentos(List<Expresion> argumentos) {
		this.argumentos = argumentos;
	}

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}

	public DefFuncion getDefFuncion() {
		return defFuncion;
	}

	public void setDefFuncion(DefFuncion defFuncion) {
		this.defFuncion = defFuncion;
	}



	private DefFuncion defFuncion;
	private String nombre;
	private List<Expresion> argumentos;
}

