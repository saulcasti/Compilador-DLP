/**
 * @generated VGen 1.3.3
 */

package ast;

import visitor.*;

//	varArray:expresion -> identificacion:expresion  posicion:expresion

public class VarArray extends AbstractExpresion {

	public VarArray(Expresion identificacion, Expresion posicion) {
		this.identificacion = identificacion;
		this.posicion = posicion;

		searchForPositions(identificacion, posicion);	// Obtener linea/columna a partir de los hijos
	}

	public VarArray(Object identificacion, Object posicion) {
		this.identificacion = (Expresion) identificacion;
		this.posicion = (Expresion) posicion;

		searchForPositions(identificacion, posicion);	// Obtener linea/columna a partir de los hijos
	}

	public Expresion getIdentificacion() {
		return identificacion;
	}
	public void setIdentificacion(Expresion identificacion) {
		this.identificacion = identificacion;
	}

	public Expresion getPosicion() {
		return posicion;
	}
	public void setPosicion(Expresion posicion) {
		this.posicion = posicion;
	}

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}

	private Expresion identificacion;
	private Expresion posicion;
	
}

