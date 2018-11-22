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
public class Exceptions extends java.lang.Exception {

    private static final long serialVersionUID = 1L;

    public Exceptions(ErrorTypes errorType) {
        super(errorType.getName());
        code = errorType.getValue();
    }

    public int code;

}
