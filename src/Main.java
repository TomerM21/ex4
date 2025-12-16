import java.io.*;
import java_cup.runtime.Symbol;
import ast.*;
import ast.Helpers.HelperFunctions;
//import ast.Dec.AstDecList;

public class Main
{
	static public void main(String argv[])
	{
		Lexer l;
		Parser p;
		AstProgram ast;
		FileReader fileReader = null;
		PrintWriter fileWriter = null;
		String inputFileName = argv[0];
		String outputFileName = argv[1];
		
		try
		{
			/********************************/
			/* [1] Initialize a file reader */
			/********************************/
			fileReader = new FileReader(inputFileName);

			/********************************/
			/* [2] Initialize a file writer */
			/********************************/
			fileWriter = new PrintWriter(outputFileName);
			
			/****************************************/
			/* [2.5] Set file writer in HelperFunctions */
			/****************************************/
			HelperFunctions.setFileWriter(fileWriter);
			
			/******************************/
			/* [3] Initialize a new lexer */
			/******************************/
			l = new Lexer(fileReader);
			
			/*******************************/
			/* [4] Initialize a new parser */
			/*******************************/
			p = new Parser(l, fileWriter);

			/***********************************/
			/* [5] 3 ... 2 ... 1 ... Parse !!! */
			/***********************************/
			//ast = (AstDecList) p.parse().value;
			ast = (AstProgram) p.parse().value;
			
			/*************************/
			/* [6] Print the AST ... */
			/*************************/
			// ast.printMe();

			/**************************/
			/* [7] Semant the AST ... */
			/**************************/
			ast.SemantMe();
			
			/****************************************/
			/* [8] Write OK if no errors occurred  */
			/****************************************/
			fileWriter.write("OK");
			
			/*************************************/
			/* [9] Finalize AST GRAPHIZ DOT file */
			/*************************************/
			// AstGraphviz.getInstance().finalizeFile();
    	}
			     
		catch (Exception e)
		{
			e.printStackTrace();
			fileWriter.write("ERROR");
		}

		finally 
		{
            // ALWAYS close the file to flush the buffer
            if (fileWriter != null) {
                fileWriter.close();
            }
            try {
                if (fileReader != null) {
                    fileReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
	}
}

