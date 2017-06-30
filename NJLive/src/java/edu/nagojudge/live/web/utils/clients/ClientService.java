/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.live.web.utils.clients;

import edu.nagojudge.live.web.utils.FacesUtil;
import java.util.Arrays;
import java.util.Map;
import javax.ws.rs.core.MediaType;
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

    private final Logger logger = Logger.getLogger(ClientService.class);

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
            logger.debug("objects [" + Arrays.toString(objects) + "]");
            logger.debug("params [" + params + "]");
            logger.debug("class [" + t.getName() + "]");

            WebClient client = WebClient.create(BASE_ADDRESS_RESTFUL_NJ, false)
                    .path(path, objects)
                    .accept(MediaType.APPLICATION_JSON)
                    .type(MediaType.APPLICATION_JSON);
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                client.query(entry.getKey(), entry.getValue());
            }
            /*
// represents client registration
            OAuthClientUtils.Consumer consumer = getConsumer();
// the token issued by AccessTokenService
            ClientAccessToken token = getToken();

            HttpRequestProperties httpProps = new HttpRequestProperties(client, "GET");
            String authorizationHeader = OAuthClientUtils.createAuthorizationHeader(consumer, token, httpProps);

            client.header("Content-Type", MediaType.APPLICATION_JSON_TYPE);
            client.header("Authorization", authorizationHeader);
             */
            return client.get(t);
        } finally {
            logger.debug("FINALIZA METODO - callRestfulGet()");
        }
    }

    public Object callRestfulGetList(String BASE_ADDRESS_RESTFUL_NJ, String path, Object[] objects, Map<String, Object> params, Class t) {
        try {
            logger.debug("INICIA METODO - callRestfulGetList()");
            logger.debug("BASE_ADDRESS_RESTFUL_NJ [" + BASE_ADDRESS_RESTFUL_NJ + "]");
            logger.debug("path [" + path + "]");
            logger.debug("objects [" + Arrays.toString(objects) + "]");
            logger.debug("class [" + t.getName() + "]");
            logger.debug("params [" + params + "]");

            WebClient client = WebClient.create(BASE_ADDRESS_RESTFUL_NJ, false)
                    .path(path, objects)
                    .accept(MediaType.APPLICATION_JSON)
                    .type(MediaType.APPLICATION_JSON);

            client.header("Content-Type", MediaType.APPLICATION_JSON_TYPE);
            //client.header("Authorization", authorizationHeader);

            return client.getCollection(t);
        } finally {
            logger.debug("FINALIZA METODO - callRestfulGetList()");
        }

    }

    /*
     public static void main(String args[]) {
     String email = "agarcia@ucentral.edu.co";
     int submitView = 1;
     String TOKEN = "asd";
     String path = "submit/evaluate/{email}/{idSubmit}";
     Object objects[] = {String.valueOf(email), String.valueOf(submitView)};
     Map<String, Object> params = new HashMap<String, Object>();
     params.put("token", TOKEN);
     Object callRestfulGet = ClientService.getInstance().callRestfulGet(path, objects, params, ResponseMessage.class);
     System.out.println(callRestfulGet);
     }
     */
}
