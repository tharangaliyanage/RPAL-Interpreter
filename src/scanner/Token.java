package scanner;

public class Token{
  /*
  * The `Token` class represents units of information from the scanner to the parser.
  * Each token has a "type" and a "value." 
  * Some tokens have meaningful values, while others (e.g., "DELETE," "L_PAREN") have unimportant or empty values.
   */
  private TokenType type;
  private String value;
  private int sourceLineNumber;
  public TokenType getType() {
    return type;
  }
  public void setType(TokenType type) {
    this.type = type;
  }
  public String getValue() {
    return value;
  }
  public void setValue(String value) {
    this.value = value;
  }
  public int getSourceLineNumber() {
    return sourceLineNumber;
  }
  public void setSourceLineNumber(int sourceLineNumber) {
    this.sourceLineNumber = sourceLineNumber;
  }
  

}
