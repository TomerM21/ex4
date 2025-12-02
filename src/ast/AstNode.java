package ast;

public abstract class AstNode
{
	/*******************************************/
	/* The serial number is for debug purposes */
	/* In particular, it can help in creating  */
	/* a graphviz dot format of the AST ...    */
	/*******************************************/
	public int serialNumber;

	public static int currLine = 0; // TODO!!!
    public int myLine = currLine;

	public AstNode left = null;
    public AstNode right = null;


	/***********************************************/
	/* The default message for an unknown AST node */
	/***********************************************/
	public void printMe()
	{
		System.out.print("AST NODE UNKNOWN\n");
	}

	public AstNode getLeft() {
        return left;
    }

    public AstNode getRight(){
        return right;
    }

    public Type SemantMe() {return null;}
}
