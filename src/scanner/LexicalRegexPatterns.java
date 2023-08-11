package scanner;

import java.util.regex.Pattern;

/**
 * Regex matchers that comply with RPAL's lexical grammar. Used by the scanner to
 * tokenize the input.
 * 
 * 
 */
public class LexicalRegexPatterns{
// Define regex strings for different terminal types

  private static final String letterRegexString = "a-zA-Z"; // Match any letter (uppercase or lowercase)
  private static final String digitRegexString = "\\d";  // Match any digit (0-9)
  private static final String spaceRegexString = "[\\s\\t\\n]"; // Match whitespace characters (spaces, tabs, newlines)
  private static final String punctuationRegexString = "();,"; // Match specific punctuation characters
  private static final String opSymbolRegexString = "+-/~:=|!#%_{}\"*<>.&$^\\[\\]?@"; // Match various operator symbols
  private static final String opSymbolToEscapeString = "([*<>.&$^?])"; // Specific operator symbols that need escaping
  

  // Define patterns for different terminal types
  public static final Pattern LetterPattern = Pattern.compile("["+letterRegexString+"]");
  public static final Pattern IdentifierPattern = Pattern.compile("["+letterRegexString+digitRegexString+"_]");
  public static final Pattern DigitPattern = Pattern.compile(digitRegexString);
  public static final Pattern PunctuationPattern = Pattern.compile("["+punctuationRegexString+"]");
  public static final String opSymbolRegex = "[" + escapeMetaChars(opSymbolRegexString, opSymbolToEscapeString) + "]";
  public static final Pattern OpSymbolPattern = Pattern.compile(opSymbolRegex);
  public static final Pattern StringPattern = Pattern.compile("[ \\t\\n\\\\"+punctuationRegexString+letterRegexString+digitRegexString+escapeMetaChars(opSymbolRegexString, opSymbolToEscapeString) +"]");
  public static final Pattern SpacePattern = Pattern.compile(spaceRegexString);
  public static final Pattern CommentPattern = Pattern.compile("[ \\t\\'\\\\ \\r"+punctuationRegexString+letterRegexString+digitRegexString+escapeMetaChars(opSymbolRegexString, opSymbolToEscapeString)+"]"); //the \\r is for Windows LF; not really required since we're targeting *nix systems
  // Method to escape characters with special meanings in regular expressions
  private static String escapeMetaChars(String inputString, String charsToEscape){
    return inputString.replaceAll(charsToEscape,"\\\\\\\\$1");
  }
}

