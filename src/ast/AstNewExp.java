package ast;

public class AstNewExp extends AstExp {

    public AstType type;
    public AstExp sizeExp;   // null if not array allocation

    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AstNewExp(AstType type, AstExp sizeExp)
    {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        serialNumber = AstNodeSerialNumber.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        if (sizeExp == null)
            System.out.print("====================== newExp -> NEW type\n");
        else
            System.out.print("====================== newExp -> NEW type [exp]\n");

        /*******************************/
        /* COPY INPUT DATA MEMBERS ... */
        /*******************************/
        this.type = type;
        this.sizeExp = sizeExp;
    }

    /**********************************************/
    /* The printing message for a new exp AST node */
    /**********************************************/
    public void printMe()
    {
        /*************************************/
        /* AST NODE TYPE = AST NEW EXP       */
        /*************************************/
        System.out.print("AST NODE NEW EXP\n");

        /*****************************/
        /* RECURSIVELY PRINT KIDS   */
        /*****************************/
        if (type != null) type.printMe();
        if (sizeExp != null) sizeExp.printMe();

        /*********************************/
        /* Print to AST GRAPHVIZ DOT file */
        /*********************************/
        AstGraphviz.getInstance().logNode(
                serialNumber,
                "NEW\nEXP");

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (type != null)
            AstGraphviz.getInstance().logEdge(serialNumber, type.serialNumber);
        if (sizeExp != null)
            AstGraphviz.getInstance().logEdge(serialNumber, sizeExp.serialNumber);
    }
}