/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.msg.pojo.constants;

/**
 *
 * @author andresfelipegarciaduran
 */
public enum TypeStateEnum {

    PRIVATE("PRIVATE"),
    PUBLIC("PUBLIC");

    private String type;

    private TypeStateEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
