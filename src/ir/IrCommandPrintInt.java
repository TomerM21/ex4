/***********/
/* PACKAGE */
/***********/
package ir;

/*******************/
/* GENERAL IMPORTS */
/*******************/
import java.util.*;

/*******************/
/* PROJECT IMPORTS */
/*******************/
import temp.*;

public class IrCommandPrintInt extends IrCommand
{
	Temp t;
	
	public IrCommandPrintInt(Temp t)
	{
		this.t = t;
	}
	
	@Override
	public String toString() {
		return "PrintInt(Temp_" + t.getSerialNumber() + ")";
	}

	public Set<String> getReadTemps() {
		Set<String> result = new HashSet<>();
		result.add("Temp_" + t.getSerialNumber());
		return result;
	}

	public Set<String> getWriteTemps() {
		return new HashSet<>();
	}
}
