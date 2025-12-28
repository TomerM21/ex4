package ast.Exp;

import ast.AstNode;
import temp.Temp;

public abstract class AstExp extends AstNode
{
	public abstract Temp irMe();
}