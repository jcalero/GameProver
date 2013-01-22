package parser;
import java.io.BufferedReader;
import java.io.Reader;
import java.io.StringReader;

import logic.Expression;

public class MyExpressionParser {

    static PARSER Logic_Parser = new Logic_Parser() ;

    static TREE parseStream(String input) throws Exception {
	// Nasty trial-and-error parsing - first as formula, then as term.
	// Really there should be two parse methods, and client code should 
	// call the right one in each instance.
	try {
	    Reader reader = new BufferedReader (new StringReader (input)) ;
	    LEX_TOKEN_STREAM lx = new CheckedSymbolLexer (new Logic_Lexer (reader)) ;
	    return Logic_Parser.parseTokenStreamAs (lx, "#Fm") ;
	} catch (Exception x) {
	    Reader reader = new BufferedReader (new StringReader (input)) ;
	    LEX_TOKEN_STREAM lx = new CheckedSymbolLexer (new Logic_Lexer (reader)) ;
	    return Logic_Parser.parseTokenStreamAs (lx, "#Tm") ;
	}
    }

    public static Expression parse(String input) throws Exception {
	System.out.println ("Parsing: " + input) ;
	TREE t = parseStream(input) ; 
	System.out.println ("Tree root: " + t.getLabel()) ;
	Expression e = walkTree(t);
	System.out.println ("Expression: " + toString(e)) ;
	return e;
    }

    static boolean isEmpty (TREE t) {
	return (t.getRhs().length == 0) ;
    }

	public static Expression walkTree (TREE t) {
	    if (t.getLabel() == "LOGICVAR" ||
		t.getLabel() == "TERMVAR" ||
		t.getLabel() == "MATCHVAR" ||
		t.getLabel() == "NUM" ||
		t.getLabel() == "FALSE")
		return new Expression (t.getValue()) ;
	    else if (t.getLabel() == "#Tm2") {
		if (t.getRhs()[0] == "(")
		    return walkTree (t.getChildren()[1]) ;
		else return walkTree (t.getChildren()[0]) ;
		// TERMVAR arguments ignored for now
	    } else if (t.getLabel() == "#Tm1") {
		if (isEmpty (t.getChildren()[1]))
		    return walkTree (t.getChildren()[0]) ;
		else return new Expression 
		    (walkTree (t.getChildren()[0]),
		     "*",
		     walkTree (t.getChildren()[1])) ;
	    } else if (t.getLabel() == "#TimesOps") {
		// can assume non-empty
		if (isEmpty (t.getChildren()[2]))
		    return walkTree (t.getChildren()[1]) ;
		else return new Expression 
		    (walkTree (t.getChildren()[1]),
		     "*",
		     walkTree (t.getChildren()[2])) ;
		// some refactoring possible here!
	    } else if (t.getLabel() == "#Tm") {
		if (isEmpty (t.getChildren()[1]))
		    return walkTree (t.getChildren()[0]) ;
		else return new Expression 
		    (walkTree (t.getChildren()[0]),
		     "+",
		     walkTree (t.getChildren()[1])) ;
	    } else if (t.getLabel() == "#PlusOps") {
		// can assume non-empty
		if (isEmpty (t.getChildren()[2]))
		    return walkTree (t.getChildren()[1]) ;
		else return new Expression 
		    (walkTree (t.getChildren()[1]),
		     "+",
		     walkTree (t.getChildren()[2])) ;
	    } else if (t.getLabel() == "#Fm2") {
		if (t.getRhs()[0] == "#Tm")
		    return new Expression
			(walkTree (t.getChildren()[0]),
			 "=",
			 walkTree (t.getChildren()[2])) ;
		else if (t.getRhs()[0] == "FALSE")
		    return walkTree (t.getChildren()[0]) ;
		else if (t.getRhs()[0] == "LOGICVAR")
		    return walkTree (t.getChildren()[0]) ;
		// LOGICVAR arguments ignored for now
		else if (t.getRhs()[0] == "~")
		    return new Expression
			(walkTree (t.getChildren()[1]), "~", null) ;
		else // if (t.getRhs()[0] == "[")
		    return walkTree (t.getChildren()[1]) ;
	    } else if (t.getLabel() == "#Fm1") {
		if (isEmpty (t.getChildren()[1]))
		    return walkTree (t.getChildren()[0]) ;
		else return new Expression 
		    (walkTree (t.getChildren()[0]),
		     "&",
		     walkTree (t.getChildren()[1])) ;
	    } else if (t.getLabel() == "#AndOps") {
		// can assume non-empty
		if (isEmpty (t.getChildren()[2]))
		    return walkTree (t.getChildren()[1]) ;
		else return new Expression 
		    (walkTree (t.getChildren()[1]),
		     "&",
		     walkTree (t.getChildren()[2])) ;
	    } else if (t.getLabel() == "#Fm") {
		if (isEmpty (t.getChildren()[1]))
		    return walkTree (t.getChildren()[0]) ;
		else if (t.getChildren()[1].getRhs()[0] == "<->")
		    return new Expression
		    (walkTree (t.getChildren()[0]),
		     "<->",
                     walkTree (t.getChildren()[1].getChildren()[1])) ;
		else return new Expression 
		    (walkTree (t.getChildren()[0]),
		     "->",
		     walkTree (t.getChildren()[1])) ;
	    } else if (t.getLabel() == "#ImplOps") {
		// can assume ->
		if (isEmpty (t.getChildren()[2]))
		    return walkTree (t.getChildren()[1]) ;
		else if (t.getChildren()[2].getRhs()[0] == "<->")
		    return new Expression
		    (walkTree (t.getChildren()[1]),
		     "<->",
                     walkTree (t.getChildren()[2].getChildren()[1])) ;
		else return new Expression
		    (walkTree (t.getChildren()[1]),
		     "->",
		     walkTree (t.getChildren()[2])) ;
	    }
	    else return null ;
	}

    static String toString (Expression e) {
	if (e==null) return "-" ;
	else return ("(" + toString(e.getLeft()) + " " + e.getNode()
		     + " " + toString(e.getRight()) + ")") ;
    }
}