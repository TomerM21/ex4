package ast.Exp;

import ast.AstGraphviz;
import ast.AstNodeSerialNumber;

public class AstExpNil extends AstExp {

    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AstExpNil()
    {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        serialNumber = AstNodeSerialNumber.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        System.out.print("====================== exp -> NIL\n");
    }

    /***************************************************/
    /* The printing message for a NIL exp AST node     */
    /***************************************************/
    public void printMe()
    {
        /*************************************/
        /* AST NODE TYPE = AST EXP NIL       */
        /*************************************/
        System.out.print("AST NODE NIL\n");

        /*****************************/
        /* NO CHILDREN TO PRINT     */
        /*****************************/

        /*********************************/
        /* Print to AST GRAPHVIZ DOT file */
        /*********************************/
        AstGraphviz.getInstance().logNode(
                serialNumber,
                "NIL");

        /****************************************/
        /* NO EDGES — LEAF NODE                 */
        /****************************************/
    }
}