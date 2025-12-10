package ast.Exp;

import ast.AstGraphviz;
import ast.AstNode;
import ast.AstNodeSerialNumber;
import types.TypeList;
import types.Type;



public class AstExpList extends AstNode
{
    public AstExp head;
    public AstExpList tail;

    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AstExpList(AstExp head, AstExpList tail)
    {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        serialNumber = AstNodeSerialNumber.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        if (tail != null) System.out.print("====================== expList -> exp expList\n");
        if (tail == null) System.out.print("====================== expList -> exp      \n");

        /*******************************/
        /* COPY INPUT DATA MEMBERS ... */
        /*******************************/
        this.head = head;
        this.tail = tail;
    }

    /******************************************************/
    /* The printing message for an expression list AST node */
    /******************************************************/
    public void printMe()
    {
        /**************************************/
        /* AST NODE TYPE = AST EXPRESSION LIST */
        /**************************************/
        System.out.print("AST NODE EXP LIST\n");
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
            "EXP\nLIST\n"); 
        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (head != null) AstGraphviz.getInstance().logEdge(serialNumber,head.serialNumber);
        if (tail != null) AstGraphviz.getInstance().logEdge(serialNumber,tail.serialNumber);
    } 
    @Override
    public Type SemantMe()
    {
        Type headType = null;
        TypeList tailType = null;

        // 1. Get the type of the current expression
        if (head != null) {
            headType = head.SemantMe();
        }

        // 2. Recursively get the list of the rest
        if (tail != null) {
            // We know the tail returns a TypeList, so we cast it
            tailType = (TypeList) tail.SemantMe();
        }

        // 3. Return the combined list
        return new TypeList(headType, tailType);
    }  
    
}
