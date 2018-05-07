// @author Raúl Izquierdo

/* No es necesario modificar esta sección ------------------ */
%{
package sintactico;

import java.io.*;
import java.util.*;
import ast.*;
import main.*;
%}

/* Precedencias aquí --------------------------------------- */
%left 'OR'
%left 'AND' 
%left 'IGUAL' 'DISTINTO'
%left	'<' '>'  'MENORIGUAL' 'MAYORIGUAL' 
%left '+' '-' '!'
%left '*' '/'
%left '[' ']' '.' ':' ',' ';'
%nonassoc '(' ')'

%%

/* Añadir las reglas en esta sección ----------------------- */

programa:	definiciones	{ raiz = new Programa($1); }
	;

definiciones: 						{ $$ = new ArrayList(); }
	|	definiciones	definicion	{ $$ = $1; ((List)$1).add($2); }
	;

definicion: definicionVariable	{ $$ = $1; }
	|	definicionFuncion		{ $$ = $1; }
	|	definicionEstructura	{ $$ = $1; }
	;


definicionVariable: 'VAR' 'IDENT' ':' tipo ';'	{ $$ = new DefVariable($2,$4,true); }
	;
	
definicionVariableLocal: 'VAR' 'IDENT' ':' tipo ';'	{ $$ = new DefVariable($2,$4,false); }
	;


tipo: 'INT'							{ $$ = new IntType(); }
	|	'REAL'						{ $$ = new RealType(); }
	|	'CHARACTER'					{ $$ = new CharType(); }
	|	'IDENT'						{ $$ = new IdentType($1); }
	|	'[' 'LITERALINT' ']' tipo	{ $$ = new ArrayType(new LiteralInt($2), $4); }
	;
	
	
definicionEstructura: 'STRUCT' 'IDENT' '{' definicionCampos '}' ';'	{ $$ = new DefEstructura($2, $4); }
	;
	

definicionCampos: 							{ $$ = new ArrayList(); }
	|	definicionCampos	definicionCampo	{ $$ = $1; ((List)$1).add($2); }
	;

definicionCampo: 'IDENT' ':' tipo ';'	{ $$ = new DefCampo($1, $3); }
	;


definicionFuncion: 'IDENT' '(' parametrosFuncion')'  retorno '{' cuerpo '}'	{ $$ = new DefFuncion($1, $3, $5, $7); }
	;


retorno: 		{ $$ = new Retorno(null); }
	| ':' tipo	{ $$ = new Retorno($2); }
	;


parametrosFuncion:			{ $$ = new ArrayList(); }
	|	parametroFuncion	{ $$ = $1; }
	;


parametroFuncion: definicionparametroFuncion 				{$$ = new ArrayList(); ((List)$$).add($1);}
	|	 parametroFuncion ',' definicionparametroFuncion	{ $$ = $1; ((List)$1).add($3); }
	;
	
	
definicionparametroFuncion: 'IDENT' ':' tipo 	{ $$ = new DefParametro($1, $3); } 
	;


cuerpo: 
	|	definicionVariablesFuncion sentencias	{ $$ = new Cuerpo($1, $2); }
	;


definicionVariablesFuncion:	definicionVariableLocal			{ $$ = new ArrayList(); $$ = $1;  }
	|	definicionVariablesFuncion	definicionVariableLocal	{ $$ = $1; ((List)$1).add($2); }
	;
 
 
sentencias:	sentencia			{ $$ = new ArrayList(); $$ = $1;  }
	|	sentencias	sentencia	{ $$ = $1; ((List)$1).add($2); }
	;

	
