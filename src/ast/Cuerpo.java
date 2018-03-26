/**
 * @generated VGen 1.3.3
 */

package ast;

import java.util.*;
import visitor.*;

//	cuerpo -> defvariable:defVariable*  sentencia:sentencia*

public class Cuerpo extends AbstractTraceable implements AST {

	public Cuerpo(List<DefVariable> defvariable, List<Sentencia> sentencia) {
		this.defvariable = defvariable;
		this.sentencia = sentencia;

		searchForPositions(defvariable, sentencia);	// Obtener linea/columna a partir de los hijos
	}

	@SuppressWarnings("unchecked")
	public Cuerpo(Object defvariable, Object sentencia) {
		this.defvariable = (List<DefVariable>) defvariable;
		this.sentencia = (List<Sentencia>) sentencia;

		searchForPositions(defvariable, sentencia);	// Obtener linea/columna a partir de los hijos
	}

	public List<DefVariable> getDefvariable() {
		return defvariable;
	}
	public void setDefvariable(List<DefVariable> defvariable) {
		this.defvariable = defvariable;
	}

	public List<Sentencia> getSentencia() {
		return sentencia;
	}
	public void setSentencia(List<Sentencia> sentencia) {
		this.sentencia = sentencia;
	}

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}

	private List<DefVariable> defvariable;
	private List<Sentencia> sentencia;
}

