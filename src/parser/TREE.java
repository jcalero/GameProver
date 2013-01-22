package parser;

// Recursive class for syntax tree nodes (any grammar).
// The same class serves for both terminal and non-terminal nodes.

interface TREE {
    String getLabel() ;    // nonterminal symbol or lexical class of terminal
    boolean isTerminal() ;
    String getValue() ;    // only relevant for terminal nodes
    void setValue(String value) ;
    String[] getRhs() ;    // only relevant for non-terminal nodes
    TREE[] getChildren() ; // ditto
    void setRhsChildren(String[] rhs, TREE[] children) ; 
}