package edu.nagojudge.app.exceptions;

import java.io.IOException;

/**
 *
 * @author andresfelipegarciaduran
 */
public class UtilNagoJudgeException extends Exception {

    public UtilNagoJudgeException() {
    }

    public UtilNagoJudgeException(String msg) {
        super(msg);
    }

    public UtilNagoJudgeException(String message, Throwable cause) {
        super(message, cause);
    }

    public UtilNagoJudgeException(Throwable ex) {
        super(ex);
    }

}
