package ast;

import ast.Helpers.HelperFunctions;
import symboltable.SymbolTable;
import types.Type;
import types.TypeVoid;

public class AstType extends AstNode {
    
    public String typeName;

    public AstType(String typeName)
    {
        serialNumber = AstNodeSerialNumber.getFresh();

        System.out.print("====================== type -> " + typeName + "\n");

        this.typeName = typeName;
    }

    /*************************************************/
    /* The printing message for a type AST node       */
    /*************************************************/
    public void printMe()
    {
        System.out.format("AST NODE TYPE (%s)\n", typeName);

        /* no children to print */

        AstGraphviz.getInstance().logNode(
                serialNumber,
                String.format("TYPE(%s)", typeName)
        );
    }
    
    @Override
    public Type SemantMe()
    {
        Type type = HelperFunctions.getTypeFromString(typeName);
        if (type == null) {
            error();
        }
        return type;
    }
}