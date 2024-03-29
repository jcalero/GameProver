package parser ;

// File:   CheckedSymbolLexer.java
// Author: John Longley
// Date:   November 2011

// Java source file provided for Informatics 2A Assignment 2 (2011).
// Thin wrapper for token streams: checks if a symbol token is 
// among those of MH, and renames its lexical class to be the symbol itself.
// (Helpful for the parser, which only looks at lexical classes.)

// Probably not needed for Logic parser - should take it out!

import java.io.* ;

class CheckedSymbolLexer implements LEX_TOKEN_STREAM {

    GenLexer tokStream ;

    CheckedSymbolLexer (GenLexer tokStream) {
	this.tokStream = tokStream ;
    }

    static String[] validTokens = new String[]
	{"::", "->", "=", "==", "<", "+", "-", "*"} ;

    static String checkString (String s) throws UnknownSymbol {
	for (int i=0; i<validTokens.length; i++) {
	    if (s.equals(validTokens[i])) return s ;
	}
	throw new UnknownSymbol(s) ;
    }

    static LexToken checkToken (LexToken t) throws UnknownSymbol {
	if (t != null && t.lexClass().equals("SYM")) {
	    return new LexToken (t.value(), checkString(t.value())) ;
	} else return t ;
    }

    public LexToken pullToken () 
	throws LexError, StateOutOfRange, IOException, UnknownSymbol {
	return checkToken (tokStream.pullToken()) ;
    }

    public LexToken pullProperToken () 
	throws LexError, StateOutOfRange, IOException, UnknownSymbol {
	return checkToken (tokStream.pullProperToken()) ;
    }

    public LexToken peekToken () 
	throws LexError, StateOutOfRange, IOException, UnknownSymbol {
	return checkToken (tokStream.peekToken()) ;
    }

    public LexToken peekProperToken () 
	throws LexError, StateOutOfRange, IOException, UnknownSymbol {
	return checkToken (tokStream.peekProperToken()) ;
    }

}

class UnknownSymbol extends Exception {
	private static final long serialVersionUID = 650394028482696564L;

	public UnknownSymbol (String s) {
	super ("Unknown symbolic token " + s) ;
    }
}


