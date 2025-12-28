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

public class IrCommandArrayLoad extends IrCommand
{
	public Temp dst;        // destination temp
	public Temp array;      // array temp
	public Temp index;      // index temp
	
	public IrCommandArrayLoad(Temp dst, Temp array, Temp index)
	{
		this.dst = dst;
		this.array = array;
		this.index = index;
	}
	
	@Override
	public String toString() {
		return "Temp_" + dst.getSerialNumber() + " := Temp_" + array.getSerialNumber() + "[Temp_" + index.getSerialNumber() + "]";
	}
}
