package scanner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class Scanner{
  /*
   * The Scanner class is responsible for reading the input file and returning the next token.
   * It combines a lexer and a screener.
   */

  private BufferedReader buffer; //buffered reader to read input file
  private String extraCharRead; //used to store extra character read from input file
  private final List<String> reservedIdentifiers = Arrays.asList(new String[]{"let","in","within","fn","where","aug","or",
                                                                              "not","gr","ge","ls","le","eq","ne","true",
                                                                              "false","nil","dummy","rec","and"}); 
  private int sourceLineNumber; //line number of the input file
  

  public Scanner(String inputFile) throws IOException{
    /*
     * The constructor of the Scanner class is responsible for opening the input file and initializing the
     * sourceLineNumber to 1.
     */
    sourceLineNumber = 1;
    buffer = new BufferedReader(new InputStreamReader(new FileInputStream(new File(inputFile))));
  }
  

  public Token readNextToken(){
    /*
     * The `readNextToken()` method is responsible for reading the next token from the input file.
     * It should return null if the file has ended.
     */
    Token nextToken = null;
    String nextChar;
    if(extraCharRead!=null){
      nextChar = extraCharRead;
      extraCharRead = null;
    } else
      nextChar = readNextChar();

    if(nextChar!=null)
      nextToken = buildToken(nextChar); 
    
    return nextToken; 
  }

  private String readNextChar(){
    /*
     * The `readNextChar()` method is responsible for reading the next character from the input file.
     */
    String nextChar = null;
    try{
      int c = buffer.read();
      if(c!=-1){
        nextChar = Character.toString((char)c);
        if(nextChar.equals("\n")) sourceLineNumber++; //increment line number if we read a new line
      } else
          buffer.close();
    }catch(IOException e){
    }
    
    return nextChar;
  }

  private Token buildToken(String currentChar){
    /*
     * The `buildToken()` method is responsible for building the next token from the input file. 
     * currentChar is the character currently being processed.
     * It should return the token that was built.
     */
    Token nextToken = null;
    if(LexicalRegexPatterns.LetterPattern.matcher(currentChar).matches()){
      nextToken = buildIdentifierToken(currentChar);
    }
    else if(LexicalRegexPatterns.DigitPattern.matcher(currentChar).matches()){
      nextToken = buildIntegerToken(currentChar);
    }
    else if(LexicalRegexPatterns.OpSymbolPattern.matcher(currentChar).matches()){ 
      nextToken = buildOperatorToken(currentChar);
    }
    else if(currentChar.equals("\'")){
      nextToken = buildStringToken(currentChar);
    }
    else if(LexicalRegexPatterns.SpacePattern.matcher(currentChar).matches()){
      nextToken = buildSpaceToken(currentChar);
    }
    else if(LexicalRegexPatterns.PunctuationPattern.matcher(currentChar).matches()){
      nextToken = buildPunctuationPattern(currentChar);
    }
    
    return nextToken;
  }


  private Token buildIdentifierToken(String currentChar){
  /**
   * buildIdentifierToken is responsible for building the Identifier token.
   * Identifier -> Letter (Letter | Digit | '_')*
   * currentChar is the character currently being processed.
   * It should return the token that was built.
   */
    Token identifierToken = new Token();
    identifierToken.setType(TokenType.IDENTIFIER);
    identifierToken.setSourceLineNumber(sourceLineNumber);
    StringBuilder sBuilder = new StringBuilder(currentChar);
    
    String nextChar = readNextChar();
    while(nextChar!=null){ //null indicates the file ended
      if(LexicalRegexPatterns.IdentifierPattern.matcher(nextChar).matches()){
        sBuilder.append(nextChar);
        nextChar = readNextChar();
      }
      else{
        extraCharRead = nextChar;
        break;
      }
    }
    
    String value = sBuilder.toString();
    if(reservedIdentifiers.contains(value))
      identifierToken.setType(TokenType.KEYWORD);
    
    identifierToken.setValue(value);
    
    return identifierToken;
  }


  private Token buildIntegerToken(String currentChar){
    /*
     * The `buildIntegerToken()` method is responsible for building the integer token.
     * Integer -> Digit+
     * currentChar is the character currently being processed.
     * It should return the token that was built.
     */
    Token integerToken = new Token();
    integerToken.setType(TokenType.INTEGER);
    integerToken.setSourceLineNumber(sourceLineNumber);
    StringBuilder sBuilder = new StringBuilder(currentChar);
    
    String nextChar = readNextChar();
    while(nextChar!=null){ //null indicates the file ended
      if(LexicalRegexPatterns.DigitPattern.matcher(nextChar).matches()){
        sBuilder.append(nextChar);
        nextChar = readNextChar();
      }
      else{
        extraCharRead = nextChar;
        break;
      }
    }
    
    integerToken.setValue(sBuilder.toString());
    
    return integerToken;
  }


  private Token buildOperatorToken(String currentChar){
    /*
     * The `buildOperatorToken()` method is responsible for building the operator token.
     * Operator_symbol -> Operator_symbol+
     * currentChar is the character currently being processed.
     * It should return the token that was built.
     */
    Token opSymbolToken = new Token();
    opSymbolToken.setType(TokenType.OPERATOR);
    opSymbolToken.setSourceLineNumber(sourceLineNumber);
    StringBuilder sBuilder = new StringBuilder(currentChar);
    
    String nextChar = readNextChar();
    
    if(currentChar.equals("/") && nextChar.equals("/"))
      return buildCommentToken(currentChar+nextChar);
    
    while(nextChar!=null){ //null indicates the file ended
      if(LexicalRegexPatterns.OpSymbolPattern.matcher(nextChar).matches()){
        sBuilder.append(nextChar);
        nextChar = readNextChar();
      }
      else{
        extraCharRead = nextChar;
        break;
      }
    }
    
    opSymbolToken.setValue(sBuilder.toString());
    
    return opSymbolToken;
  }


  private Token buildStringToken(String currentChar){
    /*
     * The `buildStringToken()` method is responsible for building the string token.
     * String -> '''' ('\' 't' | '\' 'n' | '\' '\' | '\' '''' |'(' | ')' | ';' | ',' |'' |Letter | Digit | Operator_symbol )* ''''
     * currentChar is the character currently being processed.
     * It should return the token that was built.
     */
    Token stringToken = new Token();
    stringToken.setType(TokenType.STRING);
    stringToken.setSourceLineNumber(sourceLineNumber);
    StringBuilder sBuilder = new StringBuilder("");
    String nextChar = readNextChar();
    while(nextChar!=null){ //null indicates the file ended
      if(nextChar.equals("\'")){ //we just used up the last char we read, hence no need to set extraCharRead
        //sBuilder.append(nextChar);
        stringToken.setValue(sBuilder.toString());
        
        return stringToken;
      }
      else if(LexicalRegexPatterns.StringPattern.matcher(nextChar).matches()){ //match Letter | Digit | Operator_symbol
        sBuilder.append(nextChar);
        nextChar = readNextChar();
      }
    }
    
    return null; 
  }
  
  private Token buildSpaceToken(String currentChar){
    /*
     * The `buildSpaceToken()` method is responsible for building the space token.
     * It reads consecutive space characters until a non-space character is encountered.
     * currentChar is the character currently being processed.
     * It should return the token that was built.
     */
    Token deleteToken = new Token();
    deleteToken.setType(TokenType.DELETE);
    deleteToken.setSourceLineNumber(sourceLineNumber);
    StringBuilder sBuilder = new StringBuilder(currentChar);
    
    String nextChar = readNextChar();
    while(nextChar!=null){ //null indicates the file ended
      if(LexicalRegexPatterns.SpacePattern.matcher(nextChar).matches()){
        sBuilder.append(nextChar);
        nextChar = readNextChar();
      }
      else{
        extraCharRead = nextChar;
        break;
      }
    }
    
    deleteToken.setValue(sBuilder.toString());
    //System.out.println("Space token: "+deleteToken);
    return deleteToken;
  }
  
  private Token buildCommentToken(String currentChar){
    /*
     * The `buildCommentToken()` method is responsible for building the comment token.
     * It reads consecutive comment characters until a new line character is encountered.
     * currentChar is the character currently being processed.
     * It should return the token that was built.
     */
    Token commentToken = new Token();
    commentToken.setType(TokenType.DELETE);
    commentToken.setSourceLineNumber(sourceLineNumber);
    StringBuilder sBuilder = new StringBuilder(currentChar);
    
    String nextChar = readNextChar();
    while(nextChar!=null){ //null indicates the file ended
      if(LexicalRegexPatterns.CommentPattern.matcher(nextChar).matches()){
        sBuilder.append(nextChar);
        nextChar = readNextChar();
      }
      else if(nextChar.equals("\n"))
        break;
    }
    
    commentToken.setValue(sBuilder.toString());
    //System.out.println("Comment token: "+commentToken.getValue());
    return commentToken;
  }

  private Token buildPunctuationPattern(String currentChar){
    /*
     * The `buildPunctuationPattern()` method is responsible for building the punctuation token.
     * It reads consecutive punctuation characters until a non-punctuation character is encountered.
     * currentChar is the character currently being processed.
     * It should return the token that was built.
     */
    Token punctuationToken = new Token();
    punctuationToken.setSourceLineNumber(sourceLineNumber);
    punctuationToken.setValue(currentChar);
    if(currentChar.equals("("))
      punctuationToken.setType(TokenType.L_PAREN);
    else if(currentChar.equals(")"))
      punctuationToken.setType(TokenType.R_PAREN);
    else if(currentChar.equals(";"))
      punctuationToken.setType(TokenType.SEMICOLON);
    else if(currentChar.equals(","))
      punctuationToken.setType(TokenType.COMMA);
    //System.out.println("Punctuation token: "+punctuationToken.getValue());
    return punctuationToken;
  }
}

