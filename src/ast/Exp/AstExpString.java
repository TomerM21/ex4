package ast.Exp;

import ast.AstGraphviz;
import ast.AstNodeSerialNumber;
import types.TypeString;
import types.Type;

public class AstExpString extends AstExp {

    public String value;

    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AstExpString(String value)
    {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        serialNumber = AstNodeSerialNumber.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        System.out.print("====================== exp -> STRING\n");

        /*******************************/
        /* COPY INPUT DATA MEMBERS ... */
        /*******************************/
        this.value = value;
    }

    /***************************************************/
    /* The printing message for a string exp AST node  */
    /***************************************************/
    public void printMe()
    {
        /*************************************/
        /* AST NODE TYPE = AST EXP STRING     */
        /*************************************/
        System.out.format("AST NODE STRING(%s)\n", value);

        /*****************************/
        /* NO CHILDREN TO PRINT     */
        /*****************************/

        /*********************************/
        /* Print to AST GRAPHVIZ DOT file */
        /*********************************/
        AstGraphviz.getInstance().logNode(
                serialNumber,
                String.format("STRING(%s)", value));

        /****************************************/
        /* NO EDGES — LEAF NODE                 */
        /****************************************/
    }
    @Override
    public Type SemantMe() {
        return TypeString.getInstance();
}
}