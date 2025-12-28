package ast.Stmt;

import ast.AstGraphviz;
import ast.AstNodeSerialNumber;
import ast.Exp.AstExp;
import symboltable.SymbolTable;
import types.Type;
import temp.*;
import ir.*;

public class AstStmtWhile extends AstStmt
{
	public AstExp cond;
	public AstStmtList body;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AstStmtWhile(AstExp cond, AstStmtList body)
	{
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        serialNumber = AstNodeSerialNumber.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        System.out.print("====================== stmt -> WHILE (exp) { stmtList }\n");

        this.cond = cond;
        this.body = body;
	}

    /***************************************************/
    /* The printing message for a WHILE stmt AST node  */
    /***************************************************/
    public void printMe()
    {
        /*************************************/
        /* AST NODE TYPE = AST STMT WHILE    */
        /*************************************/
        System.out.print("AST NODE WHILE STMT\n");

        /*****************************/
        /* RECURSIVELY PRINT KIDS   */
        /*****************************/
        if (cond != null) cond.printMe();
        if (body != null) body.printMe();

        /*********************************/
        /* Print to AST GRAPHVIZ DOT file */
        /*********************************/
        AstGraphviz.getInstance().logNode(
                serialNumber,
                "WHILE\nSTMT");

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (cond != null)
            AstGraphviz.getInstance().logEdge(serialNumber, cond.serialNumber);
        if (body != null)
            AstGraphviz.getInstance().logEdge(serialNumber, body.serialNumber);
    }
    @Override
    public Type SemantMe() {
        Type condType = cond.SemantMe();
        if (!condType.isInt()) {
            System.out.println(">> ERROR: While condition must be int");
            this.lineNumber = cond.lineNumber;
            error();
        }
        
        SymbolTable.getInstance().beginScope();
        if (body != null) body.SemantMe();
        SymbolTable.getInstance().endScope();
        
        return null;
    }

    public Temp irMe()
    {
        /*******************************/
        /* [1] Allocate 2 fresh labels */
        /*******************************/
        String labelEnd = IrCommand.getFreshLabel("end_while");
        String labelStart = IrCommand.getFreshLabel("start_while");

        /*********************************/
        /* [2] entry label for the while */
        /*********************************/
        Ir.getInstance().AddIrCommand(new IrCommandLabel(labelStart));

        /********************/
        /* [3] cond.irMe(); */
        /********************/
        Temp condTemp = cond.irMe();

        /******************************************/
        /* [4] Jump conditionally to the loop end */
        /******************************************/
        Ir.getInstance().AddIrCommand(new IrCommandJumpIfEqToZero(condTemp, labelEnd));

        /*******************/
        /* [5] body.irMe() */
        /*******************/
        if (body != null) body.irMe();

        /******************************/
        /* [6] Jump to the loop entry */
        /******************************/
        Ir.getInstance().AddIrCommand(new IrCommandJumpLabel(labelStart));

        /**********************/
        /* [7] Loop end label */
        /**********************/
        Ir.getInstance().AddIrCommand(new IrCommandLabel(labelEnd));

        /*******************/
        /* [8] return null */
        /*******************/
        return null;
    }
}