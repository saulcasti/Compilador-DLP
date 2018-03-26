/**
 * @generated VGen 1.3.3
 */

package visitor;

import ast.*;

public interface Visitor {
	public Object visit(Programa node, Object param);
	public Object visit(DefVariable node, Object param);
	public Object visit(DefFuncion node, Object param);
	public Object visit(Retorno node, Object param);
	public Object visit(Cuerpo node, Object param);
	public Object visit(DefParametro node, Object param);
	public Object visit(IntType node, Object param);
	public Object visit(RealType node, Object param);
	public Object visit(CharType node, Object param);
	public Object visit(IdentType node, Object param);
	public Object visit(ArrayType node, Object param);
	public Object visit(DefEstructura node, Object param);
	public Object visit(DefCampo node, Object param);
	public Object visit(While node, Object param);
	public Object visit(If node, Object param);
	public Object visit(IfElse node, Object param);
	public Object visit(Return node, Object param);
	public Object visit(Print node, Object param);
	public Object visit(Println node, Object param);
	public Object visit(Printsp node, Object param);
	public Object visit(Read node, Object param);
	public Object visit(Asigna node, Object param);
	public Object visit(LlamadaFuncionSentencia node, Object param);
	public Object visit(ExpresionBinaria node, Object param);
	public Object visit(Invocacion node, Object param);
	public Object visit(Variable node, Object param);
	public Object visit(LiteralInt node, Object param);
	public Object visit(LiteralReal node, Object param);
	public Object visit(LiteralChar node, Object param);
	public Object visit(VarArray node, Object param);
	public Object visit(Cast node, Object param);
	public Object visit(Navega node, Object param);
	public Object visit(ConParentesis node, Object param);
	public Object visit(Negacion node, Object param);
}
