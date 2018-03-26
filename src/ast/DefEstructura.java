/**
 * @generated VGen 1.3.3
 */

package ast;

import java.util.*;
import visitor.*;

//	defEstructura:definicion -> nombre:String  defcampo:defCampo*

public class DefEstructura extends AbstractDefinicion {

	public DefEstructura(String nombre, List<DefCampo> defcampo) {
		this.nombre = nombre;
		this.defcampo = defcampo;

		searchForPositions(defcampo);	// Obtener linea/columna a partir de los hijos
	}

	@SuppressWarnings("unchecked")
	public DefEstructura(Object nombre, Object defcampo) {
		this.nombre = (nombre instanceof Token) ? ((Token)nombre).getLexeme() : (String) nombre;
		this.defcampo = (List<DefCampo>) defcampo;

		searchForPositions(nombre, defcampo);	// Obtener linea/columna a partir de los hijos
	}

	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<DefCampo> getDefcampo() {
		return defcampo;
	}
	public void setDefcampo(List<DefCampo> defcampo) {
		this.defcampo = defcampo;
	}

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}

	private String nombre;
	private List<DefCampo> defcampo;
}

