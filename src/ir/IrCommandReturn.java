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

public class IrCommandReturn extends IrCommand
{
	public Temp returnValue; // null for void return
	
	public IrCommandReturn(Temp returnValue)
	{
		this.returnValue = returnValue;
	}
	
	@Override
	public String toString() {
		if (returnValue == null) {
			return "return";
		}
		return "return Temp_" + returnValue.getSerialNumber();
	}
}
