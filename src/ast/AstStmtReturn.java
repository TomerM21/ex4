package ast;

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
}