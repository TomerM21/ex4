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

public class IrCommandCall extends IrCommand
{
	public Temp dst;           // where result goes (null for void functions)
	public String funcName;
	public TempList args;      // argument temporaries
	
	public IrCommandCall(Temp dst, String funcName, TempList args)
	{
		this.dst = dst;
		this.funcName = funcName;
		this.args = args;
	}
	
	@Override
	public String toString() {
		String argsStr = "";
		if (args != null) {
			argsStr = args.toString();
		}
		if (dst == null) {
			return "call " + funcName + "(" + argsStr + ")";
		}
		return "Temp_" + dst.getSerialNumber() + " := call " + funcName + "(" + argsStr + ")";
	}

	public Set<String> getReadTemps() {
		Set<String> result = new HashSet<>();
		TempList current = args;
		while (current != null) {
			result.add("Temp_" + current.head.getSerialNumber());
			current = current.tail;
		}
		return result;
	}

	public Set<String> getWriteTemps() {
		Set<String> result = new HashSet<>();
		if (dst != null) {
			result.add("Temp_" + dst.getSerialNumber());
		}
		return result;
	}
}
