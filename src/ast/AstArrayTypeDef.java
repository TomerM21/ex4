package ast;

public class AstArrayTypeDef extends AstDec {
    private String name;
    private AstType type;

    public AstArrayTypeDef(String name, AstType type)
    {
        serialNumber = AstNodeSerialNumber.getFresh();

        System.out.print("====================== arrayTypeDef -> type ID LBRACKET RBRACKET SEMICOLON\n");

        this.name = name;
        this.type = type;
    }

    public void printMe()
    {
        /*************************************/
        /* AST NODE TYPE = AST ARRAY TYPE DEF */
        /*************************************/
        System.out.format("AST NODE ARRAY TYPE DEF %s\n", name);

        /*****************************/
        /* RECURSIVELY PRINT KIDS   */
        /*****************************/
        if (type != null) type.printMe();

        /*********************************/
        /* Print to AST GRAPHVIZ DOT file */
        /*********************************/
        AstGraphviz.getInstance().logNode(
                serialNumber,
                String.format("ARRAY TYPE DEF(%s)", name));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (type != null)
            AstGraphviz.getInstance().logEdge(serialNumber, type.serialNumber);
    }
}