/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.msg.pojo;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author andresfelipegarciaduran
 */
@XmlRootElement
public class SessionMessage implements Serializable{

    private String email;
    private AccountMessage account;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public AccountMessage getAccount() {
        return account;
    }

    public void setAccount(AccountMessage account) {
        this.account = account;
    }

}
