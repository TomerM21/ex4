package ast.Dec;

import ast.AstCFieldList;
import ast.AstGraphviz;
import ast.AstNodeSerialNumber;
import ast.Helpers.HelperFunctions;
import symboltable.SymbolTable;
import types.*;

public class AstClassDec extends AstDec {
    private String name;
    private String extends_name;
    private AstCFieldList dataMemberList;

    public AstClassDec(String name, String extends_name, AstCFieldList cFieldList) 
    {
        serialNumber = AstNodeSerialNumber.getFresh();

        if (extends_name != null)
            System.out.print("====================== classDec -> CLASS ID EXTENDS ID LBRACE cFieldList RBRACE\n");
        else
            System.out.print("====================== classDec -> CLASS ID LBRACE cFieldList RBRACE\n");

        this.name = name;
        this.extends_name = extends_name;
        this.dataMemberList = cFieldList;
    }

    public void printMe()
    {
        /*************************************/
        /* AST NODE TYPE = AST CLASS DEC      */
        /*************************************/
        if (extends_name != null)
            System.out.format("AST NODE CLASS DEC %s EXTENDS %s\n", name, extends_name);
        else
            System.out.format("AST NODE CLASS DEC %s\n", name);

        /*****************************/
        /* RECURSIVELY PRINT KIDS   */
        /*****************************/
        if (dataMemberList != null) dataMemberList.printMe();

        /*********************************/
        /* Print to AST GRAPHVIZ DOT file */
        /*********************************/
        AstGraphviz.getInstance().logNode(
                serialNumber,
                String.format("CLASS DEC(%s)", name));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (dataMemberList != null)
            AstGraphviz.getInstance().logEdge(serialNumber, dataMemberList.serialNumber);
    }

    @Override
    public Type SemantMe() {
        SymbolTable tbl = SymbolTable.getInstance();

        // check that class name doesnt
        if (HelperFunctions.existsInCurrentScope(name)) {
            error();
        }

        // handle extends
        TypeClass fatherType = null;
        if (extends_name != null) {
            Type t = tbl.find(extends_name);
            if (!(t instanceof TypeClass)) {
                error();
            }

            fatherType = (TypeClass) t;
        }

        // create class type and enter to symbol table so we can reference it recursively inside class
        TypeClass myClassType = new TypeClass(fatherType, name, null);
        tbl.enter(name, myClassType);

        // begin new scope for class fields
        tbl.beginScope();

        // semant data members
        TypeList members = null;
        if (dataMemberList != null) {
            members = (TypeList) dataMemberList.SemantMe();
        }

        // attach to class
        myClassType.dataMembers = members;

        // end scope for class fields
        tbl.endScope();

        return myClassType; 
    }
}