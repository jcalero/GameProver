package parser;

//A *lexical token* is simply a string tagged with the name of its
//lexical class.

class LexToken {
 private final String value, lexClass ;
 LexToken (String value, String lexClass) {
	this.value = value ; this.lexClass = lexClass ;
 }
 public String value () {return this.value ;} ;
 public String lexClass () {return this.lexClass ;} ;
}

//Typical example: new LexToken ("5", "num")