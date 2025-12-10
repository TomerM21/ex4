package ast.Var;

import ast.AstGraphviz;
import ast.AstNodeSerialNumber;
import ast.Exp.AstExp;
import types.Type;

public class AstVarSubscript extends AstVar
{
	public AstVar var;
	public AstExp subscript;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstVarSubscript(AstVar var, AstExp subscript)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== var -> var [ exp ]\n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.var = var;
		this.subscript = subscript;
	}

	/*****************************************************/
	/* The printing message for a subscript var AST node */
	/*****************************************************/
	public void printMe()
	{
		/*************************************/
		/* AST NODE TYPE = AST SUBSCRIPT VAR */
		/*************************************/
		System.out.print("AST NODE SUBSCRIPT VAR\n");

		/****************************************/
		/* RECURSIVELY PRINT VAR + SUBSCRIPT ... */
		/****************************************/
		if (var != null) var.printMe();
		if (subscript != null) subscript.printMe();
		
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AstGraphviz.getInstance().logNode(
				serialNumber,
			"SUBSCRIPT\nVAR\n...[...]");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (var       != null) AstGraphviz.getInstance().logEdge(serialNumber,var.serialNumber);
		if (subscript != null) AstGraphviz.getInstance().logEdge(serialNumber,subscript.serialNumber);
	}
	@Override
    public Type SemantMe() {
        // 1. Analyze the array variable
        Type t = var.SemantMe();
        
        // 2. Ensure it is an Array Type
        if (t == null || !(t instanceof types.TypeArray)) {
            System.out.println(">> ERROR: Subscripting non-array type");
            error();
        }
        
        // 3. Analyze the index expression
        Type indexType = subscript.SemantMe();
        
        // 4. Ensure index is an Integer
        if (!indexType.isInt()) {
            System.out.println(">> ERROR: Array index must be int");
            error();
        }
        
        // 5. Return the type of elements inside the array
        return ((types.TypeArray) t).elementType;
    }
}
