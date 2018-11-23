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
public class Exceptions extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final ErrorTypes errorType;

    public Exceptions(ErrorTypes errorType) {

        this.errorType = errorType;

    }

    public enum ErrorTypes {

       NO_SERVER_AVAILABLE, BITALINO_UNCONNECTED, WRONG_REGEXPRESSION
    };

    @Override
    public String toString() {
        switch (geterror()) {
            case NO_SERVER_AVAILABLE:
                return "No server available";

            case BITALINO_UNCONNECTED:
                return "Your Bitalino can not be connected...\n"
                        + "Please, try again";

            case WRONG_REGEXPRESSION:
                return "You have introduced a name or surname with invalid symbols...\n"
                        + "Please write a name or surname with symbols from A-Z ";

            default:
                return "\nSome error occurred...";
        }
    }

    public ErrorTypes geterror() {
        return errorType;

    }

}
