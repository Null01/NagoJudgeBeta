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
public class LanguageProgrammingMessage implements Serializable {

    private Long idLanguage;
    private String nameProgramming;
    private String extension;

    public LanguageProgrammingMessage() {
    }

    public LanguageProgrammingMessage(Long idLanguage, String nameProgramming, String extension) {
        this.idLanguage = idLanguage;
        this.nameProgramming = nameProgramming;
        this.extension = extension;
    }

    public Long getIdLanguage() {
        return idLanguage;
    }

    public void setIdLanguage(Long idLanguage) {
        this.idLanguage = idLanguage;
    }

    public String getNameProgramming() {
        return nameProgramming;
    }

    public void setNameProgramming(String nameProgramming) {
        this.nameProgramming = nameProgramming;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

}
