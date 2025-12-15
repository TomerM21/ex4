package ast.Helpers;

import types.Type;
import types.TypeList;
import ast.AstNode;

import java.util.Iterator;

public abstract class AstList extends AstNode implements Iterable<AstNode>{

    public abstract AstNode getHead();
    public abstract AstList getTail();

    @Override
    public TypeList SemantMe() {
        AstNode head = getHead();
        AstList tail = getTail();

        System.out.println("DEBUG AstList: Processing list, head class = " + (head != null ? head.getClass().getSimpleName() : "null"));
        Type headT = null;
        TypeList tailT = null;
        if (head != null) {
            System.out.println("DEBUG AstList: About to process head");
            headT = head.SemantMe();
            System.out.println("DEBUG AstList: Finished processing head");
        }
        if (tail != null) {
            System.out.println("DEBUG AstList: About to process tail");
            tailT = tail.SemantMe();
            System.out.println("DEBUG AstList: Finished processing tail");
        }

        return new TypeList(headT,tailT);
    }

    @Override
    public Iterator<AstNode> iterator() {
        return new AstListIterator(this);
    }

}