//### This file created by BYACC 1.8(/Java extension  1.14)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 5 "sintac.y"
package sintactico;

import java.io.*;
import java.util.*;
import ast.*;
import main.*;
//#line 24 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//## **user defined:Object
String   yytext;//user variable to return contextual strings
Object yyval; //used to return semantic vals from action routines
Object yylval;//the 'lval' (result) I got from yylex()
Object valstk[] = new Object[YYSTACKSIZE];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
final void val_init()
{
  yyval=new Object();
  yylval=new Object();
  valptr=-1;
}
final void val_push(Object val)
{
  try {
    valptr++;
    valstk[valptr]=val;
  }
  catch (ArrayIndexOutOfBoundsException e) {
    int oldsize = valstk.length;
    int newsize = oldsize*2;
    Object[] newstack = new Object[newsize];
    System.arraycopy(valstk,0,newstack,0,oldsize);
    valstk = newstack;
    valstk[valptr]=val;
  }
}
final Object val_pop()
{
  return valstk[valptr--];
}
final void val_drop(int cnt)
{
  valptr -= cnt;
}
final Object val_peek(int relative)
{
  return valstk[valptr-relative];
}
//#### end semantic value section ####
public final static short IGUAL=257;
public final static short DISTINTO=258;
public final static short MENORIGUAL=259;
public final static short MAYORIGUAL=260;
public final static short OR=261;
public final static short AND=262;
public final static short VAR=263;
public final static short IDENT=264;
public final static short INT=265;
public final static short REAL=266;
public final static short CHARACTER=267;
public final static short LITERALINT=268;
public final static short STRUCT=269;
public final static short WHILE=270;
public final static short IF=271;
public final static short ELSE=272;
public final static short RETURN=273;
public final static short READ=274;
public final static short PRINT=275;
public final static short PRINTSP=276;
public final static short PRINTLN=277;
public final static short CAST=278;
public final static short LITERALREAL=279;
public final static short LITERALCHAR=280;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    2,    2,    2,    3,    6,    6,    6,
    6,    6,    5,    7,    7,    8,    4,   10,   10,    9,
    9,   12,   12,   13,   11,   14,   14,   15,   15,   16,
   16,   16,   16,   16,   16,   16,   16,   16,   16,   16,
   16,   17,   17,   17,   17,   17,   17,   17,   17,   17,
   17,   17,   17,   17,   17,   17,   17,   17,   17,   17,
   17,   17,   17,   18,   18,   19,   19,
};
final static short yylen[] = {                            2,
    1,    0,    2,    1,    1,    1,    5,    1,    1,    1,
    1,    4,    6,    0,    2,    4,    8,    0,    2,    0,
    1,    1,    3,    3,    2,    0,    2,    0,    2,    7,
    7,   11,    2,    3,    3,    3,    3,    3,    2,    5,
    4,    3,    3,    3,    3,    3,    3,    3,    3,    3,
    3,    3,    3,    3,    7,    1,    1,    1,    1,    4,
    4,    3,    2,    0,    1,    1,    3,
};
final static short yydefred[] = {                         2,
    0,    0,    0,    0,    0,    3,    4,    5,    6,    0,
    0,    0,    0,    0,    0,    0,   22,   14,    0,   11,
    8,    9,   10,    0,    0,    0,    0,    0,    0,    7,
   24,    0,    0,   23,    0,    0,   15,    0,   19,   26,
    0,   13,   12,    0,    0,    0,   17,   27,    0,   16,
    0,    0,    0,   56,    0,    0,    0,    0,    0,    0,
    0,    0,   57,   58,   29,    0,    0,    0,    0,    0,
    0,    0,   33,    0,    0,    0,    0,   39,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   42,    0,    0,    0,
    0,    0,   34,   35,   36,   37,   38,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   62,    0,    0,    0,    0,    0,    0,    0,   61,
   41,   60,   40,    0,   28,   28,    0,    0,    0,    0,
   30,    0,   55,    0,   28,    0,   32,
};
final static short yydgoto[] = {                          1,
    2,    6,    7,    8,    9,   24,   28,   37,   15,   33,
   44,   16,   17,   45,   49,   65,   66,   99,  100,
};
final static short yysindex[] = {                         0,
    0, -221, -239,  -14, -225,    0,    0,    0,    0,  -13,
 -220,  -73,  -59,    2,   23,   22,    0,    0, -201,    0,
    0,    0,    0,   11,  -59,   13, -220, -124,  -24,    0,
    0,  -59,  -51,    0,   15,   17,    0,  -59,    0,    0,
  -59,    0,    0,  -48, -189,   19,    0,    0,   53,    0,
   78,   78,   39,    0,   41,   42,   70,   78,   78,   78,
   73,   24,    0,    0,    0,  498,   43,   10,  110,   78,
   78,   78,    0,  510,  557,  702,  732,    0,  738,  -59,
   78,   78,   78,   78,   78,   78,   78,   78,   78,   78,
   78,   78,   78, -179,   78,   78,    0,  800,   46,   45,
  133,  142,    0,    0,    0,    0,    0,   28,   10,   10,
   10,   10,   10,   10,   10,   10,  -11,  -11,  -28,  -28,
  760,    0,  766,   50,   36,   78,  -29,  -27,   57,    0,
    0,    0,    0,  800,    0,    0,   78,  -33,  -16,  336,
    0, -174,    0,  -23,    0,    1,    0,
};
final static short yyrindex[] = {                         0,
    0,   99,    0,    0,    0,    0,    0,    0,    0,    0,
   61,    0,    0,    0,    0,   63,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -73,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   18,    0,    0,    0,  -20,    0,
    0,    0,  788,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -32,  376,    0,   66,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   66,    0,  -25,    0,   67,
    0,    0,    0,    0,    0,    0,    0,    0,  399,  405,
  411,  434,  440,  446,  469,  475,  364,  370,  -39,  103,
    0,    0,    0,    0,  794,    0,    0,    0,    0,    0,
    0,    0,    0,   21,    0,    0,    0,    0,    0,    0,
    0,   35,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,    0,   69,    0,    0,    8,    0,    0,    0,    0,
    0,    0,   85,    0,  -98,    0,  812,   20,    0,
};
final static int YYTABLESIZE=1062;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         51,
   36,   45,   45,   45,   45,   45,   52,   45,   59,   59,
   59,   59,   59,   59,   59,   66,   51,   94,   66,   45,
   45,   45,   45,   52,   10,   11,   59,   59,   59,   59,
   91,   19,   31,   51,   94,   92,  138,  139,   12,   39,
   52,    3,    4,   14,   13,   43,  146,    5,   46,   18,
   28,   91,   89,   45,   90,   94,   92,   28,   59,   25,
   59,   67,   93,   26,   67,   27,   29,   31,   38,   30,
   32,   40,   41,    3,   31,   42,   47,   50,   70,   93,
   71,   72,   96,   80,  122,   51,  125,  108,  126,  129,
  132,  141,   52,  135,  133,  136,  137,  144,    1,  145,
   93,   20,   51,   21,   25,   51,   64,   65,  142,   52,
   51,   34,   52,   48,    0,  124,    0,   52,    0,    0,
    0,    0,    0,    0,    0,  147,    0,    0,   73,    0,
    0,   78,    0,    0,    0,    0,    0,    0,    0,   35,
    0,    0,   28,   46,   46,   46,   46,   46,    0,   46,
   97,   91,   89,    0,   90,   94,   92,    0,    0,   31,
    0,   46,   46,   46,   46,    0,    0,    0,    0,   81,
    0,   82,    0,  127,   91,   89,    0,   90,   94,   92,
    0,    0,  128,   91,   89,    0,   90,   94,   92,    0,
    0,    0,   81,    0,   82,   46,    0,    0,    0,    0,
   93,   81,    0,   82,   20,   21,   22,   23,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   45,   45,   45,
   45,   45,   45,   93,   59,   59,   59,   59,   59,   59,
   53,    0,   93,    0,   54,    0,   55,   56,    0,   57,
   58,   59,   60,   61,   62,   63,   64,   53,    0,    0,
    0,   54,    0,   55,   56,    0,   57,   58,   59,   60,
   61,   62,   63,   64,   53,    0,    0,    0,   54,    0,
   55,   56,    0,   57,   58,   59,   60,   61,   62,   63,
   64,   28,    0,    0,    0,   28,    0,   28,   28,    0,
   28,   28,   28,   28,   28,   28,   28,   28,   31,    0,
    0,    0,   31,    0,   31,   31,    0,   31,   31,   31,
   31,   31,   31,   31,   31,    0,   53,    0,    0,    0,
   54,    0,   55,   56,    0,   57,   58,   59,   60,   61,
   62,   63,   64,   67,    0,    0,   67,   54,    0,    0,
   54,   67,    0,    0,    0,   54,    0,   62,   63,   64,
   62,   63,   64,    0,    0,   62,   63,   64,    0,   46,
   46,   46,   46,   46,   46,    0,   83,   84,   85,   86,
   87,   88,    0,    0,    0,    0,  143,   91,   89,    0,
   90,   94,   92,    0,    0,    0,    0,    0,    0,   83,
   84,   85,   86,   87,   88,   81,    0,   82,   83,   84,
   85,   86,   87,   88,   43,    0,   43,   43,   43,    0,
   44,    0,   44,   44,   44,    0,   63,    0,    0,   63,
    0,    0,   43,   43,   43,   43,   93,    0,   44,   44,
   44,   44,    0,    0,   63,   63,   63,   63,    0,   48,
    0,    0,   48,    0,    0,   47,    0,    0,   47,    0,
    0,   49,    0,    0,   49,    0,   43,   48,   48,   48,
   48,    0,   44,   47,   47,   47,   47,    0,   63,   49,
   49,   49,   49,    0,   53,    0,    0,   53,    0,    0,
   54,    0,    0,   54,    0,    0,   52,    0,    0,   52,
    0,   48,   53,   53,   53,   53,    0,   47,   54,   54,
   54,   54,    0,   49,   52,   52,   52,   52,    0,   51,
    0,    0,   51,    0,    0,   50,    0,    0,   50,    0,
    0,    0,    0,    0,    0,    0,   53,   51,   51,   51,
   51,    0,   54,   50,   50,   50,   50,    0,   52,   91,
   89,    0,   90,   94,   92,    0,    0,    0,    0,    0,
    0,   91,   89,    0,   90,   94,   92,   81,   95,   82,
    0,   51,    0,    0,    0,    0,    0,   50,  103,   81,
    0,   82,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   93,    0,
    0,    0,   83,   84,   85,   86,   87,   88,   91,   89,
   93,   90,   94,   92,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  104,   81,    0,   82,    0,
   43,   43,   43,   43,   43,   43,   44,   44,   44,   44,
   44,   44,   63,   63,   63,   63,   63,   63,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   93,    0,    0,
    0,    0,    0,    0,    0,   48,   48,   48,   48,   48,
   48,   47,   47,   47,   47,   47,   47,   49,   49,   49,
   49,   49,   49,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   53,   53,   53,   53,   53,   53,   54,   54,   54,   54,
   54,   54,   52,   52,   52,   52,   52,   52,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   51,   51,   51,   51,   51,
   51,   50,   50,   50,   50,   50,   50,    0,    0,    0,
    0,    0,    0,   91,   89,    0,   90,   94,   92,    0,
    0,    0,    0,    0,   83,   84,   85,   86,   87,   88,
  105,   81,    0,   82,    0,    0,   83,   84,   85,   86,
   87,   88,    0,   91,   89,    0,   90,   94,   92,   91,
   89,    0,   90,   94,   92,    0,    0,    0,    0,    0,
  106,   81,   93,   82,    0,    0,  107,   81,    0,   82,
    0,   91,   89,    0,   90,   94,   92,   91,   89,    0,
   90,   94,   92,   83,   84,   85,   86,   87,   88,   81,
    0,   82,   93,    0,  131,   81,    0,   82,   93,   59,
   59,    0,   59,   59,   59,   60,   60,    0,   60,   60,
   60,   91,   89,    0,   90,   94,   92,   59,   59,   59,
   93,    0,  130,   60,   60,   60,   93,    0,    0,   81,
    0,   82,   68,   69,    0,    0,    0,    0,   74,   75,
   76,   77,   79,    0,    0,    0,    0,    0,   59,    0,
    0,   98,  101,  102,   60,    0,    0,    0,    0,    0,
   93,    0,  109,  110,  111,  112,  113,  114,  115,  116,
  117,  118,  119,  120,  121,    0,  123,   98,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  134,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  140,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   83,   84,
   85,   86,   87,   88,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   83,   84,
   85,   86,   87,   88,   83,   84,   85,   86,   87,   88,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   83,   84,   85,   86,
   87,   88,   83,   84,   85,   86,   87,   88,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   59,   59,   59,   59,   59,   59,
   60,   60,   60,   60,   60,   60,   83,   84,   85,   86,
   87,   88,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         33,
  125,   41,   42,   43,   44,   45,   40,   47,   41,   42,
   43,   44,   45,   46,   47,   41,   33,   46,   44,   59,
   60,   61,   62,   40,  264,   40,   59,   60,   61,   62,
   42,   91,   25,   33,   46,   47,  135,  136,  264,   32,
   40,  263,  264,  264,   58,   38,  145,  269,   41,  123,
   33,   42,   43,   93,   45,   46,   47,   40,   91,   58,
   93,   41,   91,   41,   44,   44,  268,   33,   93,   59,
   58,  123,   58,  263,   40,   59,  125,   59,   40,   91,
   40,   40,   40,   60,  264,   33,   41,   80,   44,   62,
   41,  125,   40,  123,   59,  123,   40,  272,    0,  123,
   91,   41,   33,   41,  125,   33,   41,   41,  125,   40,
   33,   27,   40,   45,   -1,   96,   -1,   40,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  125,   -1,   -1,   59,   -1,
   -1,   59,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  264,
   -1,   -1,  125,   41,   42,   43,   44,   45,   -1,   47,
   41,   42,   43,   -1,   45,   46,   47,   -1,   -1,  125,
   -1,   59,   60,   61,   62,   -1,   -1,   -1,   -1,   60,
   -1,   62,   -1,   41,   42,   43,   -1,   45,   46,   47,
   -1,   -1,   41,   42,   43,   -1,   45,   46,   47,   -1,
   -1,   -1,   60,   -1,   62,   93,   -1,   -1,   -1,   -1,
   91,   60,   -1,   62,  264,  265,  266,  267,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  257,  258,  259,
  260,  261,  262,   91,  257,  258,  259,  260,  261,  262,
  264,   -1,   91,   -1,  268,   -1,  270,  271,   -1,  273,
  274,  275,  276,  277,  278,  279,  280,  264,   -1,   -1,
   -1,  268,   -1,  270,  271,   -1,  273,  274,  275,  276,
  277,  278,  279,  280,  264,   -1,   -1,   -1,  268,   -1,
  270,  271,   -1,  273,  274,  275,  276,  277,  278,  279,
  280,  264,   -1,   -1,   -1,  268,   -1,  270,  271,   -1,
  273,  274,  275,  276,  277,  278,  279,  280,  264,   -1,
   -1,   -1,  268,   -1,  270,  271,   -1,  273,  274,  275,
  276,  277,  278,  279,  280,   -1,  264,   -1,   -1,   -1,
  268,   -1,  270,  271,   -1,  273,  274,  275,  276,  277,
  278,  279,  280,  264,   -1,   -1,  264,  268,   -1,   -1,
  268,  264,   -1,   -1,   -1,  268,   -1,  278,  279,  280,
  278,  279,  280,   -1,   -1,  278,  279,  280,   -1,  257,
  258,  259,  260,  261,  262,   -1,  257,  258,  259,  260,
  261,  262,   -1,   -1,   -1,   -1,   41,   42,   43,   -1,
   45,   46,   47,   -1,   -1,   -1,   -1,   -1,   -1,  257,
  258,  259,  260,  261,  262,   60,   -1,   62,  257,  258,
  259,  260,  261,  262,   41,   -1,   43,   44,   45,   -1,
   41,   -1,   43,   44,   45,   -1,   41,   -1,   -1,   44,
   -1,   -1,   59,   60,   61,   62,   91,   -1,   59,   60,
   61,   62,   -1,   -1,   59,   60,   61,   62,   -1,   41,
   -1,   -1,   44,   -1,   -1,   41,   -1,   -1,   44,   -1,
   -1,   41,   -1,   -1,   44,   -1,   93,   59,   60,   61,
   62,   -1,   93,   59,   60,   61,   62,   -1,   93,   59,
   60,   61,   62,   -1,   41,   -1,   -1,   44,   -1,   -1,
   41,   -1,   -1,   44,   -1,   -1,   41,   -1,   -1,   44,
   -1,   93,   59,   60,   61,   62,   -1,   93,   59,   60,
   61,   62,   -1,   93,   59,   60,   61,   62,   -1,   41,
   -1,   -1,   44,   -1,   -1,   41,   -1,   -1,   44,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   93,   59,   60,   61,
   62,   -1,   93,   59,   60,   61,   62,   -1,   93,   42,
   43,   -1,   45,   46,   47,   -1,   -1,   -1,   -1,   -1,
   -1,   42,   43,   -1,   45,   46,   47,   60,   61,   62,
   -1,   93,   -1,   -1,   -1,   -1,   -1,   93,   59,   60,
   -1,   62,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   91,   -1,
   -1,   -1,  257,  258,  259,  260,  261,  262,   42,   43,
   91,   45,   46,   47,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   59,   60,   -1,   62,   -1,
  257,  258,  259,  260,  261,  262,  257,  258,  259,  260,
  261,  262,  257,  258,  259,  260,  261,  262,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   91,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  257,  258,  259,  260,  261,
  262,  257,  258,  259,  260,  261,  262,  257,  258,  259,
  260,  261,  262,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  257,  258,  259,  260,  261,  262,  257,  258,  259,  260,
  261,  262,  257,  258,  259,  260,  261,  262,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  257,  258,  259,  260,  261,
  262,  257,  258,  259,  260,  261,  262,   -1,   -1,   -1,
   -1,   -1,   -1,   42,   43,   -1,   45,   46,   47,   -1,
   -1,   -1,   -1,   -1,  257,  258,  259,  260,  261,  262,
   59,   60,   -1,   62,   -1,   -1,  257,  258,  259,  260,
  261,  262,   -1,   42,   43,   -1,   45,   46,   47,   42,
   43,   -1,   45,   46,   47,   -1,   -1,   -1,   -1,   -1,
   59,   60,   91,   62,   -1,   -1,   59,   60,   -1,   62,
   -1,   42,   43,   -1,   45,   46,   47,   42,   43,   -1,
   45,   46,   47,  257,  258,  259,  260,  261,  262,   60,
   -1,   62,   91,   -1,   59,   60,   -1,   62,   91,   42,
   43,   -1,   45,   46,   47,   42,   43,   -1,   45,   46,
   47,   42,   43,   -1,   45,   46,   47,   60,   61,   62,
   91,   -1,   93,   60,   61,   62,   91,   -1,   -1,   60,
   -1,   62,   51,   52,   -1,   -1,   -1,   -1,   57,   58,
   59,   60,   61,   -1,   -1,   -1,   -1,   -1,   91,   -1,
   -1,   70,   71,   72,   91,   -1,   -1,   -1,   -1,   -1,
   91,   -1,   81,   82,   83,   84,   85,   86,   87,   88,
   89,   90,   91,   92,   93,   -1,   95,   96,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  126,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  137,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  257,  258,
  259,  260,  261,  262,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  257,  258,
  259,  260,  261,  262,  257,  258,  259,  260,  261,  262,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  257,  258,  259,  260,
  261,  262,  257,  258,  259,  260,  261,  262,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  257,  258,  259,  260,  261,  262,
  257,  258,  259,  260,  261,  262,  257,  258,  259,  260,
  261,  262,
};
}
final static short YYFINAL=1;
final static short YYMAXTOKEN=280;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,"'!'",null,null,null,null,null,null,"'('","')'","'*'","'+'",
"','","'-'","'.'","'/'",null,null,null,null,null,null,null,null,null,null,"':'",
"';'","'<'","'='","'>'",null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,"'['",null,"']'",null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,"\"IGUAL\"","\"DISTINTO\"",
"\"MENORIGUAL\"","\"MAYORIGUAL\"","\"OR\"","\"AND\"","\"VAR\"","\"IDENT\"",
"\"INT\"","\"REAL\"","\"CHARACTER\"","\"LITERALINT\"","\"STRUCT\"","\"WHILE\"",
"\"IF\"","\"ELSE\"","\"RETURN\"","\"READ\"","\"PRINT\"","\"PRINTSP\"",
"\"PRINTLN\"","\"CAST\"","\"LITERALREAL\"","\"LITERALCHAR\"",
};
final static String yyrule[] = {
"$accept : programa",
"programa : definiciones",
"definiciones :",
"definiciones : definiciones definicion",
"definicion : definicionVariable",
"definicion : definicionFuncion",
"definicion : definicionEstructura",
"definicionVariable : \"VAR\" \"IDENT\" ':' tipo ';'",
"tipo : \"INT\"",
"tipo : \"REAL\"",
"tipo : \"CHARACTER\"",
"tipo : \"IDENT\"",
"tipo : '[' \"LITERALINT\" ']' tipo",
"definicionEstructura : \"STRUCT\" \"IDENT\" '{' definicionCampos '}' ';'",
"definicionCampos :",
"definicionCampos : definicionCampos definicionCampo",
"definicionCampo : \"IDENT\" ':' tipo ';'",
"definicionFuncion : \"IDENT\" '(' parametrosFuncion ')' retorno '{' cuerpo '}'",
"retorno :",
"retorno : ':' tipo",
"parametrosFuncion :",
"parametrosFuncion : parametroFuncion",
"parametroFuncion : definicionparametroFuncion",
"parametroFuncion : parametroFuncion ',' definicionparametroFuncion",
"definicionparametroFuncion : \"IDENT\" ':' tipo",
"cuerpo : definicionVariablesFuncion sentencias",
"definicionVariablesFuncion :",
"definicionVariablesFuncion : definicionVariablesFuncion definicionVariable",
"sentencias :",
"sentencias : sentencias sentencia",
"sentencia : \"WHILE\" '(' expresion ')' '{' sentencias '}'",
"sentencia : \"IF\" '(' expresion ')' '{' sentencias '}'",
"sentencia : \"IF\" '(' expresion ')' '{' sentencias '}' \"ELSE\" '{' sentencias '}'",
"sentencia : \"RETURN\" ';'",
"sentencia : \"RETURN\" expresion ';'",
"sentencia : \"READ\" expresion ';'",
"sentencia : \"PRINT\" expresion ';'",
"sentencia : \"PRINTSP\" expresion ';'",
"sentencia : \"PRINTLN\" expresion ';'",
"sentencia : \"PRINTLN\" ';'",
"sentencia : \"IDENT\" '(' argumentosLlamadaFuncion ')' ';'",
"sentencia : expresion '=' expresion ';'",
"expresion : '(' expresion ')'",
"expresion : expresion '+' expresion",
"expresion : expresion '-' expresion",
"expresion : expresion '*' expresion",
"expresion : expresion '/' expresion",
"expresion : expresion '>' expresion",
"expresion : expresion '<' expresion",
"expresion : expresion \"IGUAL\" expresion",
"expresion : expresion \"AND\" expresion",
"expresion : expresion \"OR\" expresion",
"expresion : expresion \"MAYORIGUAL\" expresion",
"expresion : expresion \"DISTINTO\" expresion",
"expresion : expresion \"MENORIGUAL\" expresion",
"expresion : \"CAST\" '<' tipo '>' '(' expresion ')'",
"expresion : \"LITERALINT\"",
"expresion : \"LITERALREAL\"",
"expresion : \"LITERALCHAR\"",
"expresion : \"IDENT\"",
"expresion : \"IDENT\" '(' argumentosLlamadaFuncion ')'",
"expresion : expresion '[' expresion ']'",
"expresion : expresion '.' \"IDENT\"",
"expresion : '!' expresion",
"argumentosLlamadaFuncion :",
"argumentosLlamadaFuncion : argumentoLlamadaFuncion",
"argumentoLlamadaFuncion : expresion",
"argumentoLlamadaFuncion : argumentoLlamadaFuncion ',' expresion",
};

