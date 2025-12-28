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

public class IrCommandNewArray extends IrCommand
{
	public Temp dst;        // destination temp
	public Temp size;       // size temp
	public String typeName; // element type
	
	public IrCommandNewArray(Temp dst, Temp size, String typeName)
	{
		this.dst = dst;
		this.size = size;
		this.typeName = typeName;
	}
	
	@Override
	public String toString() {
		return "Temp_" + dst.getSerialNumber() + " := new " + typeName + "[Temp_" + size.getSerialNumber() + "]";
	}
}
