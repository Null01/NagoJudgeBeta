/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.app.business.dao.beans;

import edu.nagojudge.app.business.dao.entities.Submit;
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
public class SubmitFacadeDAO extends AbstractFacade<Submit> {

    @PersistenceContext(unitName = "NJWebPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SubmitFacadeDAO() {
        super(Submit.class);
    }

    public List<Submit> findLast100Results() {
        StringBuilder sql = new StringBuilder();
        int TOP = 100;
        sql.append("SELECT * FROM SUBMIT ORDER BY DATE_SUBMIT DESC LIMIT ").append(TOP);
        EntityManager em = getEntityManager();
        try {
            Query query = em.createNativeQuery(sql.toString(), Submit.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Submit> findSubmitEntitiesByAccount(long idAccount) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM SUBMIT WHERE ID_ACCOUNT = ? ORDER BY DATE_SUBMIT ");
        EntityManager em = getEntityManager();
        try {
            Query query = em.createNativeQuery(sql.toString(), Submit.class).setParameter(1, idAccount);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

}
