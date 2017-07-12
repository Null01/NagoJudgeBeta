/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.msg.pojo;

import edu.nagojudge.msg.pojo.constants.TypeRoleEnum;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author andresfelipegarciaduran
 */
@XmlRootElement
public class MetadataMessage implements Serializable {

    private String token;
    private TypeRoleEnum role;
    private String user;
    private String i18;

    public MetadataMessage() {
    }

    public MetadataMessage(String token, TypeRoleEnum role, String user, String i18) {
        this.token = token;
        this.role = role;
        this.user = user;
        this.i18 = i18;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getI18() {
        if (i18 == null) {
            setI18("en");
        }
        return i18;
    }

    public void setI18(String i18) {
        this.i18 = i18;
    }

    public TypeRoleEnum getRole() {
        return role;
    }

    public void setRole(TypeRoleEnum role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "MetadataMessage{" + "token=" + token + ", role=" + role + ", user=" + user + ", i18=" + i18 + '}';
    }

}
