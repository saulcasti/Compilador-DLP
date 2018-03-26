/**
 * @generated VGen 1.3.3
 */

package ast;

import java.util.*;
import visitor.*;

//	ifElse:sentencia -> condicion:expresion  cierto:sentencia*  falso:sentencia*

public class IfElse extends AbstractSentencia {

	public IfElse(Expresion condicion, List<Sentencia> cierto, List<Sentencia> falso) {
		this.condicion = condicion;
		this.cierto = cierto;
		this.falso = falso;

		searchForPositions(condicion, cierto, falso);	// Obtener linea/columna a partir de los hijos
	}

	@SuppressWarnings("unchecked")
	public IfElse(Object condicion, Object cierto, Object falso) {
		this.condicion = (Expresion) condicion;
		this.cierto = (List<Sentencia>) cierto;
		this.falso = (List<Sentencia>) falso;

		searchForPositions(condicion, cierto, falso);	// Obtener linea/columna a partir de los hijos
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

	public List<Sentencia> getFalso() {
		return falso;
	}
	public void setFalso(List<Sentencia> falso) {
		this.falso = falso;
	}

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}

	private Expresion condicion;
	private List<Sentencia> cierto;
	private List<Sentencia> falso;
}

