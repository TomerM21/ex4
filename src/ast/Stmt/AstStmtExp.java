package ast.Stmt;

import ast.AstCallExp;
import ast.AstGraphviz;
import ast.AstNodeSerialNumber;
import ast.Exp.AstExp;
import types.Type;
public class AstStmtExp extends AstStmt
{
    public AstCallExp callExp;

    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AstStmtExp(AstCallExp callExp)
    {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        serialNumber = AstNodeSerialNumber.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        System.out.print("====================== stmt -> callExp ;\n");

        /*******************************/
        /* COPY INPUT DATA MEMBERS ... */
        /*******************************/
        this.callExp = callExp;
    }

    /*****************************************/
    /* PRINT MESSAGE FOR CALL STMT AST NODE  */
    /*****************************************/
    public void printMe()
    {
        /*************************************/
        /* AST NODE TYPE = AST STMT EXP      */
        /*************************************/
        System.out.print("AST NODE STMT EXP\n");

        /*****************************/
        /* RECURSIVELY PRINT child  */
        /*****************************/
        if (callExp != null) callExp.printMe();

        /*********************************/
        /* PRINT Node to AST GRAPHVIZ     */
        /*********************************/
        AstGraphviz.getInstance().logNode(
                serialNumber,
                "STMT\nEXP");

        /****************************************/
        /* PRINT EDGES to AST GRAPHVIZ DOT file */
        /****************************************/
        if (callExp != null)
            AstGraphviz.getInstance().logEdge(serialNumber, callExp.serialNumber);
    }
    @Override
    public Type SemantMe() {
        callExp.SemantMe();
        return null;
    }

    public temp.Temp irMe()
    {
        // Execute the call expression
        if (callExp != null) callExp.irMe();
        return null;
    }
}