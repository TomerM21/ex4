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

	public Set<String> getReadTemps() {
		return new HashSet<>();
	}

	public Set<String> getWriteTemps() {
		Set<String> result = new HashSet<>();
		result.add("Temp_" + dst.getSerialNumber());
		return result;
	}
}
