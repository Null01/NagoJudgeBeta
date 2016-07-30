/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.app.business.dao.beans;

import edu.nagojudge.app.business.dao.entities.User;
import edu.nagojudge.app.exceptions.NagoJudgeException;
import edu.nagojudge.tools.security.constants.TypeSHAEnum;
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
public class UserFacadeDAO extends AbstractFacade<User> {

    @PersistenceContext(unitName = "NJWebPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserFacadeDAO() {
        super(User.class);
    }

    public User existUserRegistered(String email, String password) {
        EntityManager em = getEntityManager();
        User outcome = null;
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT SHA2('").append(password).append("', ").append(TypeSHAEnum.SHA256_NUM.getTypeSha()).append(") ");
        String passwordEncrypted = (String) em.createNativeQuery(sql.toString()).getSingleResult();
        sql = new StringBuilder();
        sql.append("SELECT p FROM ").append(User.class.getSimpleName()).append(" p WHERE p.idEmail = :email AND p.keyUser = :pass");
        Query query = em.createQuery(sql.toString()).setParameter("email", email).setParameter("pass", passwordEncrypted);
        List resultList = query.getResultList();
        if (resultList != null && resultList.size() == 1) {
            outcome = (User) resultList.get(0);
        }

        return outcome;
    }

    public void validateFieldsUnique(String email) throws NagoJudgeException {
        EntityManager em = getEntityManager();

        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT COUNT(0) FROM USER WHERE LOWER(ID_EMAIL) = LOWER('").append(email).append("')");
        Query query = em.createNativeQuery(sb.toString());
        int count = Integer.parseInt(query.getSingleResult().toString());
        if (count >= 1) {
            throw new NagoJudgeException(" EL EMAIL " + email + " YA ESTA REGISTRADO. ");
        }

    }

    public User findUserByIdAccount(long idAccount) {
        EntityManager em = getEntityManager();
        User outcome = null;

        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT p FROM ").append(User.class.getSimpleName()).append(" p WHERE p.idAccount.idAccount = :idAccount ");
        Query query = em.createQuery(sb.toString()).setParameter("idAccount", idAccount);
        outcome = (User) query.getResultList().get(0);

        return outcome;
    }

    public String encodeSHA2(String string, TypeSHAEnum typeSHAEnum) {
        EntityManager em = getEntityManager();
        String outcome = null;
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT SHA2('").append(string).append("', ").append(typeSHAEnum.getTypeSha()).append(") ");
        outcome = (String) em.createNativeQuery(sql.toString()).getSingleResult();

        return outcome;
    }

}
