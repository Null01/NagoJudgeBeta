/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.business.logic.exe;

import java.io.IOException;
import java.lang.reflect.Field;
import org.apache.log4j.Logger;

/*
 * Codejudge
 * Copyright 2012
 * Licensed under MIT License.
 */
public class TimedShell extends Thread {

    private final Logger logger = Logger.getLogger(TimedShell.class);

    private Process process;
    private long timeLimit;
    private boolean timeOut;

    public TimedShell(Process process, long timeLimit) {

        this.process = process;
        this.timeLimit = timeLimit;
    }

    @Override
    public void run() {
        try {
            sleep(timeLimit);
        } catch (InterruptedException ex) {
            logger.error(ex);
        }

        try {
            process.exitValue();
            setTimeOut(false);
        } catch (IllegalThreadStateException iex) {
            setTimeOut(true);
            logger.error(iex);
            try {
                killUnixProcess(process);
            } catch (IllegalArgumentException ex) {
                logger.error(ex);
            } catch (NoSuchFieldException ex) {
                logger.error(ex);
            } catch (SecurityException ex) {
                logger.error(ex);
            } catch (IllegalAccessException ex) {
                logger.error(ex);
            } catch (IOException ex) {
                logger.error(ex);
            }
        }
    }

    public boolean isTimeOut() {
        return timeOut;
    }

    public void setTimeOut(boolean timeOut) {
        this.timeOut = timeOut;
    }

    private void killUnixProcess(Process process)
            throws IllegalArgumentException, NoSuchFieldException,
            SecurityException, IllegalAccessException, IOException {
        if (process.getClass().getName().equals("java.lang.UNIXProcess")) {
            Class<?> cl = process.getClass();
            Field field = cl.getDeclaredField("pid");
            field.setAccessible(true);

            Object pidObject = field.get(process);
            Integer pid = (Integer) pidObject;

            String command = "kill -9 " + (pid + 3);
            Runtime.getRuntime().exec(command);
        } else {
            throw new IllegalArgumentException("Needs to be a UNIXProcess");
        }
    }

    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public long getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(long timeLimit) {
        this.timeLimit = timeLimit;
    }

}
