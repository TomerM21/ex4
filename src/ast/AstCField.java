package ast;

import ast.Dec.AstDec;
import types.*;  

public class AstCField extends AstNode {

    public AstDec dec;  

    public AstCField(AstDec dec)
    {
        serialNumber = AstNodeSerialNumber.getFresh();
        System.out.print("====================== cField -> varDec | funcDec\n");
        this.dec = dec;
    }

    public void printMe()
    {
        /*************************************/
        /* AST NODE TYPE = AST C-FIELD       */
        /*************************************/
        System.out.print("AST NODE C-FIELD\n");

        /*****************************/
        /* RECURSIVELY PRINT KIDS   */
        /*****************************/
        if (dec != null) dec.printMe();

        /*********************************/
        /* Print to AST GRAPHVIZ DOT file */
        /*********************************/
        AstGraphviz.getInstance().logNode(
                serialNumber,
                "C-FIELD");

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (dec != null)
            AstGraphviz.getInstance().logEdge(serialNumber, dec.serialNumber);
    }

    @Override
    public Type SemantMe() {
        return dec.SemantMe();   
    }
}