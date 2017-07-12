/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.business.codejuge.logic.exe;

import edu.nagojudge.business.codejuge.language.AbstractLanguage;
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

    private AbstractLanguage parent;
    private Process p;
    private long time;

    public TimedShell(AbstractLanguage parent, Process p, long time) {
        this.parent = parent;
        this.p = p;
        this.time = time;
    }

    // Sleep until timeout and then terminate the process
    @Override
    public void run() {
        try {
            sleep(time);
        } catch (InterruptedException e) {
            logger.error(e);
        }
        try {
            p.exitValue();
            parent.isTimeout = false;
        } catch (IllegalThreadStateException e) {
            logger.error(e);
            parent.isTimeout = true;
            p.destroy();
        }
    }

    public AbstractLanguage getParent() {
        return parent;
    }

    public void setParent(AbstractLanguage parent) {
        this.parent = parent;
    }

    public Process getP() {
        return p;
    }

    public void setP(Process p) {
        this.p = p;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
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

}
