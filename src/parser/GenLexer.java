package parser;

// File:   GenLexer.java
// Author: John Longley
// Date:   November 2011

// Java source file provided for Informatics 2A Assignment 2 (2011).
// Contains general infrastructure relating to DFAs and longest-match lexing,
// along with some trivial examples.


import java.io.* ;

// Some useful sets of characters.

class CharTypes {

    static boolean isLetter (char c) {
	return (('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z')) ;
    }

    static boolean isSmall (char c) {
	return (('a' <= c && c <= 'z') || c == '_') ;
    }

    static boolean isLarge (char c) {
	return ('A' <= c && c <= 'Z') ;
    }

    static boolean isDigit (char c) {
	return ('0' <= c && c <= '9') ;
    }

    static boolean isNonzeroDigit (char c) {
	return ('1' <= c && c <= '9') ;
    }

    static boolean isSymbolic (char c) {
	return (c == '!' || c == '#' || c == '$' || c == '%' || c == '&' || 
		c == '*' || c == '+' || c == '.' || c == '/' || c == '<' || 
		c == '=' || c == '>' || c == '?' || c == '@' || c == '\\' ||
		c == '^' || c == '|' || c == '-' || c == '~' || c == ':') ;
    }

    static boolean isWhitespace (char c) {
	return (c == ' ' || c == '\t' || c == '\r' || c == '\n' || c == '\f') ;
    }

    static boolean isNewline (char c) {
	return (c == '\r' || c == '\n' || c == '\f') ;
    }

}

// Generic implementation of DFAs with explicit "dead" states

interface DFA {
    String lexClass() ;
    int numberOfStates() ;
    void reset() ;
    void processChar (char c) throws StateOutOfRange ;
    boolean isAccepting () ;
    boolean isDead () ;
}

abstract class GenAcceptor {

    // Stubs for methods specific to a particular DFA
    abstract String lexClass() ;
    abstract int numberOfStates() ;
    abstract int nextState (int state, char input) ;
    abstract boolean accepting (int state) ;
    abstract boolean dead (int state) ;

    // General DFA machinery
    private int currState = 0 ;         // the initial state is always 0
    public void reset () {currState = 0 ;}  

    public void processChar (char c) throws StateOutOfRange {
	// performs the state transition determined by c
	currState = nextState (currState,c) ;
	if (currState >= numberOfStates()) {
	    throw new StateOutOfRange (lexClass(), currState) ;
	}
    }

    public boolean isAccepting () {return accepting (currState) ;}
    public boolean isDead () {return dead (currState) ;}
}

class StateOutOfRange extends Exception {
	private static final long serialVersionUID = -1516180731227750791L;

	public StateOutOfRange (String lexClassName, int state) {
	super ("Illegal state " + Integer.toString(state) + 
               " in acceptor for " + lexClassName) ;
    }
}


// Generic lexical analyser. 
// Uses principle of longest match, a.k.a. "maximal munch".


// The following allows a LEX_TOKEN_STREAM object to be created for
// a given input file and a language-specific repertoire of lexical classes.

public class GenLexer implements LEX_TOKEN_STREAM {

    Reader reader ;       
    // for reading characters from input
    DFA[] acceptors ;  
    // array of acceptors for the lexical classes, in order of priority

    GenLexer (Reader reader, DFA[] acceptors) {
	this.reader = reader ;
	this.acceptors = acceptors ;
    }

    LexToken bufferToken ;       // buffer to allow 1-token lookahead
    boolean bufferInUse = false ;

    static final char EOF = (char)65535 ;

    // Implementation of longest-match lexer as described in lectures.
    // We go for simplicity and clarity rather than maximum efficiency.

    LexToken nextToken () 
	throws LexError, StateOutOfRange, IOException {
        char c ;                 // current input character
	String definite = "" ;   // characters up to last acceptance point
	String maybe = "" ;      // characters since last acceptance point
        int acceptorIndex = -1 ; // array index of highest priority acceptor
	boolean liveFound = false ;      // flags for use in 
	boolean acceptorFound = false ;  // iteration over acceptors

	for (int i=0; i<acceptors.length; i++) {
	    acceptors[i].reset() ;
	} ;
	do {
	    c = (char)(reader.read()) ;
	    acceptorFound = false ;
	    liveFound = false ;
	    if (c != EOF) {
		maybe += c ;    
		for (int i=0; i<acceptors.length; i++) {
		    acceptors[i].processChar(c) ;
		    if (!acceptors[i].isDead()) {
			liveFound = true ;
		    }
		    if (!acceptorFound && acceptors[i].isAccepting()) {
			acceptorFound = true ;
			acceptorIndex = i ;
			definite += maybe ;
			maybe = "" ;
			reader.mark(10) ; // register backup point in input
		    } ;
		}
	    }
	} while (liveFound && c != EOF) ;
	if (acceptorIndex >= 0) { // lex token has been found
	    // backup to last acceptance point and output token
	    reader.reset() ;
	    String theLexClass = acceptors[acceptorIndex].lexClass() ;
	    return new LexToken (definite, theLexClass) ;
	} else if (c == EOF && maybe.equals("")) {
	    // end of input already reached before nextToken was called
	    reader.close() ;
	    return null ;    // by convention, signifies end of input
	} else {
	    reader.close() ;
	    throw new LexError(maybe) ;
	}
    }

    public LexToken peekToken () 
	throws LexError, StateOutOfRange, IOException {
	if (bufferInUse) {
	    return bufferToken ;
	} else {
	    bufferToken = nextToken() ;
	    bufferInUse = true ;
	    return bufferToken ;
	}
    }

    public LexToken pullToken () 
	throws LexError, StateOutOfRange, IOException {
	peekToken () ;
	bufferInUse = false ;
	return bufferToken ;
    }

    public LexToken peekProperToken () 
	throws LexError, StateOutOfRange, IOException {
	LexToken tok = peekToken () ;
	while (tok != null && tok.lexClass().equals("")) {
	    pullToken () ;
	    tok = peekToken () ;
	}
	bufferToken = tok ;
	bufferInUse = true ;
	return tok ;
    }

    public LexToken pullProperToken () 
	throws LexError, StateOutOfRange, IOException {
	peekProperToken () ;
	bufferInUse = false ;
	return bufferToken ;
    }
}

class LexError extends Exception {
	private static final long serialVersionUID = 7899116875935427634L;

	public LexError (String nonToken) {
	super ("Can't make lexical token from input \"" + 
	       nonToken + "\"") ;
    }
}

// To try out the lexer on the dummy examples, compile this file, type 
//    java LexerDemo
// and then type a line of input such as
//    abcd&&&&
// You can also experiment with erroneous inputs.

