package ast.Helpers;

import java.util.Iterator;

import ast.AstNode;


public class AstListIterator implements Iterator<AstNode>{

    private AstList list;

    public AstListIterator(AstList list){
        this.list = list;
    }

    @Override
    public boolean hasNext() {
        return list != null;
    }

    @Override
    public AstNode next() {
        AstNode res = list.getHead();
        list = list.getTail();
        return res;
    }
}