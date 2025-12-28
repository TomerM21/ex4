package ast.Stmt;

import ast.AstGraphviz;
import ast.AstNodeSerialNumber;
import ast.Exp.AstExp;
import symboltable.SymbolTable;
import types.Type;
import temp.*;
import ir.*;

public class AstStmtIfElse extends AstStmt
{
    public AstExp cond;
    public AstStmtList body;
    public AstStmtList elseBody;

    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AstStmtIfElse(AstExp cond, AstStmtList body, AstStmtList elseBody)
    {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        serialNumber = AstNodeSerialNumber.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        System.out.print("====================== stmt -> IF (exp) { stmtList } ELSE { stmtList }\n");

        /*******************************/
        /* COPY INPUT DATA MEMBERS ... */
        /*******************************/
        this.cond = cond;
        this.body = body;
        this.elseBody = elseBody;
    }

    /*************************************************/
    /* PRINT MESSAGE FOR IF-ELSE STMT AST NODE       */
    /*************************************************/
    public void printMe()
    {
        /*************************************/
        /* AST NODE TYPE = AST STMT IF-ELSE  */
        /*************************************/
        System.out.print("AST NODE IF-ELSE STMT\n");

        /*****************************/
        /* RECURSIVELY PRINT CHILDREN */
        /*****************************/
        if (cond != null) cond.printMe();
        if (body != null) body.printMe();
        if (elseBody != null) elseBody.printMe();

        /*********************************/
        /* PRINT Node to AST GRAPHVIZ     */
        /*********************************/
        AstGraphviz.getInstance().logNode(
                serialNumber,
                "IF-ELSE\nSTMT");

        /****************************************/
        /* PRINT EDGES to AST GRAPHVIZ DOT file */
        /****************************************/
        if (cond != null)
            AstGraphviz.getInstance().logEdge(serialNumber, cond.serialNumber);

        if (body != null)
            AstGraphviz.getInstance().logEdge(serialNumber, body.serialNumber);

        if (elseBody != null)
            AstGraphviz.getInstance().logEdge(serialNumber, elseBody.serialNumber);
    }
    @Override
    public Type SemantMe() {
        Type condType = cond.SemantMe();
        if (!condType.isInt()) {
            System.out.println(">> ERROR: If condition must be int");
            this.lineNumber = cond.lineNumber;
            error();
        }
        
        // If Body
        SymbolTable.getInstance().beginScope();
        if (body != null) body.SemantMe();
        SymbolTable.getInstance().endScope();

        // Else Body
        SymbolTable.getInstance().beginScope();
        if (elseBody != null) elseBody.SemantMe();
        SymbolTable.getInstance().endScope();
        
        return null;
    }

    public Temp irMe()
    {
        /*******************************/
        /* [1] Allocate fresh labels */
        /*******************************/
        String labelElse = IrCommand.getFreshLabel("else");
        String labelEnd = IrCommand.getFreshLabel("end_if");

        /****************************/
        /* [2] cond.irMe() */
        /****************************/
        Temp condTemp = cond.irMe();

        /*******************************************/
        /* [3] Jump to else if condition is false */
        /*******************************************/
        Ir.getInstance().AddIrCommand(new IrCommandJumpIfEqToZero(condTemp, labelElse));

        /***********************/
        /* [4] if body.irMe() */
        /***********************/
        if (body != null) body.irMe();

        /**************************************/
        /* [5] Jump to end (skip else block) */
        /**************************************/
        Ir.getInstance().AddIrCommand(new IrCommandJumpLabel(labelEnd));

        /**********************/
        /* [6] Else label */
        /**********************/
        Ir.getInstance().AddIrCommand(new IrCommandLabel(labelElse));

        /*************************/
        /* [7] else body.irMe() */
        /*************************/
        if (elseBody != null) elseBody.irMe();

        /**********************/
        /* [8] End label */
        /**********************/
        Ir.getInstance().AddIrCommand(new IrCommandLabel(labelEnd));

        return null;
    }
}