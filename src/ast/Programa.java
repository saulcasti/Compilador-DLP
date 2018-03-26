/**
 * @generated VGen 1.3.3
 */

package ast;

import java.util.*;
import visitor.*;

//	programa -> definicion:definicion*

public class Programa extends AbstractTraceable implements AST {

	public Programa(List<Definicion> definicion) {
		this.definicion = definicion;

		searchForPositions(definicion);	// Obtener linea/columna a partir de los hijos
	}

	@SuppressWarnings("unchecked")
	public Programa(Object definicion) {
		this.definicion = (List<Definicion>) definicion;

		searchForPositions(definicion);	// Obtener linea/columna a partir de los hijos
	}

	public List<Definicion> getDefinicion() {
		return definicion;
	}
	public void setDefinicion(List<Definicion> definicion) {
		this.definicion = definicion;
	}

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}

	private List<Definicion> definicion;
}