//#line 151 "sintac.y"
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
//#line 550 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 24 "sintac.y"
{ raiz = new Programa(val_peek(0)); }
break;
case 2:
//#line 27 "sintac.y"
{ yyval = new ArrayList(); }
break;
case 3:
//#line 28 "sintac.y"
{ yyval = val_peek(1); ((List)val_peek(1)).add(val_peek(0)); }
break;
case 4:
//#line 31 "sintac.y"
{ yyval = val_peek(0); }
break;
case 5:
//#line 32 "sintac.y"
{ yyval = val_peek(0); }
break;
case 6:
//#line 33 "sintac.y"
{ yyval = val_peek(0); }
break;
case 7:
//#line 37 "sintac.y"
{ yyval = new DefVariable(val_peek(3),val_peek(1)); }
break;
case 8:
//#line 41 "sintac.y"
{ yyval = new IntType(); }
break;
case 9:
//#line 42 "sintac.y"
{ yyval = new RealType(); }
break;
case 10:
//#line 43 "sintac.y"
{ yyval = new CharType(); }
break;
case 11:
//#line 44 "sintac.y"
{ yyval = new IdentType(val_peek(0)); }
break;
case 12:
//#line 45 "sintac.y"
{ yyval = new ArrayType(new LiteralInt(val_peek(2)), val_peek(0)); }
break;
case 13:
//#line 49 "sintac.y"
{ yyval = new DefEstructura(val_peek(4), val_peek(2)); }
break;
case 14:
//#line 53 "sintac.y"
{ yyval = new ArrayList(); }
break;
case 15:
//#line 54 "sintac.y"
{ yyval = val_peek(1); ((List)val_peek(1)).add(val_peek(0)); }
break;
case 16:
//#line 57 "sintac.y"
{ yyval = new DefCampo(val_peek(3), val_peek(1)); }
break;
case 17:
//#line 61 "sintac.y"
{ yyval = new DefFuncion(val_peek(7), val_peek(5), val_peek(3), val_peek(1)); }
break;
case 18:
//#line 65 "sintac.y"
{ yyval = new Retorno(null); }
break;
case 19:
//#line 66 "sintac.y"
{ yyval = new Retorno(val_peek(0)); }
break;
case 20:
//#line 70 "sintac.y"
{ yyval = new ArrayList(); }
break;
case 21:
//#line 71 "sintac.y"
{ yyval = val_peek(0); }
break;
case 22:
//#line 75 "sintac.y"
{yyval = new ArrayList(); ((List)yyval).add(val_peek(0));}
break;
case 23:
//#line 76 "sintac.y"
{ yyval = val_peek(2); ((List)val_peek(2)).add(val_peek(0)); }
break;
case 24:
//#line 80 "sintac.y"
{ yyval = new DefParametro(val_peek(2), val_peek(0)); }
break;
case 25:
//#line 84 "sintac.y"
{ yyval = new Cuerpo(val_peek(1), val_peek(0)); }
break;
case 26:
//#line 88 "sintac.y"
{ yyval = new ArrayList(); }
break;
case 27:
//#line 89 "sintac.y"
{ yyval = val_peek(1); ((List)val_peek(1)).add(val_peek(0)); }
break;
case 28:
//#line 93 "sintac.y"
{ yyval = new ArrayList();}
break;
case 29:
//#line 94 "sintac.y"
{ yyval = val_peek(1); ((List)val_peek(1)).add(val_peek(0)); }
break;
case 30:
//#line 98 "sintac.y"
{ yyval = new While(val_peek(4), val_peek(1)); }
break;
case 31:
//#line 99 "sintac.y"
{ yyval = new If(val_peek(4), val_peek(1)); }
break;
case 32:
//#line 100 "sintac.y"
{ yyval = new IfElse(val_peek(8), val_peek(5), val_peek(1)); }
break;
case 33:
//#line 101 "sintac.y"
{ yyval = new Return(null).setPositions(val_peek(1)); }
break;
case 34:
//#line 102 "sintac.y"
{ yyval = new Return(val_peek(1)); }
break;
case 35:
//#line 103 "sintac.y"
{ yyval = new Read(val_peek(1)); }
break;
case 36:
//#line 104 "sintac.y"
{ yyval = new Print(val_peek(1)); }
break;
case 37:
//#line 105 "sintac.y"
{ yyval = new Printsp(val_peek(1)); }
break;
case 38:
//#line 106 "sintac.y"
{ yyval = new Println(val_peek(1)); }
break;
case 39:
//#line 107 "sintac.y"
{ yyval = new Println(null).setPositions(val_peek(1)); }
break;
case 40:
//#line 108 "sintac.y"
{ yyval = new LlamadaFuncionSentencia(val_peek(4), val_peek(2));}
break;
case 41:
//#line 109 "sintac.y"
{ yyval = new Asigna(val_peek(3), val_peek(1)); }
break;
case 42:
//#line 113 "sintac.y"
{ yyval = val_peek(1); }
break;
case 43:
//#line 114 "sintac.y"
{ yyval = new ExpresionBinaria(val_peek(2), "+", val_peek(0)); }
break;
case 44:
//#line 115 "sintac.y"
{ yyval = new ExpresionBinaria(val_peek(2), "-", val_peek(0)); }
break;
case 45:
//#line 116 "sintac.y"
{ yyval = new ExpresionBinaria(val_peek(2), "*", val_peek(0)); }
break;
case 46:
//#line 117 "sintac.y"
{ yyval = new ExpresionBinaria(val_peek(2), "/", val_peek(0)); }
break;
case 47:
//#line 118 "sintac.y"
{ yyval = new ExpresionBinaria(val_peek(2), ">", val_peek(0)); }
break;
case 48:
//#line 119 "sintac.y"
{ yyval = new ExpresionBinaria(val_peek(2), "<", val_peek(0)); }
break;
case 49:
//#line 120 "sintac.y"
{ yyval = new ExpresionBinaria(val_peek(2), "IGUAL", val_peek(0)); }
break;
case 50:
//#line 121 "sintac.y"
{ yyval = new ExpresionBinaria(val_peek(2), "AND", val_peek(0)); }
break;
case 51:
//#line 122 "sintac.y"
{ yyval = new ExpresionBinaria(val_peek(2), "OR", val_peek(0)); }
break;
case 52:
//#line 123 "sintac.y"
{ yyval = new ExpresionBinaria(val_peek(2), "MAYORIGUAL", val_peek(0)); }
break;
case 53:
//#line 124 "sintac.y"
{ yyval = new ExpresionBinaria(val_peek(2), "DISTINTO", val_peek(0)); }
break;
case 54:
//#line 125 "sintac.y"
{ yyval = new ExpresionBinaria(val_peek(2), "MENORIGUAL", val_peek(0)); }
break;
case 55:
//#line 126 "sintac.y"
{ yyval = new Cast(val_peek(4), val_peek(1)); }
break;
case 56:
//#line 127 "sintac.y"
{ yyval = new LiteralInt(val_peek(0)); }
break;
case 57:
//#line 128 "sintac.y"
{ yyval = new LiteralReal(val_peek(0)); }
break;
case 58:
//#line 129 "sintac.y"
{ yyval = new LiteralChar(val_peek(0)); }
break;
case 59:
//#line 130 "sintac.y"
{ yyval = new Variable(val_peek(0)); }
break;
case 60:
//#line 131 "sintac.y"
{ yyval = new Invocacion(val_peek(3), val_peek(1)); }
break;
case 61:
//#line 132 "sintac.y"
{ yyval = new VarArray(val_peek(3), val_peek(1)); }
break;
case 62:
//#line 133 "sintac.y"
{ yyval = new Navega(val_peek(2), val_peek(0)); }
break;
case 63:
//#line 134 "sintac.y"
{ yyval = new Negacion(val_peek(0)); }
break;
case 64:
//#line 138 "sintac.y"
{ yyval = new ArrayList(); }
break;
case 65:
//#line 139 "sintac.y"
{ yyval = val_peek(0); }
break;
case 66:
//#line 142 "sintac.y"
{yyval = new ArrayList(); ((List)yyval).add(val_peek(0));}
break;
case 67:
//#line 143 "sintac.y"
{ yyval = val_peek(2); ((List)val_peek(2)).add(val_peek(0)); }
break;
//#line 966 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
