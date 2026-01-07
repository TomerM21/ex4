package ast.Helpers;

import java.io.PrintWriter;
import symboltable.SymbolTable;
import types.*;

public class HelperFunctions {
    public static PrintWriter file_writer;

    public static void setFileWriter(PrintWriter writer) {
        file_writer = writer;
    }

    public static void printErrorAndExit(int lineNumber) {
        System.out.println("ERROR(" + lineNumber + ")");
        if (file_writer != null) {
            file_writer.write("ERROR(" + lineNumber + ")");
            file_writer.close();
        }
        System.exit(0);
    }

    public static boolean existsInCurrentScope(String name) {
        SymbolTable tbl = SymbolTable.getInstance();
        return tbl.findInScope(name) != null;
    }

    public static boolean canAssign(Type target, Type source) {
        // null source (i think shoulnt happen)
        if (source == null || target == null) return false;
        
        // same type
        if (target == source) return true;

        // nil allowed only for class types and arrays
        if (source instanceof TypeNil) {
            return (target instanceof TypeClass) || (target instanceof TypeArray);
        }

        // array type check - both must be arrays with compatible element types
        if (target instanceof TypeArray && source instanceof TypeArray) {
            TypeArray targetArray = (TypeArray) target;
            TypeArray sourceArray = (TypeArray) source;
            
            // If source is anonymous (no name, from "new int[]"), element types must match EXACTLY
            // Arrays are NOT covariant - new B[] cannot be assigned to A[] even if B extends A
            if (sourceArray.name == null || sourceArray.name.isEmpty()) {
                return targetArray.elementType == sourceArray.elementType;
            }
            
            // If both have names (named array types), they must be exactly the same type
            // gradesArray != IDsArray even if both are int[]
            return targetArray == sourceArray;
        }

        // inheritance check for class types
        if (target instanceof TypeClass && source instanceof TypeClass) {
            TypeClass src = (TypeClass) source;
            while (src != null) {
                if (src == target) return true;
                src = src.father;
            }
            return false;
        }

        // no other assignments allowed
        return false;
    }
    public static Type getTypeFromString(String typeName) {
        switch (typeName) {
            case "int":
                return TypeInt.getInstance(); //optional
            case "string":
                return TypeString.getInstance(); //optional
            case "void":
                return TypeVoid.getInstance(); //optional
            case "nil":
                return TypeNil.getInstance(); //optional
            default:
                return SymbolTable.getInstance().find(typeName);
        }
    }

}   