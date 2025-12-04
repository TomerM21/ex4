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

        Type headT = null;
        TypeList tailT = null;
        if (head != null)
            headT = head.SemantMe();
        if (tail != null)
            tailT = tail.SemantMe();

        return new TypeList(headT,tailT);
    }

    @Override
    public Iterator<AstNode> iterator() {
        return new AstListIterator(this);
    }

}