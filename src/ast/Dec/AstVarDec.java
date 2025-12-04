package ast.Dec;

import ast.AstGraphviz;
import ast.AstNodeSerialNumber;
import ast.AstType;
import ast.Exp.AstExp;
import ast.Exp.AstNewExp;
import ast.Helpers.HelperFunctions;
import types.Type;
import types.TypeVoid;

public class AstVarDec extends AstDec {
    private AstType typeNode;  
    private String name;
    private AstExp exp;
    private AstNewExp newExp;

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
    public Type SemantMe() {
        // 1. Check if the type is valid
        Type varType = typeNode.SemantMe();

        // Type cannot be void
        if (varType instanceof TypeVoid) {
            System.out.format("Type of variable %s cannot be void\n", name);
            HelperFunctions.printErrorAndExit(myLine);
        }

        // 2. Check if the name is valid
        // 3. Check if the value (the exp/newExp) is the same type as the declared type
    }
    
}
/*
varDec ::= type ID [ ASSIGN exp ] SEMICOLON
| type ID ASSIGN newExp SEMICOLON
 */