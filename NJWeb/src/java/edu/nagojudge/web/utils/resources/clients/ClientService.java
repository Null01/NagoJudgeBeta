/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.web.utils.resources.clients;

import java.util.Arrays;
import java.util.Collection;
import javax.ws.rs.core.MediaType;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.log4j.Logger;

/**
 *
 * @author andresfelipegarciaduran
 */
public class ClientService {

    private Logger logger = Logger.getLogger(ClientService.class);

    private final String BASE_ADDRESS_RESTFUL = "http://localhost:8484/ask";

    private static ClientService instance;

    public static ClientService getInstance() {
        if (instance == null) {
            instance = new ClientService();
        }
        return instance;
    }

    public Object callRestfulGet(String path, Object[] objects, Class t) {
        try {
            logger.debug("Inicia metodo - callRestfulGet()");
            logger.debug("path [" + path + "]");
            logger.debug("objects [" + Arrays.toString(objects) + "]");
            logger.debug("class [" + t.getName() + "]");
            WebClient client = WebClient.create(BASE_ADDRESS_RESTFUL)
                    .path(path, objects)
                    .accept(MediaType.APPLICATION_JSON)
                    .type(MediaType.APPLICATION_JSON);
            return client.get(t);
        } finally {
            logger.debug("Finaliza metodo - callRestfulGet()");
        }
    }

    public Collection callRestfulGetList(String path, Object[] objects, Class t) {
        try {
            logger.debug("Inicia metodo - callRestfulGetList()");
            logger.debug("path [" + path + "]");
            logger.debug("objects [" + Arrays.toString(objects) + "]");
            logger.debug("class [" + t.getName() + "]");
            WebClient client = WebClient.create(BASE_ADDRESS_RESTFUL)
                    .path(path, objects)
                    .accept(MediaType.APPLICATION_JSON)
                    .type(MediaType.APPLICATION_JSON);
            return client.getCollection(t);
        } finally {
            logger.debug("Finaliza metodo - callRestfulGetList()");
        }
    }

}
