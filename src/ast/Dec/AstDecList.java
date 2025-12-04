package ast.Dec;

import ast.AstGraphviz;
import ast.AstNode;
import ast.AstNodeSerialNumber;

public class AstDecList extends AstNode extends AstList
{
    /****************/
    /* DATA MEMBERS */
    /****************/
    public AstDec head;
    public AstDecList tail;

    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AstDecList(AstDec head, AstDecList tail)
    {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        serialNumber = AstNodeSerialNumber.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        if (tail != null) System.out.print("====================== decList -> dec decList\n");
        if (tail == null) System.out.print("====================== decList -> dec\n");

        /*******************************/
        /* COPY INPUT DATA MEMBERS ... */
        /*******************************/
        this.head = head;
        this.tail = tail;

        left = head;
        right = tail;
    }

    /******************************************************/
    /* The printing message for a declaration list AST node */
    /******************************************************/
    public void printMe()
    {
        /**************************************/
        /* AST NODE TYPE = AST DEC LIST */
        /**************************************/
        System.out.print("AST NODE DEC LIST\n");

        /*************************************/
        /* RECURSIVELY PRINT HEAD + TAIL ... */
        /*************************************/
        if (head != null) head.printMe();
        if (tail != null) tail.printMe();

        /**********************************/
        /* PRINT to AST GRAPHVIZ DOT file */
        /**********************************/
        AstGraphviz.getInstance().logNode(
                serialNumber,
                "DEC\nLIST\n");

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (head != null) AstGraphviz.getInstance().logEdge(serialNumber, head.serialNumber);
        if (tail != null) AstGraphviz.getInstance().logEdge(serialNumber, tail.serialNumber);
    }

    @Override
    public AstDec getHead() {
        return this.head;
    }

    @Override
    public AstDecList getTail() {
        return this.tail;
    }

    
}