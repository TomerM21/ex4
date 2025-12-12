   
import java.io.*;
import java.io.PrintWriter;
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
		Symbol s;
		AstProgram ast;
		FileReader fileReader;
		PrintWriter fileWriter;
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
			ast.printMe();

			/**************************/
			/* [7] Semant the AST ... */
			/**************************/
			ast.SemantMe();
			
			/****************************************/
			/* [8] Write OK if no errors occurred  */
			/****************************************/
			fileWriter.write("OK\n");
			
			/*************************/
			/* [9] Close output file */
			/*************************/
			fileWriter.close();

			/*************************************/
			/* [10] Finalize AST GRAPHIZ DOT file */
			/*************************************/
			AstGraphviz.getInstance().finalizeFile();
    	}
			     
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}


