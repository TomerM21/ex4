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

public class IrCommandFieldStore extends IrCommand
{
	public Temp object;        // object temp
	public String fieldName;
	public Temp value;         // value to store
	
	public IrCommandFieldStore(Temp object, String fieldName, Temp value)
	{
		this.object = object;
		this.fieldName = fieldName;
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "Temp_" + object.getSerialNumber() + "." + fieldName + " := Temp_" + value.getSerialNumber();
	}
}
