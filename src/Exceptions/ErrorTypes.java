/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Exceptions;

/**
 *
 * @author plaza
 */

public enum ErrorTypes {
        
        UNDEFINED (0,"UNDEFINED ERROR"),
        NO_SERVER_AVAILABLE (1, "Unable to write the objects on the server. Closing the socket."), 
        BITALINO_UNCONNECTED (2, "Unable to connect with the BITalino."), 
        WRONG_REGEXPRESSION (3, "You have introduced invalid symbols. Introduce characters from a to z.");


	private final int value;
	private final String name;
	ErrorTypes (int value, String name)
	{
	    this.value = value;
	    this.name = name;
	}
	
	public int getValue()
	{
	    return value;
	}
	
	public String getName()
	{
	    return name; 
	}
	
	public static final ErrorTypes getType(int val)
	{
	    for (ErrorTypes t : ErrorTypes.values())
	    {
	        if (t.getValue()==val)
	            return t;
	    }
	    return UNDEFINED;
	}
	
	public static final ErrorTypes getType(String val)
	{
	    for (ErrorTypes t : ErrorTypes.values())
	    {
	        if (t.getName().equals(val))
	            return t;
	    }
	    return UNDEFINED;
	}
}