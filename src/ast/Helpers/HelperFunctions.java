package ast.Helpers;

import java.io.PrintWriter;

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
}
