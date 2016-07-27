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
public class ResponseMessage implements Serializable {

    private Object object;

    public ResponseMessage() {
    }

    public ResponseMessage(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

}
