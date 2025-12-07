package ast;

import ast.Helpers.HelperFunctions;
import types.Type;

public abstract class AstNode {

    public int serialNumber;

    public static int currLine = 0; // TODO WHILE PARSING SET THIS TO THE CURRENT LINE
    public int lineNumber = currLine; 

    public AstNode left = null;
    public AstNode right = null;

    public void printMe() {
        System.out.print("AST NODE UNKNOWN\n");
    }

    // Semantic default
    public Type SemantMe() { return null; }

    public void error() {
        HelperFunctions.printErrorAndExit(this.lineNumber);
    }
}