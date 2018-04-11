/**
 * @generated VGen 1.3.3
 */

package ast;

import visitor.*;

//	identType:tipo -> nombre:String

public class IdentType extends AbstractTipo {

	public IdentType(String nombre) {
		this.nombre = nombre;
	}

	public IdentType(Object nombre) {
		this.nombre = (nombre instanceof Token) ? ((Token)nombre).getLexeme() : (String) nombre;

		searchForPositions(nombre);	// Obtener linea/columna a partir de los hijos
	}

	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}

	private String nombre;
	private DefEstructura definicion;

	public void setDefinicion(DefEstructura definicion) {
		this.definicion=definicion;
		
	}

	public DefEstructura getDefinicion() {
		return definicion;
	}

	@Override
	public int getSize() {
		return definicion.getSize();
	}

	@Override
	public char getSufijo() {
		return 'v';
	}

	@Override
	public String getNombreMAPL() {
		return definicion.getNombre();
	}
}

