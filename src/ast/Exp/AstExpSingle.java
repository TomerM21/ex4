package ast.Exp;

import ast.AstGraphviz;
import ast.AstNodeSerialNumber;
import types.Type;

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
    @Override
    public Type SemantMe() {
        return exp.SemantMe();
    }

    public temp.Temp irMe()
    {
        // Just a wrapper - delegate to the wrapped expression
        if (exp != null) return exp.irMe();
        return null;
    }
}