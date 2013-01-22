package parser;

// File:   Logic_Lexer.java
// Author: John Longley
// Date:   November 2011

// Sample solution for lexer part of Informatics 2A Assignment 2 (2011).
// Lexical classes and lexer for the language MH (`Micro-Haskell').


import java.io.* ;

class Logic_Lexer extends GenLexer implements LEX_TOKEN_STREAM {

static class TermvarAcceptor extends GenAcceptor implements DFA {

    public String lexClass() {return "TERMVAR" ;} ;
    public int numberOfStates() {return 4 ;} ;

    int nextState (int state, char c) {
	switch (state) {
	case 0: if (CharTypes.isSmall(c)) return 1 ; else return 3 ;
	case 1: if (CharTypes.isSmall(c)) return 1 ;
	    else if (c == '\'') return 2 ; else return 3 ;
        case 2: if (c == '\'') return 2 ; else return 3 ;
        default: return 3 ; // dead state
	}
    }

    boolean accepting (int state) {return ((state == 1) || (state == 2)) ;}
    boolean dead (int state) {return (state == 3) ;}
}

static class MatchvarAcceptor extends GenAcceptor implements DFA {

    public String lexClass() {return "MATCHVAR" ;} ;
    public int numberOfStates() {return 5 ;} ;

    int nextState (int state, char c) {
	switch (state) {
        case 0: if (c == '?') return 1 ; else return 4 ;
	case 1: if (CharTypes.isSmall(c)) return 2 ; else return 4 ;
	case 2: if (CharTypes.isSmall(c)) return 2 ;
	    else if (c == '\'') return 3 ; else return 4 ;
        case 3: if (c == '\'') return 3 ; else return 4 ;
        default: return 4 ; // dead state
	}
    }

    boolean accepting (int state) {return ((state == 1) || 
					   (state == 2) || 
					   (state == 3)) ;}
    boolean dead (int state) {return (state == 4) ;}
}

static class LogicvarAcceptor extends GenAcceptor implements DFA {

    public String lexClass() {return "LOGICVAR" ;} ;
    public int numberOfStates() {return 3 ;} ;

    int nextState (int state, char c) {
	switch (state) {
	case 0: if (CharTypes.isLarge(c)) return 1 ; else return 2 ;
        case 1: if (CharTypes.isLetter(c)) return 1 ; else return 2 ;
        default: return 2 ; // dead state
	}
    }

    boolean accepting (int state) {return (state == 1) ;}
    boolean dead (int state) {return (state == 2) ;}
}

static class NumAcceptor extends GenAcceptor implements DFA {

    public String lexClass() {return "NUM" ;} ;
    public int numberOfStates() {return 4 ;} ;

    int nextState (int state, char c) {
	switch (state) {
	case 0: if (c == '0') return 1 ;
	    else if (CharTypes.isNonzeroDigit(c)) return 2 ;
	    else return 3 ;
	case 1: return 3 ;
        case 2: if (CharTypes.isDigit(c)) return 2 ;
	    else return 3 ;
        default: return 3 ; // dead state
	}
    }

    boolean accepting (int state) {return ((state == 1) || (state == 2)) ;}
    boolean dead (int state) {return (state == 3) ;}
}


static class WhitespaceAcceptor extends GenAcceptor implements DFA {
    public String lexClass() {return "" ;} ;
    public int numberOfStates() {return 2 ;} ;

    int nextState (int state, char c) {
	switch (state) {
	case 0: if (c == ' ' || c == '\t' ||
		    c == '\r' || c == '\n' || c == '\f')
		return 0 ; else return 1 ;
	default: return 1 ;
	}
    }

    boolean accepting (int state) {return (state == 0) ;}
    boolean dead (int state) {return (state == 1) ;}
}

static class CommentAcceptor extends GenAcceptor implements DFA {
    public String lexClass() {return "" ;} ;
    public int numberOfStates() {return 6 ;} ;

