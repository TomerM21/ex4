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

public class IrCommandArrayStore extends IrCommand
{
	public Temp array;      // array temp
	public Temp index;      // index temp
	public Temp value;      // value to store
	
	public IrCommandArrayStore(Temp array, Temp index, Temp value)
	{
		this.array = array;
		this.index = index;
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "Temp_" + array.getSerialNumber() + "[Temp_" + index.getSerialNumber() + "] := Temp_" + value.getSerialNumber();
	}
}
