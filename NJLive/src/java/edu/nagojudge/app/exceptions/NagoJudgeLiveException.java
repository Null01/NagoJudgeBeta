package edu.nagojudge.app.exceptions;

/**
 *
 * @author andresfelipegarciaduran
 */
public class NagoJudgeLiveException extends Exception {

    public NagoJudgeLiveException() {
    }

    public NagoJudgeLiveException(String message) {
        super(message);
    }

    public NagoJudgeLiveException(Throwable cause) {
        super(cause);
    }

    public NagoJudgeLiveException(String message, Throwable cause) {
        super(message, cause);
    }

}
