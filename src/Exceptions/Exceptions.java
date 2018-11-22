/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Exceptions;

import java.lang.Exception;

/**
 *
 * @author plaza
 */
public class Exceptions extends Exception{
    
    public enum ERRORS{NO_SERVER_AVAILABLE, BITALINO_UNCONNECTED, WRONG_REGEXPRESSION};
    private ERRORS errorType;
    
    public Exceptions(ERRORS errorType){
        this.errorType = errorType;
    }

    public String toString() {
        
        switch(getErrorType()){
            case NO_SERVER_AVAILABLE:
                System.out.println("ecepions");
                return "Exceptions{" + "errorType=" + errorType + '}' + "Unable to write the objects on the server. Closing the socket.";
            
            case BITALINO_UNCONNECTED:
                return "Exceptions{" + "errorType=" + errorType + '}' + "Unable to connect with the BITalino.";
                
            default:
                return "An error occurred.";
        }
        
    }
    


    public ERRORS getErrorType() {
        return errorType;
    }
}
