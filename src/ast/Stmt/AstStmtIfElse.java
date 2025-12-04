package ast.Stmt;

import ast.AstGraphviz;
import ast.AstNodeSerialNumber;
import ast.Exp.AstExp;

public class AstStmtIfElse extends AstStmt
{
    public AstExp cond;
    public AstStmtList body;
    public AstStmtList elseBody;

    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AstStmtIfElse(AstExp cond, AstStmtList body, AstStmtList elseBody)
    {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        serialNumber = AstNodeSerialNumber.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        System.out.print("====================== stmt -> IF (exp) { stmtList } ELSE { stmtList }\n");

        /*******************************/
        /* COPY INPUT DATA MEMBERS ... */
        /*******************************/
        this.cond = cond;
        this.body = body;
        this.elseBody = elseBody;
    }

    /*************************************************/
    /* PRINT MESSAGE FOR IF-ELSE STMT AST NODE       */
    /*************************************************/
    public void printMe()
    {
        /*************************************/
        /* AST NODE TYPE = AST STMT IF-ELSE  */
        /*************************************/
        System.out.print("AST NODE IF-ELSE STMT\n");

        /*****************************/
        /* RECURSIVELY PRINT CHILDREN */
        /*****************************/
        if (cond != null) cond.printMe();
        if (body != null) body.printMe();
        if (elseBody != null) elseBody.printMe();

        /*********************************/
        /* PRINT Node to AST GRAPHVIZ     */
        /*********************************/
        AstGraphviz.getInstance().logNode(
                serialNumber,
                "IF-ELSE\nSTMT");

        /****************************************/
        /* PRINT EDGES to AST GRAPHVIZ DOT file */
        /****************************************/
        if (cond != null)
            AstGraphviz.getInstance().logEdge(serialNumber, cond.serialNumber);

        if (body != null)
            AstGraphviz.getInstance().logEdge(serialNumber, body.serialNumber);

        if (elseBody != null)
            AstGraphviz.getInstance().logEdge(serialNumber, elseBody.serialNumber);
    }
}