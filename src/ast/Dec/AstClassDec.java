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
        if (extends_name != null)
            System.out.format("AST NODE CLASS DEC %s EXTENDS %s\n", name, extends_name);
        else
            System.out.format("AST NODE CLASS DEC %s\n", name);

        if (dataMemberList != null) dataMemberList.printMe();

        AstGraphviz.getInstance().logNode(
                serialNumber,
                String.format("CLASS DEC(%s)", name));

        if (dataMemberList != null)
            AstGraphviz.getInstance().logEdge(serialNumber, dataMemberList.serialNumber);
    }

    @Override
    public Type SemantMe() {
        SymbolTable tbl = SymbolTable.getInstance();

        // 1. Check if class name is already taken
        if (HelperFunctions.existsInCurrentScope(name)) {
            error();
        }

        // 2. Handle Inheritance (Father Class)
        TypeClass fatherType = null;
        if (extends_name != null) {
            Type t = tbl.find(extends_name);
            // Ensure the father exists and is actually a class
            if (t == null || !(t instanceof TypeClass)) {
                System.out.println(">> ERROR: Extended class not found or invalid");
                error();
            }
            fatherType = (TypeClass) t;
        }

        // 3. Create Class Shell
        TypeClass myClassType = new TypeClass(fatherType, name, null);

        // 4. Enter Shell into Symbol Table
        tbl.enter(name, myClassType);

        // 5. Begin Scope for Class Members
        tbl.beginScope();

        // 6. Process Members
        TypeList members = null;
        if (dataMemberList != null) {
            members = (TypeList) dataMemberList.SemantMe();
        }

        // 7. BACKFILL: Update TypeClass
        myClassType.dataMembers = members;

        // 8. Organize Members
        myClassType.splitMembers();

        // 9. CHECK INHERITANCE RULES (Shadowing/Overriding)
        if (fatherType != null) {
            checkInheritance(myClassType, fatherType);
        }

        // 10. End Scope
        tbl.endScope();

        return null;
    }

    private void checkInheritance(TypeClass myClass, TypeClass father) {
        
        // A. Check Fields (Variable Shadowing is ILLEGAL)
        for (String fieldName : myClass.fields.keySet()) {
            // Check if father has a field with this name
            if (father.lookupField(fieldName) != null) {
                System.out.format(">> ERROR: Field '%s' shadows a field in the superclass\n", fieldName);
                error();
            }
            // Check if father has a method with this name
            if (father.lookupMethod(fieldName) != null) {
                System.out.format(">> ERROR: Field '%s' shadows a method in the superclass\n", fieldName);
                error();
            }
        }

        // B. Check Methods (Overloading ILLEGAL, Overriding MUST MATCH SIGNATURE)
        for (String methodName : myClass.methods.keySet()) {
            TypeFunction myMethod = myClass.methods.get(methodName);

            // 1. Check if it shadows a FIELD in father
            if (father.lookupField(methodName) != null) {
                System.out.format(">> ERROR: Method '%s' shadows a field in the superclass\n", methodName);
                error();
            }

            // 2. Check Overriding
            TypeFunction fatherMethod = father.lookupMethod(methodName);
            if (fatherMethod != null) {
                // Return type must match
                if (myMethod.returnType != fatherMethod.returnType) {
                    System.out.format(">> ERROR: Method '%s' override has different return type\n", methodName);
                    error();
                }

                // Arguments must match exactly
                TypeList myParams = myMethod.params;
                TypeList fatherParams = fatherMethod.params;

                while (myParams != null && fatherParams != null) {
                    if (myParams.head != fatherParams.head) {
                        System.out.format(">> ERROR: Method '%s' override has different parameter types\n", methodName);
                        error();
                    }
                    myParams = myParams.tail;
                    fatherParams = fatherParams.tail;
                }

                // Check argument count mismatch
                if (myParams != null || fatherParams != null) {
                    System.out.format(">> ERROR: Method '%s' override has different number of parameters\n", methodName);
                    error();
                }
            }
        }
    }
}