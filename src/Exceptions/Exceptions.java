/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Exceptions;

//import java.lang.Exception;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import static patient.Patient.RegularExp;

/**
 *
 * @author plaza
 */
public class Exceptions extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final ErrorTypes errorType;

    public Exceptions(ErrorTypes errorType) {

        this.errorType = errorType;

    }

    public enum ErrorTypes {

        UNDEFINED, NO_SERVER_AVAILABLE, BITALINO_UNCONNECTED, WRONG_REGEXPRESSION
    };

    @Override
    public String toString() {
        String name = null;
        switch (geterror()) {
            case UNDEFINED:

                return "undefined";

            case NO_SERVER_AVAILABLE:
                return "no server available";

            case BITALINO_UNCONNECTED:
                return "Your Bitalino can not be connected...\n"
                        + "Please, try again";

            case WRONG_REGEXPRESSION:
                return "You have introduced a name or surname with invalid symbols...\n"
                        + "Please write a name or surname with symbols from A-Z ";

            default:
                return "_se produjo algun error";
        }
    }

    public ErrorTypes geterror() {
        return errorType;

    }

}
