package types;

public class TypeClassVarDec extends Type
{
	public Type t;
	public int lineNumber;
	
	public TypeClassVarDec(Type t, String name)
	{
		this.t = t;
		this.name = name;
		this.lineNumber = 0;
	}
}
