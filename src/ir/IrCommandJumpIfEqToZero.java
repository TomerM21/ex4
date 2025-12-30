/***********/
/* PACKAGE */
/***********/
package ir;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/
import temp.*;

public class IrCommandJumpIfEqToZero extends IrCommand
{
	Temp t;
	String labelName;
	
	public IrCommandJumpIfEqToZero(Temp t, String labelName)
	{
		this.t          = t;
		this.labelName = labelName;
	}

	@Override
    public boolean isJump() { return true; }

    @Override
    public boolean isUnconditionalJump() { return true; }

    @Override
    public String getJumpLabel() { return labelName; }
	
	@Override
	public String toString() {
		return "if Temp_" + t.getSerialNumber() + " == 0 goto " + labelName;
	}
}
