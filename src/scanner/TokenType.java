package scanner;

public enum TokenType{
  /*
   * The TokenType enum is used to represent the type of a token constructed by the scanner.
   */
  IDENTIFIER, 
  INTEGER,
  STRING,
  OPERATOR,
  DELETE,
  L_PAREN,
  R_PAREN,
  SEMICOLON,
  COMMA,
  KEYWORD;
}
