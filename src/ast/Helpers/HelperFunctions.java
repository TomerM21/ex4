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
            file_writer.write("ERROR(" + lineNumber + ")\n");
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

        // nil allowed only for class types
        if (source instanceof TypeNil) {
            return target instanceof TypeClass;
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