package ast;

public class AstFuncDec extends AstDec {
    private AstType returnType;
    private String name;
    private AstTypeList typeList;
    private AstStmtList stmtList;

    public AstFuncDec(AstType returnType, String name, AstTypeList typeList, AstStmtList stmtList)
    {
        serialNumber = AstNodeSerialNumber.getFresh();

        if (typeList != null)
            System.out.print("====================== funcDec -> type ID LPAREN typeList RPAREN LBRACE stmtList RBRACE\n");
        else
            System.out.print("====================== funcDec -> type ID LPAREN RPAREN LBRACE stmtList RBRACE\n");

        this.returnType = returnType;
        this.name = name;
        this.typeList = typeList;
        this.stmtList = stmtList;
    }

    public void printMe()
    {
        /*************************************/
        /* AST NODE TYPE = AST FUNC DEC      */
        /*************************************/
        System.out.print("AST NODE FUNC DEC\n");

        /*****************************/
        /* RECURSIVELY PRINT KIDS   */
        /*****************************/
        if (returnType != null) returnType.printMe();
        if (typeList != null) typeList.printMe();
        if (stmtList != null) stmtList.printMe();

        /*********************************/
        /* Print to AST GRAPHVIZ DOT file */
        /*********************************/
        AstGraphviz.getInstance().logNode(
                serialNumber,
                String.format("FUNC DEC(%s)", name));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (returnType  != null) AstGraphviz.getInstance().logEdge(serialNumber, returnType.serialNumber);
        if (typeList    != null) AstGraphviz.getInstance().logEdge(serialNumber, typeList.serialNumber);
        if (stmtList    != null) AstGraphviz.getInstance().logEdge(serialNumber, stmtList.serialNumber);
    }
}