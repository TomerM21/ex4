package ast.Dec;

import ast.AstGraphviz;
import ast.AstNodeSerialNumber;
import ast.AstType;
import ast.Helpers.HelperFunctions;
import symboltable.SymbolTable;
import types.*;

public class AstArrayTypeDef extends AstDec {
    private String name;
    private AstType type;

    public AstArrayTypeDef(String name, AstType type)
    {
        serialNumber = AstNodeSerialNumber.getFresh();

        System.out.print("====================== arrayTypeDef -> type ID LBRACKET RBRACKET SEMICOLON\n");

        this.name = name;
        this.type = type;
    }

    public void printMe()
    {
        /*************************************/
        /* AST NODE TYPE = AST ARRAY TYPE DEF */
        /*************************************/
        System.out.format("AST NODE ARRAY TYPE DEF %s\n", name);

        /*****************************/
        /* RECURSIVELY PRINT KIDS   */
        /*****************************/
        if (type != null) type.printMe();

        /*********************************/
        /* Print to AST GRAPHVIZ DOT file */
        /*********************************/
        AstGraphviz.getInstance().logNode(
                serialNumber,
                String.format("ARRAY TYPE DEF(%s)", name));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (type != null)
            AstGraphviz.getInstance().logEdge(serialNumber, type.serialNumber);
    }

    @Override
    public Type SemantMe()
    {   
        // inner type must exist
        Type inner = type.SemantMe();
        if (inner == null) {
            error();
        }

        // array element type cannot be void
        if (inner.isVoid()) {
            System.out.println(">> ERROR: Array element type cannot be void");
            this.lineNumber = type.lineNumber;
            error();
        }

        // name must be unique in current scope
        if (HelperFunctions.existsInCurrentScope(name)) {
            error();
        }

        // crate array type
        TypeArray arrType = new TypeArray(name, inner);

        // enter type to symbol table
        SymbolTable.getInstance().enter(name, arrType);

        return null;
    }

    public temp.Temp irMe()
    {
        // Array type definitions don't generate IR code
        // They're just type definitions used by the semantic analyzer
        return null;
    }
}