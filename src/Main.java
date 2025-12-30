import java.io.*;
import java.util.*;
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
			
			/*********************************/
			/* [8] Generate IR from AST ...  */
			/*********************************/
			ast.irMe();//was written before tomerm edit
			
			// Get IR commands -tomerm edit from here
   			List<ir.IrCommand> irCommands = ir.Ir.getInstance().getCommandList();

			// Build CFG
			cfg.Fullcfggraph controlFlowGraph = cfg.Fullcfggraph.buildFromIR(irCommands);

			// Run dataflow analysis
			Set<String> uninitializedVars = controlFlowGraph.runDataflowAnalysis();

			// Output results
			if (uninitializedVars.isEmpty()) {
    		// No uninitialized variables detected
    		fileWriter.write("OK");
			} else {
    		// Sort and output each uninitialized variable on separate line
    		List<String> sortedVars = new ArrayList<>(uninitializedVars);
    		Collections.sort(sortedVars);
    		for (String var : sortedVars) {
        	fileWriter.write(var + "\n");/// up to here tomer edit
    }
}

			/****************************************/
			/* [9] Write IR to file for debugging   */
			/****************************************/
			String irOutputFile = outputFileName.replace("SemanticStatus.txt", "IR_Output.txt");
			try {
				ir.Ir.getInstance().printIrToFile(irOutputFile);
				System.out.println("IR output written to: " + irOutputFile);
			} catch (Exception irEx) {
				System.err.println("Failed to write IR output: " + irEx.getMessage());
			}
			
			/****************************************/
			/* [10] Write OK if no errors occurred  */
			/****************************************/
			fileWriter.write("OK");
			
			/*************************************/
			/* [11] Finalize AST GRAPHIZ DOT file */
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

