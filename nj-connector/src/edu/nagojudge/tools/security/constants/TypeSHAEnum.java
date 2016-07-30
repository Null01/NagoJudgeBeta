/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.tools.security.constants;

/**
 *
 * @author andresfelipegarciaduran
 */
public enum TypeSHAEnum {

    SHA256("SHA-256"),
    SHA256_NUM("256");

    private String typeSha;

    private TypeSHAEnum(String typeSha) {
        this.typeSha = typeSha;
    }

    public void setTypeSha(String typeSha) {
        this.typeSha = typeSha;
    }

    public String getTypeSha() {
        return typeSha;
    }

}
