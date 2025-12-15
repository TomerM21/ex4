package types;

public class TypeFunction extends Type
{
	/***********************************/
	/* The return type of the function */
	/***********************************/
	public Type returnType;

	/*************************/
	/* types of input params */
	/*************************/
	public TypeList params;
	
	/****************************/
	/* line number of declaration */
	/****************************/
	public int lineNumber;
	
	/****************/
	/* CTROR(S) ... */
	/****************/
	public TypeFunction(Type returnType, String name, TypeList params)
	{
		this.name = name;
		this.returnType = returnType;
		this.params = params;
		this.lineNumber = 0;
	}

	@Override
	public boolean isFunction()  { return true; }
}
