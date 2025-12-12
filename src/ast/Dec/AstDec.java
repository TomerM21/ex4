package ast.Dec;

import ast.AstNode;
import types.Type;

public abstract class AstDec extends AstNode {
    @Override
    public abstract Type SemantMe();
}
