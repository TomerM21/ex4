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

public class TempList
{
	public Temp head;
	public TempList tail;
	
	public TempList(Temp head, TempList tail)
	{
		this.head = head;
		this.tail = tail;
	}
	
	@Override
	public String toString() {
		if (tail == null) {
			return "Temp_" + head.getSerialNumber();
		}
		return "Temp_" + head.getSerialNumber() + ", " + tail.toString();
	}
}
