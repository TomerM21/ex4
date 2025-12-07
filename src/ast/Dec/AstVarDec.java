package ast.Dec;

import ast.AstGraphviz;
import ast.AstNodeSerialNumber;
import ast.AstType;
import ast.Exp.AstExp;
import ast.Exp.AstNewExp;
import ast.Helpers.HelperFunctions;
import symboltable.SymbolTable;
import types.Type;
import types.TypeVoid;

public class AstVarDec extends AstDec {
    private AstType typeNode; // int, string, void, A, B, ... 
    private String name; // string name of the var
    private AstExp exp; // optional initialization expression
    private AstNewExp newExp; // optional new expression for class types

    public AstVarDec(AstType typeNode, String name, AstExp exp, AstNewExp newExp)
    {
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
        if (exp != null)
            System.out.print("====================== varDec -> type ID ASSIGN exp SEMICOLON\n");
        else if (newExp != null)
            System.out.print("====================== varDec -> type ID ASSIGN newExp SEMICOLON\n");
        else
		    System.out.print("====================== varDec -> type ID SEMICOLON\n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.typeNode = typeNode;
        this.name = name;
        this.exp = exp;
        this.newExp = newExp;
	}

    /*************************************************/
    /* The printing message for a var dec AST node   */
    /*************************************************/
    public void printMe()
    {
        /*************************************/
        /* AST NODE TYPE = AST VAR DEC       */
        /*************************************/
        System.out.print("AST NODE VAR DEC\n");

        /*****************************/
        /* RECURSIVELY PRINT KIDS   */
        /*****************************/
        if (typeNode != null) typeNode.printMe();
        if (exp != null) exp.printMe();
        if (newExp != null) newExp.printMe();

        /*********************************/
        /* Print to AST GRAPHVIZ DOT file */
        /*********************************/
        AstGraphviz.getInstance().logNode(
                serialNumber,
                String.format("VAR DEC(%s)", name));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (typeNode   != null) AstGraphviz.getInstance().logEdge(serialNumber, typeNode.serialNumber);
        if (exp    != null) AstGraphviz.getInstance().logEdge(serialNumber, exp.serialNumber);
        if (newExp != null) AstGraphviz.getInstance().logEdge(serialNumber, newExp.serialNumber);
    }
    
    @Override
    public Type SemantMe()
    {
        // get declared type
        Type varType = typeNode.SemantMe();

        // Type must exist
        if (varType == null) {
            error();
        }

        // void type is not allowed
        if (varType instanceof TypeVoid) {
            error();
        }

        // check if name already exists in the current scope, shadowing not allowed
        if (HelperFunctions.existsInCurrentScope(name)) {
            error();
        }

        // add variable to symbol table
        SymbolTable.getInstance().enter(name, varType);

        // if there is an initialization expression, check its type
        if (exp != null) {
            Type expType = exp.SemantMe();
            if (!HelperFunctions.canAssign(varType, expType)) {
                error();
            }
        }

        // if there is a new expression, check its type
        if (newExp != null) {
            Type newType = newExp.SemantMe();
            if (!HelperFunctions.canAssign(varType, newType)) {
                error();
            }
        }
        
        return null; 
    }
}
/*
varDec ::= type ID [ ASSIGN exp ] SEMICOLON
| type ID ASSIGN newExp SEMICOLON
 */