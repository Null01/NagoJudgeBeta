/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.app.business.dao.beans;

import edu.nagojudge.app.business.dao.entities.Account;
import edu.nagojudge.app.business.dao.entities.User;
import edu.nagojudge.app.exceptions.NagoJudgeException;
import edu.nagojudge.msg.pojo.AccountMessage;
import edu.nagojudge.msg.pojo.UserMessage;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author andresfelipegarciaduran
 */
@Stateless
public class AccountFacade extends AbstractFacade<Account> {

    @PersistenceContext(unitName = "NJWebPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AccountFacade() {
        super(Account.class);
    }

    public AccountMessage findAccountByIdAccount(long idAccount) {
        AccountMessage accountMessage = null;
        Account account = em.find(Account.class, idAccount);
        if (account != null) {
            if (account.getUserList() != null && !account.getUserList().isEmpty()) {
                UserMessage userMessage = parseEntityAccountToMessage(account, account.getUserList().get(0));
                accountMessage = userMessage.getAccountMessage();
            }
        }
        return accountMessage;
    }

    public UserMessage findUserByIdAccount(long idAccount) {
        UserMessage userMessage = null;
        Account account = em.find(Account.class, idAccount);
        if (account != null) {
            if (account.getUserList() != null && !account.getUserList().isEmpty()) {
                userMessage = parseEntityAccountToMessage(account, account.getUserList().get(0));
            }
        }
        return userMessage;
    }

    public void validateFieldsUnique(String nickname) throws NagoJudgeException {
        Long count = (Long) em.createQuery("SELECT COUNT(0) FROM Account a WHERE LOWER(a.nickname) = :nickname")
                .setParameter("nickname", nickname)
                .getSingleResult();
        if (count >= 1) {
            throw new NagoJudgeException(" EL NICKNAME " + nickname + " YA ESTA REGISTRADO. ");
        }
    }

    private UserMessage parseEntityAccountToMessage(Account account, User user) {
        UserMessage userMessage = new UserMessage();
        userMessage.setDateBirthday(user.getDateBirthday() != null ? user.getDateBirthday().getTime() : 0);
        userMessage.setEmail(user.getIdEmail());
        userMessage.setFirstName(user.getFirstName());
        userMessage.setKeyUser(user.getPasswordUser());
        userMessage.setLastName(user.getLastName());
        userMessage.setNameTypeUser(user.getIdType().getNameType());

        AccountMessage accountMessage = new AccountMessage();
        accountMessage.setDateRegister(account.getDateRegister() != null ? account.getDateRegister().getTime() : 0);
        accountMessage.setIdAccount(account.getIdAccount());
        accountMessage.setNickname(account.getNickname());
        userMessage.setAccountMessage(accountMessage);
        return userMessage;
    }

}
