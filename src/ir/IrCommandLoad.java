/***********/
/* PACKAGE */
/***********/
package ir;

import java.util.*;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/
import temp.*;

public class IrCommandLoad extends IrCommand
{
	Temp dst;
	String varName;
	
	public IrCommandLoad(Temp dst, String varName)
	{
		this.dst      = dst;
		this.varName = varName;
	}
	
	@Override
	public String toString() {
		return "Temp_" + dst.getSerialNumber() + " := " + varName;
	}

    @Override
    public Set<String> getReadVariables() {
        Set<String> s = new HashSet<>();
        if (varName != null) s.add(varName);
        return s;
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
