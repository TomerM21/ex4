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

public class IrCommandFieldLoad extends IrCommand
{
	public Temp dst;           // destination temp
	public Temp object;        // object temp
	public String fieldName;
	
	public IrCommandFieldLoad(Temp dst, Temp object, String fieldName)
	{
		this.dst = dst;
		this.object = object;
		this.fieldName = fieldName;
	}
	
	@Override
	public String toString() {
		return "Temp_" + dst.getSerialNumber() + " := Temp_" + object.getSerialNumber() + "." + fieldName;
	}
}
