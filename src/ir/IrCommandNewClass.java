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

public class IrCommandNewClass extends IrCommand
{
	public Temp dst;        // destination temp
	public String className;
	
	public IrCommandNewClass(Temp dst, String className)
	{
		this.dst = dst;
		this.className = className;
	}
	
	@Override
	public String toString() {
		return "Temp_" + dst.getSerialNumber() + " := new " + className + "()";
	}
}
