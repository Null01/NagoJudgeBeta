/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.app.business.dao.beans;

import edu.nagojudge.app.business.dao.entities.FunctionUser;
import edu.nagojudge.app.business.dao.entities.TypeUser;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author andresfelipegarciaduran
 */
@Stateless
public class FunctionUserFacadeDAO extends AbstractFacade<FunctionUser> {

    @PersistenceContext(unitName = "NJWebPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FunctionUserFacadeDAO() {
        super(FunctionUser.class);
    }

    public List<FunctionUser> findFunctionsAccessByRole(Long idType) throws Exception {
        EntityManager em = getEntityManager();
        List<FunctionUser> outcome = null;
        try {
            em.clear();
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT t FROM ").append(TypeUser.class.getSimpleName()).append(" t JOIN t.functionUserList f ");
            sql.append(" WHERE t.idType = :idType ");
            Query query = em.createQuery(sql.toString()).setParameter("idType", idType);
            List<TypeUser> typeUsers = (List<TypeUser>) query.getResultList();
            if (!typeUsers.isEmpty()) {
                outcome = typeUsers.get(0).getFunctionUserList();
            }
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
        return outcome;
    }

}