sentencia:'WHILE' '(' expresion ')'  '{' sentencias '}'							{ $$ = new While($3, $6); }
	|	'IF' '(' expresion ')' '{' sentencias '}'								{ $$ = new If($3, $6); }
	|	'IF' '(' expresion ')' '{' sentencias '}' 'ELSE' '{' sentencias '}'		{ $$ = new IfElse($3, $6, $10); }
	|	'RETURN' ';'																{ $$ = new Return(null).setPositions($1); }
	|	'RETURN' 'NULL' ';'														{ $$ = new Return(null).setPositions($1); }
	|	'RETURN' expresion ';'													{ $$ = new Return($2); }
	|	'READ'	expresion	';'													{ $$ = new Read($2); }
	|	'PRINT' expresion	';'													{ $$ = new Print($2); }
	|	'PRINTSP' expresion	';'													{ $$ = new Printsp($2); }
	|	'PRINTLN' expresion	';'													{ $$ = new Println($2); }
	|	'PRINTLN'	';'															{ $$ = new Println(null).setPositions($1); }
	|	'IDENT' '(' argumentosLlamadaFuncion ')'	';'								{ $$ = new LlamadaFuncionSentencia($1, $3);}
	|	expresion	'=' expresion	';'											{ $$ = new Asigna($1, $3); }
	;


expresion:	'(' expresion ')'						{ $$ = $2; }
	|	expresion	'+' expresion					{ $$ = new ExpresionBinaria($1, "+", $3); }
	|	expresion	'-'	expresion					{ $$ = new ExpresionBinaria($1, "-", $3); }
	|	expresion	'*'	expresion					{ $$ = new ExpresionBinaria($1, "*", $3); }
	|	expresion	'/'	expresion					{ $$ = new ExpresionBinaria($1, "/", $3); }
	|	expresion	'>'	expresion					{ $$ = new ExpresionBinaria($1, ">", $3); }
	|	expresion	'<'	expresion					{ $$ = new ExpresionBinaria($1, "<", $3); }
	|	expresion	'IGUAL'	expresion				{ $$ = new ExpresionBinaria($1, "IGUAL", $3); }
	|	expresion	'AND'	expresion				{ $$ = new ExpresionBinaria($1, "AND", $3); }
	|	expresion	'OR'	expresion				{ $$ = new ExpresionBinaria($1, "OR", $3); }
	|	expresion	'MAYORIGUAL'	expresion		{ $$ = new ExpresionBinaria($1, "MAYORIGUAL", $3); }
	|	expresion	'DISTINTO'	expresion			{ $$ = new ExpresionBinaria($1, "DISTINTO", $3); }
	|	expresion	'MENORIGUAL'	expresion		{ $$ = new ExpresionBinaria($1, "MENORIGUAL", $3); }
	|	'CAST' '<' tipo '>' '(' expresion ')'		{ $$ = new Cast($3, $6); }
	|	'LITERALINT'								{ $$ = new LiteralInt($1); }
	|	'LITERALREAL'								{ $$ = new LiteralReal($1); }
	|	'LITERALCHAR'								{ $$ = new LiteralChar($1); }
	|	'IDENT'										{ $$ = new Variable($1); }
	| 	'IDENT' '(' argumentosLlamadaFuncion ')'	{ $$ = new Invocacion($1, $3); }
	|	expresion '['	expresion	']'				{ $$ = new VarArray($1, $3); }
	|	expresion	'.'	'IDENT'						{ $$ = new Navega($1, $3); }
	|	'!'	expresion								{ $$ = new Negacion($2); }
	;
	

argumentosLlamadaFuncion:			{ $$ = new ArrayList(); }
	|  argumentoLlamadaFuncion		{ $$ = $1; }
	;

argumentoLlamadaFuncion: expresion				{$$ = new ArrayList(); ((List)$$).add($1);}
	| argumentoLlamadaFuncion ',' expresion		{ $$ = $1; ((List)$1).add($3); }
	;

	



%%
/* No es necesario modificar esta sección ------------------ */

public Parser(Yylex lex, GestorErrores gestor, boolean debug) {
	this(debug);
	this.lex = lex;
	this.gestor = gestor;
}

// Métodos de acceso para el main -------------
public int parse() { return yyparse(); }
public AST getAST() { return raiz; }

// Funciones requeridas por Yacc --------------
void yyerror(String msg) {
	Token lastToken = (Token) yylval;
	gestor.error("Sintáctico", "Token = " + lastToken.getToken() + ", lexema = \"" + lastToken.getLexeme() + "\". " + msg, lastToken.getStart());
}

int yylex() {
	try {
		int token = lex.yylex();
		yylval = new Token(token, lex.lexeme(), lex.line(), lex.column());
		return token;
	} catch (IOException e) {
		return -1;
	}
}

private Yylex lex;
private GestorErrores gestor;
private AST raiz;
