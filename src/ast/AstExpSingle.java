package ast;

public class AstExpSingle extends AstExp {

    public AstExp exp;

    public AstExpSingle(AstExp exp)
    {
        serialNumber = AstNodeSerialNumber.getFresh();

        System.out.print("====================== exp -> (exp)\n");

        this.exp = exp;
    }

    public void printMe()
    {
        /*************************************/
        /* AST NODE TYPE = AST EXP SINGLE     */
        /*************************************/
        System.out.print("AST NODE EXP SINGLE\n");

        /*****************************/
        /* RECURSIVELY PRINT KIDS   */
        /*****************************/
        if (exp != null) exp.printMe();

        /*********************************/
        /* Print to AST GRAPHVIZ DOT file */
        /*********************************/
        AstGraphviz.getInstance().logNode(
                serialNumber,
                "EXP\nSINGLE");

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (exp != null)
            AstGraphviz.getInstance().logEdge(serialNumber, exp.serialNumber);
    }
}