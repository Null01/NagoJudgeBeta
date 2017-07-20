/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.live.web.utils.clients;

import edu.nagojudge.live.web.utils.FacesUtil;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.rs.security.oauth2.client.HttpRequestProperties;
import org.apache.cxf.rs.security.oauth2.client.OAuthClientUtils;
import org.apache.cxf.rs.security.oauth2.common.ClientAccessToken;
import org.apache.log4j.Logger;

/**
 *
 * @author andresfelipegarciaduran
 */
public class ClientService {

    private static final Logger logger = Logger.getLogger(ClientService.class);

    private static ClientService instance;

    public static ClientService getInstance() {
        if (instance == null) {
            instance = new ClientService();
        }
        return instance;
    }

    public Object callRestfulGet(String BASE_ADDRESS_RESTFUL_NJ, String path, Object[] objects, Map<String, Object> params, Class t) {
        try {
            logger.debug("INICIA METODO - callRestfulGet()");
            logger.debug("BASE_ADDRESS_RESTFUL_NJ [" + BASE_ADDRESS_RESTFUL_NJ + "]");
            logger.debug("path [" + path + "]");
            logger.debug("params [" + params + "]");
            if (objects != null) {
                logger.debug("objects [" + Arrays.toString(objects) + "]");
            }
            if (t != null) {
                logger.debug("class [" + t.getName() + "]");
            }

            WebClient client = WebClient.create(BASE_ADDRESS_RESTFUL_NJ, false)
                    .path(path, objects)
                    .accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_OCTET_STREAM)
                    .type(MediaType.APPLICATION_JSON);
            if (params != null) {
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    client.query(entry.getKey(), entry.getValue());
                }
            }
            /*
            // represents client registration
            OAuthClientUtils.Consumer consumer = getConsumer();
            // the token issued by AccessTokenService
            ClientAccessToken token = getToken();

            HttpRequestProperties httpProps = new HttpRequestProperties(client, "GET");
            String authorizationHeader = OAuthClientUtils.createAuthorizationHeader(consumer, token, httpProps);
             client.header("Content-Type", MediaType.APPLICATION_JSON);
            client.header("Authorization", authorizationHeader);
             */
            return (t != null) ? client.get(t) : client.get();
        } finally {
            logger.debug("FINALIZA METODO - callRestfulGet()");
        }
    }

    public Object callRestfulGetList(String BASE_ADDRESS_RESTFUL_NJ, String path, Object[] objects, Map<String, Object> params, Class t) {
        try {
            logger.debug("INICIA METODO - callRestfulGetList()");
            logger.debug("BASE_ADDRESS_RESTFUL_NJ [" + BASE_ADDRESS_RESTFUL_NJ + "]");
            logger.debug("path [" + path + "]");
            logger.debug("params [" + params + "]");
            if (objects != null) {
                logger.debug("objects [" + Arrays.toString(objects) + "]");
            }
            if (t != null) {
                logger.debug("class [" + t.getName() + "]");
            }

            WebClient client = WebClient.create(BASE_ADDRESS_RESTFUL_NJ, false)
                    .path(path, objects)
                    .accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_OCTET_STREAM)
                    .type(MediaType.APPLICATION_JSON);
            if (params != null) {
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    client.query(entry.getKey(), entry.getValue());
                }
            }

            //client.header("Authorization", authorizationHeader);
            return (t != null) ? client.getCollection(t) : client.get();
        } finally {
            logger.debug("FINALIZA METODO - callRestfulGetList()");
        }
    }

}
