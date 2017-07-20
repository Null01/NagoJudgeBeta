/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.live.web.utils;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import org.apache.log4j.Logger;

/**
 *
 * @author andresfelipegarciaduran
 */
public class WebServiceUtil implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(WebServiceUtil.class);

    private static WebServiceUtil webServiceUtil;

    public static WebServiceUtil getWebServiceUtil() {
        if (webServiceUtil == null) {
            webServiceUtil = new WebServiceUtil();
        }
        return webServiceUtil;
    }

    public Service clientService(String urlString, String nameSapceURI, String nameClassService) throws MalformedURLException {
        Service service = null;
        try {
            URL url = new URL(urlString);
            QName qname = new QName(nameSapceURI, nameClassService);
            service = Service.create(url, qname);
        } catch (MalformedURLException ex) {
            LOGGER.error(ex);
            throw ex;
        }
        return service;
    }

}
