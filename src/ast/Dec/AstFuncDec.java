package ast.Dec;

import ast.AstGraphviz;
import ast.AstNodeSerialNumber;
import ast.AstType;
import ast.AstTypeList;
import ast.Helpers.HelperFunctions;
import ast.Stmt.AstStmtList;
import symboltable.SymbolTable;
import types.*;

public class AstFuncDec extends AstDec {
    private AstType returnType;
    private String name;
    private AstTypeList typeList;
    private AstStmtList stmtList;

    public AstFuncDec(AstType returnType, String name, AstTypeList typeList, AstStmtList stmtList)
    {
        serialNumber = AstNodeSerialNumber.getFresh();

        if (typeList != null)
            System.out.print("====================== funcDec -> type ID LPAREN typeList RPAREN LBRACE stmtList RBRACE\n");
        else
            System.out.print("====================== funcDec -> type ID LPAREN RPAREN LBRACE stmtList RBRACE\n");

        this.returnType = returnType;
        this.name = name;
        this.typeList = typeList;
        this.stmtList = stmtList;
    }

    public void printMe()
    {
        /*************************************/
        /* AST NODE TYPE = AST FUNC DEC      */
        /*************************************/
        System.out.print("AST NODE FUNC DEC\n");

        /*****************************/
        /* RECURSIVELY PRINT KIDS   */
        /*****************************/
        if (returnType != null) returnType.printMe();
        if (typeList != null) typeList.printMe();
        if (stmtList != null) stmtList.printMe();

        /*********************************/
        /* Print to AST GRAPHVIZ DOT file */
        /*********************************/
        AstGraphviz.getInstance().logNode(
                serialNumber,
                String.format("FUNC DEC(%s)", name));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (returnType  != null) AstGraphviz.getInstance().logEdge(serialNumber, returnType.serialNumber);
        if (typeList    != null) AstGraphviz.getInstance().logEdge(serialNumber, typeList.serialNumber);
        if (stmtList    != null) AstGraphviz.getInstance().logEdge(serialNumber, stmtList.serialNumber);
    }
    @Override
    public Type SemantMe() {
        SymbolTable tbl = SymbolTable.getInstance();

        // 1. Resolve Return Type
        Type retType = returnType.SemantMe();
        if (retType == null) error();

        // 2. Check for Shadowing (Function name must be unique)
        if (HelperFunctions.existsInCurrentScope(name)) {
            HelperFunctions.printErrorAndExit(returnType.lineNumber);
        }

        // 3. Validate Parameter Types and Build the signature list
        TypeList params = null;
        if (typeList != null) {
            // First, validate all parameter types before building TypeList
            AstTypeList it = typeList;
            while (it != null) {
                Type paramType = it.type.SemantMe();
                
                // Validate parameter type (cannot be void)
                if (paramType instanceof TypeVoid) {
                    HelperFunctions.printErrorAndExit(it.type.lineNumber);
                }
                
                it = it.tail;
            }
            
            // Now build the TypeList (all params are valid)
            params = (TypeList) typeList.SemantMe();
        }

        // 4. Create the Function Type Wrapper
        TypeFunction funcType = new TypeFunction(retType, name, params);
        funcType.lineNumber = returnType.lineNumber;  // Use return type's line number (same as function declaration)

        // 5. Enter Function into Symbol Table (Before body, allowing recursion)
        tbl.enter(name, funcType);

        // 6. Begin Scope for Function Body
        tbl.beginScope();

        // 7. Register Parameters in the New Scope
        AstTypeList it = typeList;
        while (it != null) {
            Type paramType = it.type.SemantMe();
            
            // Check for duplicate parameter names
            if (HelperFunctions.existsInCurrentScope(it.name)) {
                this.lineNumber = it.type.lineNumber;
                error();
            }

            // Enter param into the local function scope
            tbl.enter(it.name, paramType);
            
            it = it.tail;
        }

        // --- NEW ADDITION START ---
        // Store the expected return type in the SymbolTable so return statements can check it
        tbl.currentFunctionReturnType = retType;
        // --- NEW ADDITION END ---

        // 8. Process Function Body
        if (stmtList != null) {
            stmtList.SemantMe();
        }

        // 9. End Scope
        tbl.endScope();

        // 10. RETURN THE WRAPPER
        return funcType;
    }
}