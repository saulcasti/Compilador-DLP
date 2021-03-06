/**
 * @generated VGen 1.3.3
 */

package ast;

import visitor.*;

//	retorno -> tipo:tipo

public class Retorno extends AbstractTraceable implements AST {

	public Retorno(Tipo tipo) {
		this.tipo = tipo;

		searchForPositions(tipo);	// Obtener linea/columna a partir de los hijos
	}

	public Retorno(Object tipo) {
		this.tipo = (Tipo) tipo;

		searchForPositions(tipo);	// Obtener linea/columna a partir de los hijos
	}

	public Tipo getTipo() {
		return tipo;
	}
	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}

	private Tipo tipo;
}

