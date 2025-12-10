package ast.Stmt;

import ast.AstGraphviz;
import ast.AstNodeSerialNumber;
import ast.Exp.AstExp;
import symboltable.SymbolTable;
import types.Type;

public class AstStmtIf extends AstStmt
{
    public AstExp cond;
    public AstStmtList body;

    /*******************/
    /*  CONSTRUCTOR(S) */
    /*******************/
    public AstStmtIf(AstExp cond, AstStmtList body)
    {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        serialNumber = AstNodeSerialNumber.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        System.out.print("====================== stmt -> IF (exp) { stmtList }\n");

        /*******************************/
        /* COPY INPUT DATA MEMBERS ... */
        /*******************************/
        this.cond = cond;
        this.body = body;
    }

    /*************************************************/
    /* The printing message for an IF stmt AST node  */
    /*************************************************/
    public void printMe()
    {
        /*************************************/
        /* AST NODE TYPE = AST STMT IF       */
        /*************************************/
        System.out.print("AST NODE IF STMT\n");

        /*****************************/
        /* RECURSIVELY PRINT KIDS   */
        /*****************************/
        if (cond != null) cond.printMe();
        if (body != null) body.printMe();

        /*********************************/
        /* Print to AST GRAPHVIZ DOT file */
        /*********************************/
        AstGraphviz.getInstance().logNode(
                serialNumber,
                "IF\nSTMT");

        /****************************************/
        /* PRINT EDges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (cond != null)
            AstGraphviz.getInstance().logEdge(serialNumber, cond.serialNumber);
        if (body != null)
            AstGraphviz.getInstance().logEdge(serialNumber, body.serialNumber);
    }
    @Override
    public Type SemantMe() {
        Type condType = cond.SemantMe();
        if (!condType.isInt()) {
            System.out.println(">> ERROR: If condition must be int");
            error();
        }
        
        // Scoping rules: blocks usually create new scopes
        SymbolTable.getInstance().beginScope();
        if (body != null) body.SemantMe();
        SymbolTable.getInstance().endScope();
        
        return null;
    }
}