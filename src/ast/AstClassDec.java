package ast;

public class AstClassDec extends AstDec {
    private String name;
    private String extends_name;
    private AstCFieldList dataMemberList;

    public AstClassDec(String name, String extends_name, AstCFieldList cFieldList) 
    {
        serialNumber = AstNodeSerialNumber.getFresh();

        if (extends_name != null)
            System.out.print("====================== classDec -> CLASS ID EXTENDS ID LBRACE cFieldList RBRACE\n");
        else
            System.out.print("====================== classDec -> CLASS ID LBRACE cFieldList RBRACE\n");

        this.name = name;
        this.extends_name = extends_name;
        this.dataMemberList = cFieldList;
    }

    public void printMe()
    {
        /*************************************/
        /* AST NODE TYPE = AST CLASS DEC      */
        /*************************************/
        if (extends_name != null)
            System.out.format("AST NODE CLASS DEC %s EXTENDS %s\n", name, extends_name);
        else
            System.out.format("AST NODE CLASS DEC %s\n", name);

        /*****************************/
        /* RECURSIVELY PRINT KIDS   */
        /*****************************/
        if (dataMemberList != null) dataMemberList.printMe();

        /*********************************/
        /* Print to AST GRAPHVIZ DOT file */
        /*********************************/
        AstGraphviz.getInstance().logNode(
                serialNumber,
                String.format("CLASS DEC(%s)", name));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (dataMemberList != null)
            AstGraphviz.getInstance().logEdge(serialNumber, dataMemberList.serialNumber);
    }
}