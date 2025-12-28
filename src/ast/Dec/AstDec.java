package ast.Dec;

import ast.Stmt.AstStmt;
import types.Type;
import temp.Temp;

public abstract class AstDec extends AstStmt {
    @Override
    public abstract Type SemantMe();
    
    @Override
    public abstract Temp irMe();
}
