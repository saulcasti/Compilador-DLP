/**
 * @generated VGen 1.3.3
 */

package ast;

import visitor.*;

//	return:sentencia -> expresion:expresion

public class Return extends AbstractSentencia {

	public Return(Expresion expresion) {
		this.expresion = expresion;

		searchForPositions(expresion);	// Obtener linea/columna a partir de los hijos
	}

	public Return(Object expresion) {
		this.expresion = (Expresion) expresion;

		searchForPositions(expresion);	// Obtener linea/columna a partir de los hijos
	}

	public Expresion getExpresion() {
		return expresion;
	}
	public void setExpresion(Expresion expresion) {
		this.expresion = expresion;
	}

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}

	
	
	public DefFuncion getFuncion() {
		return funcion;
	}

	public void setFuncion(DefFuncion funcion) {
		this.funcion = funcion;
	}



	private Expresion expresion;
	private DefFuncion funcion;
}

