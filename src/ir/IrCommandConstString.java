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

public class IrCommandConstString extends IrCommand
{
	public Temp t;
	public String value;
	
	public IrCommandConstString(Temp t, String value)
	{
		this.t = t;
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "Temp_" + t.getSerialNumber() + " := \"" + value + "\"";
	}
}
