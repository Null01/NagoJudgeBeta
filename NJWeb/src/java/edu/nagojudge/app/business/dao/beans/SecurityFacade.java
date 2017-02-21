/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.app.business.dao.beans;

import edu.nagojudge.tools.security.constants.TypeSHAEnum;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.log4j.Logger;

/**
 *
 * @author andresfelipegarciaduran
 */
@Stateless
public class SecurityFacade implements Serializable {

    private final String MSG_ERROR_INVALID_KEY_PUB = "Public key corrupt, notify to administrator.";

    private final Logger logger = Logger.getLogger(SecurityFacade.class);

    @PersistenceContext(unitName = "NJWebPU")
    private EntityManager em;

    public EntityManager getEntityManager() {
        return em;
    }

    public String encodeSHA2(String string, TypeSHAEnum typeSHAEnum) {
        EntityManager entityManager = getEntityManager();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT SHA2('").append(string).append("', ").append(typeSHAEnum.getTypeSha()).append(") ");
        String outcome = (String) entityManager.createNativeQuery(sql.toString()).getSingleResult();
        logger.debug("encodeSHA2 [" + outcome + "]");
        return outcome;
    }

    public String decodeAESParameterNJ(String string) {
        if (string == null || string.isEmpty()) {
            return string;
        }
        EntityManager entityManager = getEntityManager();
        String keyPub = getPublicKey();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT AES_DECRYPT('").append(string).append("','").append(keyPub).append("')");
        Object object = entityManager.createNativeQuery(sql.toString()).getSingleResult();
        logger.debug("decodeAESParameterNJ [" + object + "]");
        return String.valueOf(object);
    }

    public Object encodeAESParameterNJ(String string) {
        if (string == null || string.isEmpty()) {
            return string;
        }
        EntityManager entityManager = getEntityManager();
        StringBuilder sql = new StringBuilder();
        String keyPub = getPublicKey();
        sql.append("SELECT AES_ENCRYPT('").append(string).append("','").append(keyPub).append("')");
        Object object = entityManager.createNativeQuery(sql.toString()).getSingleResult();
        logger.debug("encodeAESParameterNJ [" + object + "]");
        return object;
    }

    public String getPublicKey() {
        try {
            logger.debug("INICIO METODO - getPublicKey()");
            EntityManager entityManager = getEntityManager();
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT CONTENT_PARAMETER FROM PARAMETERS_NJ WHERE ID_PARAMETER = 'KEY-PUB'");
            List<Object> resultList = entityManager.createNativeQuery(sql.toString()).getResultList();
            if (resultList == null || resultList.isEmpty()) {
                throw new SecurityException(MSG_ERROR_INVALID_KEY_PUB);
            }
            String keyPub = new String((byte[]) resultList.get(0));
            logger.debug("getPublicKey [" + keyPub + "]");
            return keyPub;
        } finally {
            logger.debug("FIN METODO - getPublicKey()");
        }
    }

    public void overrideProperties(String fullPathFileConfig, Map<String, String> mapMatch) {

    }

}
