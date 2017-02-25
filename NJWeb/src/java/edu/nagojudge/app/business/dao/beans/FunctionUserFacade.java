/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.app.business.dao.beans;

import edu.nagojudge.app.business.dao.entities.FunctionUser;
import edu.nagojudge.app.business.dao.entities.TypeUser;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.log4j.Logger;

/**
 *
 * @author andresfelipegarciaduran
 */
@Stateless
public class FunctionUserFacade extends AbstractFacade<FunctionUser> {

    private final Logger logger = Logger.getLogger(FunctionUserFacade.class);

    @PersistenceContext(unitName = "NJWebPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FunctionUserFacade() {
        super(FunctionUser.class);
    }

    public List<FunctionUser> findFunctionsAccessByRole(Long idType) throws Exception {

        EntityManager em = getEntityManager();
        List<FunctionUser> outcome = null;
        em.clear();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT t FROM ").append(TypeUser.class.getSimpleName()).append(" t JOIN t.functionUserList f ");
        sql.append(" WHERE t.idType = :idType ");
        Query query = em.createQuery(sql.toString()).setParameter("idType", idType).setMaxResults(1);
        List<TypeUser> typeUsers = (List<TypeUser>) query.getResultList();
        if (typeUsers == null || typeUsers.isEmpty()) {
            final String msgError = "NO EXISTE UN TIPO USUARIO TIPO [" + idType + "] ASOCIADO CON FUNCIONES";
            logger.error(msgError);
            throw new Exception(msgError);
        }
        outcome = new ArrayList<FunctionUser>(typeUsers.get(0).getFunctionUserList());
        return outcome;
    }

}
