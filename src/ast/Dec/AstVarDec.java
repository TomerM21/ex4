package ast.Dec;

import ast.AstGraphviz;
import ast.AstNodeSerialNumber;
import ast.AstType;
import ast.Exp.AstExp;
import ast.Exp.AstNewExp;
import ast.Helpers.HelperFunctions;
import symboltable.SymbolTable;
import types.Type;
import types.TypeClassVarDec;
import types.TypeVoid;

public class AstVarDec extends AstDec {
    private AstType typeNode; // int, string, void, A, B, ... 
    private String name; // string name of the var
    private AstExp exp; // optional initialization expression
    private AstNewExp newExp; // optional new expression for class types

    public AstVarDec(AstType typeNode, String name, AstExp exp, AstNewExp newExp)
    {
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
        if (exp != null)
            System.out.print("====================== varDec -> type ID ASSIGN exp SEMICOLON\n");
        else if (newExp != null)
            System.out.print("====================== varDec -> type ID ASSIGN newExp SEMICOLON\n");
        else
		    System.out.print("====================== varDec -> type ID SEMICOLON\n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.typeNode = typeNode;
        this.name = name;
        this.exp = exp;
        this.newExp = newExp;
	}

    /*************************************************/
    /* The printing message for a var dec AST node   */
    /*************************************************/
    public void printMe()
    {
        /*************************************/
        /* AST NODE TYPE = AST VAR DEC       */
        /*************************************/
        System.out.print("AST NODE VAR DEC\n");

        /*****************************/
        /* RECURSIVELY PRINT KIDS   */
        /*****************************/
        if (typeNode != null) typeNode.printMe();
        if (exp != null) exp.printMe();
        if (newExp != null) newExp.printMe();

        /*********************************/
        /* Print to AST GRAPHVIZ DOT file */
        /*********************************/
        AstGraphviz.getInstance().logNode(
                serialNumber,
                String.format("VAR DEC(%s)", name));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (typeNode   != null) AstGraphviz.getInstance().logEdge(serialNumber, typeNode.serialNumber);
        if (exp    != null) AstGraphviz.getInstance().logEdge(serialNumber, exp.serialNumber);
        if (newExp != null) AstGraphviz.getInstance().logEdge(serialNumber, newExp.serialNumber);
    }
    
    @Override
    public Type SemantMe() {
        System.out.println("### SEMANT GOING (VAR DEC) ###");
        // 1. Resolve the declared type (e.g., "int", "string", "Person")
        Type varType = typeNode.SemantMe();

        // 2. Validate Type: Must exist and cannot be void
        if (varType == null || varType instanceof TypeVoid) {
            error();
        }

        // 3. Shadowing Check: Name must be unique in the current scope
        if (HelperFunctions.existsInCurrentScope(name)) {
            error();
        }

        // 4. Check Initialization Expression (if exists: int x := 5;)
        if (exp != null) {
            Type expType = exp.SemantMe();
            if (!HelperFunctions.canAssign(varType, expType)) {
                error();
            }
        }

        // 5. Check New Expression (if exists: Person p := new Person;)
        if (newExp != null) {
            Type newType = newExp.SemantMe();
            if (!HelperFunctions.canAssign(varType, newType)) {
                error();
            }
        }

        // 6. Enter Variable into Symbol Table (So subsequent code can use it)
        SymbolTable.getInstance().enter(name, varType);

        // 7. RETURN THE WRAPPER
        // This allows AstClassDec to know the name of this variable later.
        TypeClassVarDec varDecType = new TypeClassVarDec(varType, name);
        varDecType.lineNumber = this.lineNumber;  // Store the line number for error reporting
        return varDecType; 
    }
}
/*
varDec ::= type ID [ ASSIGN exp ] SEMICOLON
| type ID ASSIGN newExp SEMICOLON
 */