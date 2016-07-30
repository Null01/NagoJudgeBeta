package edu.nagojudge.app.exceptions;

/**
 *
 * @author andresfelipegarciaduran
 */
public class NagoJudgeException extends Exception {

    public NagoJudgeException() {
    }

    public NagoJudgeException(String message) {
        super(message);
    }

    public NagoJudgeException(Throwable cause) {
        super(cause);
    }

    public NagoJudgeException(String message, Throwable cause) {
        super(message, cause);
    }

}
