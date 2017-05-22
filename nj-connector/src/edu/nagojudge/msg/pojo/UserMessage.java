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
    private String nameTypeUser;
    private AccountMessage accountMessage;

    public UserMessage() {
    }

    public UserMessage(String email, String keyUser, String firstName, String lastName, Long dateBirthday, String nameTypeUser, AccountMessage accountMessage) {
        this.email = email;
        this.keyUser = keyUser;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateBirthday = dateBirthday;
        this.nameTypeUser = nameTypeUser;
        this.accountMessage = accountMessage;
    }

    public AccountMessage getAccountMessage() {
        return accountMessage;
    }

    public void setAccountMessage(AccountMessage accountMessage) {
        this.accountMessage = accountMessage;
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

    public String getNameTypeUser() {
        return nameTypeUser;
    }

    public void setNameTypeUser(String nameTypeUser) {
        this.nameTypeUser = nameTypeUser;
    }

}
