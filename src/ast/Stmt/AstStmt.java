package ast.Stmt;

import ast.AstNode;
import temp.Temp;

public abstract class AstStmt extends AstNode
{
	public abstract Temp irMe();
}
