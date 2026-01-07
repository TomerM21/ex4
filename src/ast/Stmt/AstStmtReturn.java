package ast.Stmt;

import ast.AstGraphviz;
import ast.AstNodeSerialNumber;
import ast.Exp.AstExp;
import ast.Helpers.HelperFunctions;
import symboltable.SymbolTable;
import types.TypeVoid;
import types.Type;
import temp.*;
import ir.*;

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

        // CASE 1: Empty return "return;"
        if (exp == null) {
            // An empty return is ONLY valid if the function is declared void
            if (!(expected instanceof TypeVoid)) {
                System.out.println(">> ERROR: Missing return value in non-void function");
                error();
            }
            return null; // Correct usage of return;
        }

        // CASE 2: Return with expression "return exp;"
        Type actual = exp.SemantMe();

        // Explicitly check if the expression itself returns void (e.g., return a();)
        // You requested that this be an error even inside a void function.
        if (actual instanceof TypeVoid) {
            System.out.println(">> ERROR: Cannot return void expression");
            this.lineNumber = exp.lineNumber;
            error();
        }

        // Check if the actual type matches the expected return type
        if (!HelperFunctions.canAssign(expected, actual)) {
            System.out.println(">> ERROR: Return type mismatch");
            this.lineNumber = exp.lineNumber;
            error();
        }

        return null;
    }

    public Temp irMe()
    {
        if (exp != null) {
            Temp retVal = exp.irMe();
            Ir.getInstance().AddIrCommand(new IrCommandReturn(retVal));
        } else {
            // Void return
            Ir.getInstance().AddIrCommand(new IrCommandReturn(null));
        }
        return null;
    }
}