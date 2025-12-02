package ast;

public class AstCallExp extends AstExp {

    public AstVar receiver;       // null if calling f()
    public String methodName;
    public AstExpList args;       // null if no arguments

    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AstCallExp(AstVar receiver, String methodName, AstExpList args)
    {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        serialNumber = AstNodeSerialNumber.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        if (receiver == null && args == null)
            System.out.print("====================== callExp -> ID LPAREN RPAREN\n");
        else if (receiver == null)
            System.out.print("====================== callExp -> ID LPAREN expList RPAREN\n");
        else if (args == null)
            System.out.print("====================== callExp -> var DOT ID LPAREN RPAREN\n");
        else
            System.out.print("====================== callExp -> var DOT ID LPAREN expList RPAREN\n");

        /*******************************/
        /* COPY INPUT DATA MEMBERS ... */
        /*******************************/
        this.receiver = receiver;
        this.methodName = methodName;
        this.args = args;
    }

    /*****************************************************/
    /* The printing message for a method-call AST node    */
    /*****************************************************/
    public void printMe()
    {
        /*************************************/
        /* AST NODE TYPE = AST EXP METHOD     */
        /*************************************/
        System.out.format("AST NODE METHOD CALL (%s)\n", methodName);

        /*****************************/
        /* RECURSIVELY PRINT KIDS   */
        /*****************************/
        if (receiver != null) receiver.printMe();
        if (args != null) args.printMe();

        /*********************************/
        /* Print to AST GRAPHVIZ DOT file */
        /*********************************/
        AstGraphviz.getInstance().logNode(
                serialNumber,
                String.format("METHOD\nCALL(%s)", methodName));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (receiver != null)
            AstGraphviz.getInstance().logEdge(serialNumber, receiver.serialNumber);

        if (args != null)
            AstGraphviz.getInstance().logEdge(serialNumber, args.serialNumber);
    }
}