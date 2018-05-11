/**
 * @generated VGen 1.3.3
 */

package ast;

import visitor.*;

//	defVariable:definicion -> nombre:String  tipo:tipo

public class DefVariable extends AbstractDefinicion {

	public DefVariable(String nombre, Tipo tipo) {
		this.nombre = nombre;
		this.tipo = tipo;

		searchForPositions(tipo);	// Obtener linea/columna a partir de los hijos
	}

	public DefVariable(Object nombre, Object tipo, int ambito) {
		this.nombre = (nombre instanceof Token) ? ((Token)nombre).getLexeme() : (String) nombre;
		this.tipo = (Tipo) tipo;
		this.ambito = ambito;

		searchForPositions(nombre, tipo);	// Obtener linea/columna a partir de los hijos
	}

	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
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

	public int getDireccion() {
		return direccion;
	}

	public void setDireccion(int direccion) {
		this.direccion = direccion;
	}
	
	private String nombre;
	private Tipo tipo;
	private int ambito; // 0 global, 1 local, 2 parámetro
	private int direccion;
	
	
	public int getAmbito() {
		return ambito;
	}

	public void setAmbito(int ambito) {
		this.ambito = ambito;
	}
	
	
	
}

