/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.business.app.listeners;

import java.io.File;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.log4j.PropertyConfigurator;

/**
 * Web application lifecycle listener.
 *
 * @author andresfelipegarciaduran
 */
public class InitializeAppListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent contextEvent) {
        System.out.println("InitializeAppListener - initialized");
        ServletContext context = contextEvent.getServletContext();
        String log4jConfigFile = context.getInitParameter("log4j-config");
        String fullPath = context.getRealPath("") + File.separator + log4jConfigFile;
        PropertyConfigurator.configure(fullPath);
    }

    @Override
    public void contextDestroyed(ServletContextEvent contextEvent) {
        System.out.println("InitializeAppListener - destroy");
    }
}
