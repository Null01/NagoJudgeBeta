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
    private Long idAccount;
    private String nicknameAccount;
    private String nameTypeUser;

    public UserMessage() {
    }

    public UserMessage(String email, String keyUser, String firstName, String lastName, Long dateBirthday, Long idAccount, String nicknameAccount, String nameTypeUser) {
        this.email = email;
        this.keyUser = keyUser;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateBirthday = dateBirthday;
        this.idAccount = idAccount;
        this.nicknameAccount = nicknameAccount;
        this.nameTypeUser = nameTypeUser;
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

    public Long getIdAccount() {
        return idAccount;
    }

    public void setIdAccount(Long idAccount) {
        this.idAccount = idAccount;
    }

    public String getNicknameAccount() {
        return nicknameAccount;
    }

    public void setNicknameAccount(String nicknameAccount) {
        this.nicknameAccount = nicknameAccount;
    }

    public String getNameTypeUser() {
        return nameTypeUser;
    }

    public void setNameTypeUser(String nameTypeUser) {
        this.nameTypeUser = nameTypeUser;
    }

}
