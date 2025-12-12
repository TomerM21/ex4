package ast;

import types.Type;
import types.TypeList;

import ast.Helpers.AstList;

public class AstTypeList extends AstNode {

    public AstType type;
    public String name;
    public AstTypeList tail;

    public AstTypeList(AstType type, String name, AstTypeList tail)
    {
        serialNumber = AstNodeSerialNumber.getFresh();

        if (tail != null)
            System.out.print("====================== typeList -> type ID COMMA typeList\n");
        else
            System.out.print("====================== typeList -> type ID\n");

        this.type = type;
        this.name = name;
        this.tail = tail;
    }

    public void printMe()
    {
        /*************************************/
        /* AST NODE TYPE = AST TYPE LIST     */
        /*************************************/
        System.out.print("AST NODE TYPE LIST\n");

        /*****************************/
        /* RECURSIVELY PRINT KIDS   */
        /*****************************/
        if (type != null) type.printMe();
        if (tail != null) tail.printMe();

        /*********************************/
        /* Print to AST GRAPHVIZ DOT file */
        /*********************************/
        AstGraphviz.getInstance().logNode(
                serialNumber,
                String.format("TYPE LIST(%s)", name));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (type != null) AstGraphviz.getInstance().logEdge(serialNumber, type.serialNumber);
        if (tail != null) AstGraphviz.getInstance().logEdge(serialNumber, tail.serialNumber);
    }

    @Override
    public Type SemantMe() {

        Type headType = null;
        TypeList tailList = null;

        if (type != null)
            headType = type.SemantMe();

        if (tail != null)
            tailList = (TypeList) tail.SemantMe();

        return new TypeList(headType, tailList);
    }

}