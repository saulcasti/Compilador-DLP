/**
 * @generated VGen 1.3.3
 */

package ast;

import visitor.*;

//	intType:tipo -> 

public class IntType extends AbstractTipo {

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public String getNombreMAPL() {
		return "int";
	}

	@Override
	public char getSufijo() {
		return 'i';
	}
}

