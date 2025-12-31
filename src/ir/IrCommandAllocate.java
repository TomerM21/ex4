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

public class IrCommandAllocate extends IrCommand
{
	String varName;
	
	public IrCommandAllocate(String varName)
	{
		this.varName = varName;
	}
	
	@Override
	public String toString() {
		return "allocate " + varName;
	}

    // Allocate creates a fresh UNINITIALIZED variable
    // This handles shadowing: a new declaration shadows previous ones
    @Override
    public Set<String> getWriteVariables() {
        Set<String> s = new HashSet<>();
        if (varName != null) s.add(varName);
        return s;
    }
    
    // Override: Allocate always writes UNINITIALIZED (special case)
    // We need to handle this in the transfer function
}
