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

public abstract class IrCommand
{
    /*****************/
    /* Label Factory */
    /*****************/
    protected static int labelCounter = 0;
    public    static String getFreshLabel(String msg)
    {
        return String.format("Label_%d_%s", labelCounter++,msg);
    }

    // By default, IR commands do not read or write named variables.
    // Subclasses that interact with named variables should override these.
    public Set<String> getReadVariables() {
        return new HashSet<>();
    }

    public Set<String> getWriteVariables() {
        return new HashSet<>();
	/***********************************/
	/* Abstract method for IR printing */
	/***********************************/
	public abstract String toString();
}
