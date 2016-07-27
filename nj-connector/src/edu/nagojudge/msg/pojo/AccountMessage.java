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
public class AccountMessage implements Serializable {

    private Long id;
    private String nickname;
    private Long dateRegister;

    public AccountMessage() {
    }

    public AccountMessage(Long id, String nickname, Long dateRegister) {
        this.id = id;
        this.nickname = nickname;
        this.dateRegister = dateRegister;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Long getDateRegister() {
        return dateRegister;
    }

    public void setDateRegister(Long dateRegister) {
        this.dateRegister = dateRegister;
    }

}
