/**
 * @generated VGen 1.3.3
 */

package ast;

import java.util.*;
import visitor.*;

//	while:sentencia -> condicion:expresion  cierto:sentencia*

public class While extends AbstractSentencia {

	public While(Expresion condicion, List<Sentencia> cierto) {
		this.condicion = condicion;
		this.cierto = cierto;

		searchForPositions(condicion, cierto);	// Obtener linea/columna a partir de los hijos
	}

	@SuppressWarnings("unchecked")
	public While(Object condicion, Object cierto) {
		this.condicion = (Expresion) condicion;
		this.cierto = (List<Sentencia>) cierto;

		searchForPositions(condicion, cierto);	// Obtener linea/columna a partir de los hijos
	}

	public Expresion getCondicion() {
		return condicion;
	}
	public void setCondicion(Expresion condicion) {
		this.condicion = condicion;
	}

	public List<Sentencia> getCierto() {
		return cierto;
	}
	public void setCierto(List<Sentencia> cierto) {
		this.cierto = cierto;
	}

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}

	private Expresion condicion;
	private List<Sentencia> cierto;
}

