# Lenguaje-DLP

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/6f1eae5c3b7749a3ba299ae6c548e3a9)](https://app.codacy.com/app/ameliafb/InciManager_e3a?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Arquisoft/InciManager_e3a&amp;utm_campaign=Badge_Grade)
[![Build Status](https://travis-ci.org/Arquisoft/InciManager_e3a.svg?branch=master)](https://travis-ci.org/Arquisoft/InciManager_e3a)
[![codecov](https://codecov.io/gh/Arquisoft/InciManager_e3a/branch/master/graph/badge.svg)](https://codecov.io/gh/Arquisoft/InciManager_e3a)


En este repositorio se guarda el proyecto final de la asignatura de Diseño de Lenguajes de Programación en la Facultadad de [Ingeniería Informática del Software](https://ingenieriainformatica.uniovi.es).


## Índice
- [Introducción al proyecto](#lenguaje-dlp)
- [Características del lenguaje](#características-del-lenguaje)
- [Detalles Especificos](#detalles-especificos)
	- [Léxico](#léxico)
	- [Sintáctico](#sintáctico)
	- [Gramática Abstracta](#gramática-abstracta)
- [Autor](#autor)	

### Características del lenguaje

#### Aspectos léxicos

	• Deberá permitir constantes literales de tipo entero y real. Los literales de tipo real, además de una parte entera, deben tener obligatoriamente algún digito decimal.
	• Deberá permitir constantes de tipo carácter (se usarán comillas simples). Se debe admitir la secuencia de escape ‘\n’ para representar al carácter de salto de línea.
	• El lenguaje debe tener los comentarios de C/C++: /**/ y //.

   ##### Tipos simples o primitivos
   
	• Entero, float y char/byte (estos dos últimos son sinónimos; se debe elegir uno de los dos nombres)

. . .

### Detalles Especificos

En este apartado se muestran los diferentes ficheros que van complentado cada una de las fases que estamos implementando.

#### Léxico

      
      package sintactico;

      import java.io.*;
      import main.*;
      import ast.Position;

      %%
      %byaccj
      %unicode
      %line
      %column
      %public

      %{
        public Yylex(Reader in, GestorErrores gestor) {
          this(in);
          this.gestor = gestor;
        }

        public int line() { return yyline + 1; }
        public int column() { return yycolumn + 1; }
        public String lexeme() { return yytext(); }

        // Traza para probar el léxico de manera independiente al sintáctico
        public static void main(String[] args) throws Exception {
          Yylex lex = new Yylex(new FileReader(Main.programa), new GestorErrores());
          int token;
          while ((token = lex.yylex()) != 0)
            System.out.println("\t[" + lex.line() + ":" + lex.column() + "] Token: " + token + ". Lexema: " + lex.lexeme());
        }

        private GestorErrores gestor;
      %}

      %%

      int		{ return Parser.INT; }
      float	{ return Parser.REAL; }
      char	{ return Parser.CHARACTER; }
      struct 	{return Parser.STRUCT; }
      return	{ return Parser.RETURN; }
      read	{ return Parser.READ; }
      cast	{ return Parser.CAST; }

      print	{ return Parser.PRINT; }
      printsp	{ return Parser.PRINTSP; }
      println	{ return Parser.PRINTLN; }

      "&&"		{ return Parser.AND; }
      "||"		{ return Parser.OR; }
      ">="		{ return Parser.MAYORIGUAL; }
      "=="		{ return Parser.IGUAL; }
      "<="		{ return Parser.MENORIGUAL; }
      "!="		{ return Parser.DISTINTO; }

      var		{ return Parser.VAR; }
      if		{ return Parser.IF; }
      else	{ return Parser.ELSE; }
      while	{ return Parser.WHILE; }

      [0-9]+     				{ return Parser.LITERALINT; }
      [0-9]+\.[0-9]*			{ return Parser.LITERALREAL; }
      '(.|\\n)'				{ return Parser.LITERALCHAR; }

      [a-zA-ZñÑ][ñÑa-zA-Z0-9_]*	{ return Parser.IDENT; }

      [+\-*/;\()=:\[\]{}&<>!,\.]	{ return yytext().charAt(0); }


      "/*"([^*]|\*+[^*/])*\*+"/"		{ }		/* Comentario de varias lineas como este */
      "//".*							{ }		// Comentario de una linea como este

      [ \n\r]		{ }
      "\t"		{ yycolumn += 3; } // Para que coincida con la info del editor de Eclipse (opcional). En eclipse: \t == 4 caracteres. En Jflex: \t == 1 carácter.

      .			{ gestor.error("Léxico", "Cadena \"" + yytext() +"\" no reconocida.", new Position(line(), column())); }

      
#### Sintáctico

~~~
%{
package sintactico;

import java.io.*;
import java.util.*;
import ast.*;
import main.*;
%}

/* Precedencias aquí --------------------------------------- */
%left	'<' '>' 'IGUAL' 'DISTINTO' 'MENORIGUAL' 'MAYORIGUAL'  'OR' 'AND' '!'
%left '+' '-'
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


definicionVariable: 'VAR' 'IDENT' ':' tipo ';'	{ $$ = new DefVariable($2,$4); }
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


cuerpo: definicionVariablesFuncion sentencias	{ $$ = new Cuerpo($1, $2); }
	;


definicionVariablesFuncion:								{ $$ = new ArrayList(); }
	|	definicionVariablesFuncion	definicionVariable	{ $$ = $1; ((List)$1).add($2); }
	;
 
 
sentencias:				{ $$ = new ArrayList();}
	|	sentencias	sentencia	{ $$ = $1; ((List)$1).add($2); }
	;

	
sentencia:'WHILE' '(' expresion ')'  '{' sentencias '}'							{ $$ = new While($3, $6); }
	|	'IF' '(' expresion ')' '{' sentencias '}'								{ $$ = new If($3, $6); }
	|	'IF' '(' expresion ')' '{' sentencias '}' 'ELSE' '{' sentencias '}'		{ $$ = new IfElse($3, $6, $10); }
	|	'RETURN' ';'															{ $$ = new Return(null); }
	|	'RETURN' expresion ';'													{ $$ = new Return($2); }
	|	'READ'	expresion	';'													{ $$ = new Read($2); }
	|	'PRINT' expresion	';'													{ $$ = new Print($2); }
	|	'PRINTSP' expresion	';'													{ $$ = new Printsp($2); }
	|	'PRINTLN' expresion	';'													{ $$ = new Println($2); }
	|	'IDENT' '(' argumentosLlamadaFuncion ')'	';'							{ $$ = new LlamadaFuncionSentencia($1, $3);}
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
~~~

#### Gramática Abstracta

~~~
CATEGORIES
definicion, expresion, sentencia, tipo

NODES
programa -> definicion* ;

defVariable:definicion -> nombre:string tipo;
defFuncion:definicion -> nombre:string parametros:defParametro* retorno cuerpo;

retorno -> tipo ;
cuerpo -> defVariable* sentencia* ;
defParametro -> nombre:string tipo ;

intType:tipo -> ;
realType:tipo -> ;
charType:tipo -> ;
identType:tipo -> nombre:string;
arrayType:tipo -> dimension:literalInt tipo ;

defEstructura:definicion -> nombre:string defCampo* ;
defCampo -> nombre:string tipo ;

while:sentencia -> condicion:expresion cierto:sentencia* ;
if:sentencia -> condicion:expresion cierto:sentencia* ;
ifElse:sentencia -> condicion:expresion cierto:sentencia* falso:sentencia* ;
return:sentencia -> expresion ;
print:sentencia -> expresion ;
println:sentencia -> expresion ;
printsp:sentencia -> expresion ;
read:sentencia -> expresion ;
asigna:sentencia -> left:expresion right:expresion;
llamadaFuncionSentencia:sentencia -> nombre:string argumentos:expresion* ;

expresionBinaria:expresion -> left:expresion operador:string right:expresion;
invocacion:expresion -> nombre:string argumentos:expresion* ;
variable:expresion -> nombre:string;
literalInt:expresion -> valor:string;
literalReal:expresion -> valor:string;
literalChar:expresion -> valor:string;
varArray:expresion -> identificacion:expresion posicion:expresion ;
cast:expresion -> tipo expresion ;
navega:expresion -> expresion nombre:string ;
conParentesis:expresion -> expresion ;
negacion:expresion -> expresion ;
~~~


### Autor

Saúl Castillo Valdés
