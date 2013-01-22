package parser;

// File:   Logic_Parser.java
// Author: John Longley
// Date:   November 2012

// Encodes the LL(1) parse table for the following grammar:

/*
Fm       -> Fm1 ImplOps
ImplOps  -> epsilon | -> Fm1 ImplOps | <-> Fm1
Fm1      -> Fm2 AndOps
AndOps   -> epsilon | & Fm2 AndOps
Fm2      -> Tm = Tm | FALSE | LOGICVAR Args | ~ Fm2 | [ Fm ]
Args     -> epsilon | ( Tm Arglist )
Arglist  -> epsilon | , Tm Arglist
Tm       -> Tm1 PlusOps
PlusOps  -> epsilon | + Tm1 PlusOps
Tm1      -> Tm2 TimesOps
TimesOps -> epsilon | * Tm2 TimesOps
Tm2      -> NUM | MATCHVAR | TERMVAR Args | ( Tm )
*/

import java.io.* ;
 
class Logic_Parser extends GenParser implements PARSER {

    String startSymbol() {return "#Fm" ;} 

    String[] epsilon              = new String[] { } ;
    String[] Fm1_ImplOps          = new String[] {"#Fm1", "#ImplOps"} ;
    String[] impl_Fm1_ImplOps     = new String[] {"->", "#Fm1", "#ImplOps"} ;
    String[] iff_Fm1              = new String[] {"<->", "#Fm1"} ;
    String[] Fm2_AndOps           = new String[] {"#Fm2", "#AndOps"} ;
    String[] and_Fm2_AndOps       = new String[] {"&", "#Fm2", "#AndOps"} ;
    String[] FALSE                = new String[] {"FALSE"} ;
    String[] Tm_eq_Tm             = new String[] {"#Tm", "=", "#Tm"} ;
    String[] LOGICVAR_Args        = new String[] {"LOGICVAR", "#Args"} ;
    String[] neg_Fm2              = new String[] {"~", "#Fm2"} ;
    String[] lsqbr_Fm_rsqbr       = new String[] {"[", "#Fm", "]"} ;
    String[] lbr_Tm_Arglist_rbr   = new String[] {"(", "#Tm", "#Arglist", ")"};
    String[] comma_Tm_Arglist     = new String[] {",", "#Tm", "#Arglist"} ;
    String[] Tm1_PlusOps          = new String[] {"#Tm1", "#PlusOps"} ;
    String[] plus_Tm1_PlusOps     = new String[] {"+", "#Tm1", "#PlusOps"} ;
    String[] Tm2_TimesOps         = new String[] {"#Tm2", "#TimesOps"} ;
    String[] times_Tm2_TimesOps   = new String[] {"*", "#Tm2", "#TimesOps"} ;
    String[] NUM                  = new String[] {"NUM"} ;
    String[] MATCHVAR             = new String[] {"MATCHVAR"} ;
    String[] TERMVAR_Args         = new String[] {"TERMVAR", "#Args"} ;
    String[] lbr_Tm_rbr           = new String[] {"(", "#Tm", ")"} ;

    boolean Tm_start (String s) {
	return (s.equals("NUM") || s.equals("MATCHVAR") ||
		s.equals("TERMVAR") || s.equals("(")) ;
    }

    boolean Fm_start (String s) {
	return (Tm_start(s) || s.equals("FALSE") || 
		s.equals("LOGICVAR") || s.equals("~") || s.equals("[")) ;
    }

