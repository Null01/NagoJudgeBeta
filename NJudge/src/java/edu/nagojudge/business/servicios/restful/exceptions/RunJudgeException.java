/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.business.servicios.restful.exceptions;

/**
 *
 * @author andres.garcia
 */
public class RunJudgeException extends Exception {

    public RunJudgeException() {
    }

    public RunJudgeException(String msg) {
        super(msg);
    }

    public RunJudgeException(Throwable cause) {
        super(cause);
    }

    public RunJudgeException(String message, Throwable cause) {
        super(message, cause);
    }

}
