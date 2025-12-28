package ast;
import ast.Helpers.AstList;


public class AstCFieldList extends AstList {

    public AstCField head;
    public AstCFieldList tail;

    public AstCFieldList(AstCField head, AstCFieldList tail)
    {
        serialNumber = AstNodeSerialNumber.getFresh();

        if (tail != null)
            System.out.print("====================== cFieldList -> cField cFieldList\n");
        else
            System.out.print("====================== cFieldList -> cField\n");

        this.head = head;
        this.tail = tail;
    }

    public void printMe()
    {
        /*************************************/
        /* AST NODE TYPE = AST C-FIELD LIST  */
        /*************************************/
        System.out.print("AST NODE C-FIELD LIST\n");

        /*****************************/
        /* RECURSIVELY PRINT KIDS   */
        /*****************************/
        if (head != null) head.printMe();
        if (tail != null) tail.printMe();

        /*********************************/
        /* Print to AST GRAPHVIZ DOT file */
        /*********************************/
        AstGraphviz.getInstance().logNode(
                serialNumber,
                "C-FIELD\nLIST");

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (head != null) AstGraphviz.getInstance().logEdge(serialNumber, head.serialNumber);
        if (tail != null) AstGraphviz.getInstance().logEdge(serialNumber, tail.serialNumber);
    }

    @Override
    public AstNode getHead() {
        return head;
    }

    @Override
    public AstList getTail() {
        return tail;
    }

    public temp.Temp irMe()
    {
        // Process all class fields
        if (head != null) head.irMe();
        if (tail != null) tail.irMe();
        return null;
    }

    // Semant me of astList
}
