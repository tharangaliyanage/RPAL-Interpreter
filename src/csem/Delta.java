package csem;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import ast.ASTNode;
import ast.ASTNodeType;

/**
 * Represents a lambda closure.
 *
 */
public class Delta extends ASTNode{
  private List<String> boundVars;
  private Environment linkedEnv; //environment in effect when this Delta was pushed on to the value stack
  private Stack<ASTNode> body;
  private int index;
  
  public Delta(){
    setType(ASTNodeType.DELTA);
    boundVars = new ArrayList<String>();
  }
  
  public Delta accept(NodeCopier nodeCopier){
    return nodeCopier.copy(this);
  }
  
  //used if the program evaluation results in a partial application
  @Override
  public String getValue(){
    return "[lambda closure: "+boundVars.get(0)+": "+index+"]";
  }
  
  //get the list of bound variables and add a new bound variable to the list
  public List<String> getBoundVars(){
    return boundVars;
  }
  
  public void addBoundVars(String boundVar){
    boundVars.add(boundVar);
  }
  
  //set the list of bound variables to a provided list
  public void setBoundVars(List<String> boundVars){
    this.boundVars = boundVars;
  }
  
  //get the body of the lambda closure (as a stack of AST nodes) and set the body to a provided stack of AST nodes
  public Stack<ASTNode> getBody(){
    return body;
  }
  
  public void setBody(Stack<ASTNode> body){
    this.body = body;
  }
   //get and set the index value of the Delta node.
  public int getIndex(){
    return index;
  }

  public void setIndex(int index){
    this.index = index;
  }
  
  //get and set the linked environment associated with the Delta node
  public Environment getLinkedEnv(){
    return linkedEnv;
  }

  public void setLinkedEnv(Environment linkedEnv){
    this.linkedEnv = linkedEnv;
  }
}
