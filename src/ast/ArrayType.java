/**
 * @generated VGen 1.3.3
 */

package ast;

import visitor.*;

//	arrayType:tipo -> dimension:literalInt  tipo:tipo

public class ArrayType extends AbstractTipo {

	public ArrayType(LiteralInt dimension, Tipo tipo) {
		this.dimension = dimension;
		this.tipo = tipo;

		searchForPositions(dimension, tipo);	// Obtener linea/columna a partir de los hijos
	}

	public ArrayType(Object dimension, Object tipo) {
		this.dimension = (LiteralInt) dimension;
		this.tipo = (Tipo) tipo;

		searchForPositions(dimension, tipo);	// Obtener linea/columna a partir de los hijos
	}

	public LiteralInt getDimension() {
		return dimension;
	}
	public void setDimension(LiteralInt dimension) {
		this.dimension = dimension;
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

	private LiteralInt dimension;
	private Tipo tipo;
}

