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

public class IrCommandStore extends IrCommand
{
	String varName;
	Temp src;
	
	public IrCommandStore(String varName, Temp src)
	{
		this.src      = src;
		this.varName = varName;
	}
	
	@Override
	public String toString() {
		return varName + " := Temp_" + src.getSerialNumber();
	}

    @Override
    public Set<String> getWriteVariables() {
        Set<String> s = new HashSet<>();
        if (varName != null) s.add(varName);
        return s;
    }
} 