    int nextState (int state, char c) {
	switch (state) {
	case 0: if (c == '-') return 1 ; else return 5 ;
	case 1: if (c == '-') return 2 ; else return 5 ;
	case 2: if (c == '-') return 2 ;
	    else if (!CharTypes.isSymbolic(c)) return 3 ; else return 5 ;
        case 3: if (CharTypes.isNewline(c)) return 4 ; else return 3 ;
	case 4: return 5 ;
        default: return 5 ; // dead state
	}
    }

    boolean accepting (int state) {return (state == 4) ;}
    boolean dead (int state) {return (state == 5) ;}
}

static class TokAcceptor extends GenAcceptor implements DFA {

    String tok, cls ;
    int tokLen ;
    TokAcceptor (String tok, String cls) {
	this.tok = tok ; this.cls = cls ; tokLen = tok.length() ;
    }
    TokAcceptor (String tok) {this(tok,tok) ;}

    public String lexClass() {return cls ;} ;
    public int numberOfStates() {return tokLen+2 ;} ;
    
    // state number records number of string characters processed.
    // tokLen+1 is dead state
	
    int nextState (int state, char c) {
	if (state < tokLen && c == tok.charAt(state)) {
	    return (state+1) ;
	} else return tokLen+1 ;
    }
	
    boolean accepting (int state) {return (state == tokLen) ;}
    boolean dead (int state) {return (state == tokLen+1) ;}
}

    static DFA termvarAcc = new TermvarAcceptor() ;
    static DFA matchvarAcc = new MatchvarAcceptor() ;
    static DFA logicvarAcc = new LogicvarAcceptor() ;
    static DFA numAcc = new NumAcceptor() ;
    static DFA whitespaceAcc = new WhitespaceAcceptor() ;

    static DFA lbrAcc = new TokAcceptor("(") ;
    static DFA rbrAcc = new TokAcceptor(")") ;
    static DFA lsqbrAcc = new TokAcceptor("[") ;
    static DFA rsqbrAcc = new TokAcceptor("]") ;
    static DFA commaAcc = new TokAcceptor(",") ;
    static DFA plusAcc = new TokAcceptor("+") ;
    static DFA timesAcc = new TokAcceptor("*") ;
    static DFA equalsAcc = new TokAcceptor("=") ;
    static DFA andAcc = new TokAcceptor("&") ;
    static DFA impliesAcc = new TokAcceptor("->") ;
    static DFA iffAcc = new TokAcceptor("<->") ;
    static DFA notAcc = new TokAcceptor("~") ;
    static DFA falseAcc = new TokAcceptor("!F", "FALSE") ;

    public static DFA[] Logic_acceptors = new DFA[] {
	lbrAcc, rbrAcc, lsqbrAcc, rsqbrAcc, commaAcc, plusAcc, timesAcc, 
        equalsAcc, andAcc, impliesAcc, iffAcc, notAcc, falseAcc,
	termvarAcc, matchvarAcc, logicvarAcc, numAcc, whitespaceAcc
    } ;

    Logic_Lexer (Reader reader) {
	super(reader, Logic_acceptors) ;
    }

    // For testing:

    public static void main (String[] args) 
	throws LexError, StateOutOfRange, IOException, UnknownSymbol {
	System.out.print ("Lexer> ") ;
	Reader reader = new BufferedReader 
	                (new InputStreamReader (System.in)) ;
        CheckedSymbolLexer demoLexer = 
	    new CheckedSymbolLexer (new GenLexer (reader, Logic_acceptors)) ;
	LexToken currTok = demoLexer.pullProperToken() ;
 	while (currTok != null) {
 	    System.out.println (currTok.value() + " \t" + currTok.lexClass()) ;
	    currTok = demoLexer.pullProperToken() ;
 	} ;
 	System.out.println ("END OF INPUT.") ;
    }
}

