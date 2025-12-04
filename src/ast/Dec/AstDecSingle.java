package ast.Dec;

import ast.AstGraphviz;
import ast.AstNodeSerialNumber;

public class AstDecSingle extends AstDec {
    AstDec dec;

    public AstDecSingle(AstDec dec)
    {
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== dec -> varDec | funcDec | classDec | arrayTypeDef\n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.dec = dec;
	}

    /*************************************************/
	/* The printing message for a dec single AST node */
	/*************************************************/
	public void printMe()
	{
        /*************************************/
        /* AST NODE TYPE = AST DEC SINGLE */
        /*************************************/
        System.out.print("AST NODE DEC SINGLE\n");

        /*****************************/
        /* RECURSIVELY PRINT dec ... */
        /*****************************/
        if (dec != null) dec.printMe();

        /*********************************/
        /* Print to AST GRAPHVIZ DOT file */
        /*********************************/
        AstGraphviz.getInstance().logNode(
                serialNumber,
                "DEC\nSINGLE");

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (dec != null) AstGraphviz.getInstance().logEdge(serialNumber, dec.serialNumber);
	}
}
