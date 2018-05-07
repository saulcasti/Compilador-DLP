// @author Raúl Izquierdo

/* -- No es necesario modificar esta parte ----------------------------------------------- */
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

/* -- Modificar aquí. Añadir reglas en esta sección ----------------------------------------- */



int		{ return Parser.INT; }
float	{ return Parser.REAL; }
real	{ return Parser.REAL; }
char		{ return Parser.CHARACTER; }
struct 	{return Parser.STRUCT; }
return	{ return Parser.RETURN; }
read		{ return Parser.READ; }
cast		{ return Parser.CAST; }

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

null		{return Parser.NULL; }



\'(.|\\n)\'				{ return Parser.LITERALCHAR; }
[0-9]+     				{ return Parser.LITERALINT; }
[0-9]+\.[0-9]*			{ return Parser.LITERALREAL; }


[a-zA-ZñÑ][ñÑa-zA-Z0-9_]*	{ return Parser.IDENT; }

[+\-*/;\()=:\[\]{}&<>!,\.]	{ return yytext().charAt(0); }


"/*"([^*]|\*+[^*/])*\*+"/"		{ }		/* Comentario de varias lineas como este */
"//".*							{ }		// Comentario de una linea como este

[ \n\r]		{ }
"\t"		{ yycolumn += 3; } // Para que coincida con la info del editor de Eclipse (opcional). En eclipse: \t == 4 caracteres. En Jflex: \t == 1 carácter.

.			{ gestor.error("Léxico", "Cadena \"" + yytext() +"\" no reconocida.", new Position(line(), column())); }
