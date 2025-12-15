package ast.Dec;

import ast.Stmt.AstStmt;
import types.Type;

public abstract class AstDec extends AstStmt {
    @Override
    public abstract Type SemantMe();
}
