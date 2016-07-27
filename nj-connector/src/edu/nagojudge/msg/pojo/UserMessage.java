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
public class UserMessage implements Serializable {

    private String email;
    private String keyUser;
    private String firstName;
    private String lastName;
    private Long dateBirthday;
    private AccountMessage account;
    private TypeUserMessage typeUser;

    public UserMessage() {
    }

    public UserMessage(String email, String keyUser, String firstName, String lastName, Long dateBirthday, AccountMessage account, TypeUserMessage typeUser) {
        this.email = email;
        this.keyUser = keyUser;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateBirthday = dateBirthday;
        this.account = account;
        this.typeUser = typeUser;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getKeyUser() {
        return keyUser;
    }

    public void setKeyUser(String keyUser) {
        this.keyUser = keyUser;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Long getDateBirthday() {
        return dateBirthday;
    }

    public void setDateBirthday(Long dateBirthday) {
        this.dateBirthday = dateBirthday;
    }

    public AccountMessage getAccount() {
        return account;
    }

    public void setAccount(AccountMessage account) {
        this.account = account;
    }

    public TypeUserMessage getTypeUser() {
        return typeUser;
    }

    public void setTypeUser(TypeUserMessage typeUser) {
        this.typeUser = typeUser;
    }

}
