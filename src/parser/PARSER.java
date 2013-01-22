package parser;

public interface PARSER {
	TREE parseTokenStream(LEX_TOKEN_STREAM tokStream) throws Exception;
	TREE parseTokenStreamAs(LEX_TOKEN_STREAM tokStream, String nonterm)
			throws Exception;
}