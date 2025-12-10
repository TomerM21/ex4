package ast.Stmt;

import ast.AstGraphviz;
import ast.AstNodeSerialNumber;
import ast.Exp.AstExp;
import ast.Helpers.HelperFunctions;
import symboltable.SymbolTable;
import types.TypeVoid;
import types.Type;


public class AstStmtReturn extends AstStmt
{
    public AstExp exp; // may be null

    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AstStmtReturn(AstExp exp)
    {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        serialNumber = AstNodeSerialNumber.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        if (exp == null)
            System.out.print("====================== stmt -> RETURN ;\n");
        else
            System.out.print("====================== stmt -> RETURN exp ;\n");

        /*******************************/
        /* COPY INPUT DATA MEMBERS ... */
        /*******************************/
        this.exp = exp;
    }

    /*************************************************/
    /* The printing message for a RETURN stmt AST    */
    /*************************************************/
    public void printMe()
    {
        /*************************************/
        /* AST NODE TYPE = AST STMT RETURN   */
        /*************************************/
        System.out.print("AST NODE RETURN STMT\n");

        /*****************************/
        /* RECURSIVELY PRINT CHILD  */
        /*****************************/
        if (exp != null) exp.printMe();

        /*********************************/
        /* Print to AST GRAPHVIZ DOT file */
        /*********************************/
        AstGraphviz.getInstance().logNode(
                serialNumber,
                "RETURN\nSTMT");

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (exp != null)
            AstGraphviz.getInstance().logEdge(serialNumber, exp.serialNumber);
    }
    @Override
    public Type SemantMe() {
        // Retrieve the return type of the function we are currently in
        Type expected = SymbolTable.getInstance().currentFunctionReturnType;
        
        if (expected == null) {
            // Should not happen if parser is correct (return outside function)
            error(); 
        }

        Type actual = TypeVoid.getInstance(); // Default for empty return
        if (exp != null) {
            actual = exp.SemantMe();
        }

        if (!HelperFunctions.canAssign(expected, actual)) {
            System.out.println(">> ERROR: Return type mismatch");
            error();
        }

        return null;
    }
}