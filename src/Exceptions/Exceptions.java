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
public class Exceptions extends RunTimeException{
    
    public enum ERRORS{NO_SERVER_AVAILABLE, BITALINO_UNCONNECTED};
    private ERRORS errorType;
    
    
    
}
