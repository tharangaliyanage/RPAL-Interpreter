import ast.AST;
import csem.*;
import scanner.*;
import parser.*;

import java.io.IOException;

public class rpal20 {
  public static void main(String[] args) throws Exception {
    String fileName = args[0];
    AST ast = null;

    try {
      Scanner scanner = new Scanner(fileName);
      Parser parser = new Parser(scanner);
      ast = parser.buildAST();
    } catch (IOException e) {
      throw new ParseException("ERROR ");
    }

    ast.standardize();
    CSEMachine csem = new CSEMachine(ast);
    csem.evaluateProgram();
    System.out.println();

  }

}