    String[] tableEntry (String nonterm, String tokClass) {
	if (tokClass == null) {
	    if (nonterm.equals("#ImplOps") ||
		nonterm.equals("#AndOps")  ||
		nonterm.equals("#Args")    ||
		nonterm.equals("#PlusOps") ||
		nonterm.equals("#TimesOps")) 
		return epsilon ;
	    else return null ;
	}
        else if (nonterm.equals("#Fm")) {
	    if (Fm_start(tokClass)) return Fm1_ImplOps ;
	    else return null ;
	}
	else if (nonterm.equals("#ImplOps")) {
	    if (tokClass.equals("]")) return epsilon ;
	    else if (tokClass.equals("->")) return impl_Fm1_ImplOps ;
	    else if (tokClass.equals("<->")) return iff_Fm1 ;
	    else return null ;
	}
        else if (nonterm.equals("#Fm1")) {
	    if (Fm_start(tokClass)) return Fm2_AndOps ;
	    else return null ;
	}
	else if (nonterm.equals("#AndOps")) {
	    if (tokClass.equals("]") || 
		tokClass.equals("->") || 
		tokClass.equals("<->")) 
		return epsilon ;
	    else if (tokClass.equals("&")) return and_Fm2_AndOps ;
	    else return null ;
	}
	else if (nonterm.equals("#Fm2")) {
	    if (Tm_start(tokClass)) return Tm_eq_Tm ;
	    else if (tokClass.equals("FALSE")) return FALSE ;
	    else if (tokClass.equals("LOGICVAR")) return LOGICVAR_Args ;
	    else if (tokClass.equals("~")) return neg_Fm2 ;
	    else if (tokClass.equals("[")) return lsqbr_Fm_rsqbr ;
	    else return null ;
	}
	else if (nonterm.equals("#Args")) {
	    if (tokClass.equals("(")) return lbr_Tm_Arglist_rbr ;
	    else if (tokClass.equals(")") ||
		     tokClass.equals(",") ||
		     tokClass.equals("]") ||
		     tokClass.equals("&") ||
		     tokClass.equals("->") ||
		     tokClass.equals("<->") ||
		     tokClass.equals("=") ||
		     tokClass.equals("+") ||
		     tokClass.equals("*"))
		return epsilon ;
	    else return null ;
	}
	else if (nonterm.equals("#Arglist")) {
	    if (tokClass.equals(")")) return epsilon ;
	    else if (tokClass.equals(",")) return comma_Tm_Arglist ;
	    else return null ;
	}
	else if (nonterm.equals("#Tm")) {
	    if (Tm_start(tokClass)) return Tm1_PlusOps ;
	    else return null ;
	}
	else if (nonterm.equals("#PlusOps")) {
	    if (tokClass.equals(")") ||
		tokClass.equals(",") ||
		tokClass.equals("&") ||
		tokClass.equals("->") ||
		tokClass.equals("<->") ||
		tokClass.equals("="))
		return epsilon ;
	    else if (tokClass.equals("+")) return plus_Tm1_PlusOps ;
	    else return null ;
	}
	else if (nonterm.equals("#Tm1")) {
	    if (Tm_start(tokClass)) return Tm2_TimesOps ;
	    else return null ;
	}
	else if (nonterm.equals("#TimesOps")) {
	    if (tokClass.equals(")") ||
		tokClass.equals(",") ||
		tokClass.equals("]") ||
		tokClass.equals("&") ||
		tokClass.equals("->") ||
		tokClass.equals("<->") ||
		tokClass.equals("=") ||
		tokClass.equals("+"))
		return epsilon ;
	    else if (tokClass.equals("*")) return times_Tm2_TimesOps ;
	    else return null ;
	}
	else if (nonterm.equals("#Tm2")) {
	    if (tokClass.equals("NUM")) return NUM ;
	    else if (tokClass.equals("MATCHVAR")) return MATCHVAR ;
	    else if (tokClass.equals("TERMVAR")) return TERMVAR_Args ;
	    else if (tokClass.equals("(")) return lbr_Tm_rbr ;
	    else return null ;
	}
	else return null ;
    }
}


// For testing

class Logic_ParserDemo {

    static PARSER Logic_Parser = new Logic_Parser() ;

    public static void main (String[] args) throws Exception {
	Reader reader = new BufferedReader (new FileReader (args[0])) ;
	LEX_TOKEN_STREAM Logic_Lexer = 
	    new CheckedSymbolLexer (new Logic_Lexer (reader)) ;
	@SuppressWarnings("unused")
	TREE theTree = Logic_Parser.parseTokenStream (Logic_Lexer) ;
    }
}
