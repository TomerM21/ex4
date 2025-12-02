package ast;

public class AstVarDec extends AstDec {
    private AstType type;  
    private String name;
    private AstExp exp;
    private AstNewExp newExp;

    public AstVarDec(AstType type, String name, AstExp exp, AstNewExp newExp)
    {
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
        if (exp != null)
            System.out.print("====================== varDec -> type ID ASSIGN exp SEMICOLON\n");
        else if (newExp != null)
            System.out.print("====================== varDec -> type ID ASSIGN newExp SEMICOLON\n");
        else
		    System.out.print("====================== varDec -> type ID SEMICOLON\n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.type = type;
        this.name = name;
        this.exp = exp;
        this.newExp = newExp;
	}

    /*************************************************/
    /* The printing message for a var dec AST node   */
    /*************************************************/
    public void printMe()
    {
        /*************************************/
        /* AST NODE TYPE = AST VAR DEC       */
        /*************************************/
        System.out.print("AST NODE VAR DEC\n");

        /*****************************/
        /* RECURSIVELY PRINT KIDS   */
        /*****************************/
        if (type != null) type.printMe();
        if (exp != null) exp.printMe();
        if (newExp != null) newExp.printMe();

        /*********************************/
        /* Print to AST GRAPHVIZ DOT file */
        /*********************************/
        AstGraphviz.getInstance().logNode(
                serialNumber,
                String.format("VAR DEC(%s)", name));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (type   != null) AstGraphviz.getInstance().logEdge(serialNumber, type.serialNumber);
        if (exp    != null) AstGraphviz.getInstance().logEdge(serialNumber, exp.serialNumber);
        if (newExp != null) AstGraphviz.getInstance().logEdge(serialNumber, newExp.serialNumber);
    }

    
}