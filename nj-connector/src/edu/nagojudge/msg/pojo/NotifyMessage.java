/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.msg.pojo;

import edu.nagojudge.msg.pojo.collections.ListMessage;
import edu.nagojudge.msg.pojo.constants.TypeActionEnum;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author andresfelipegarciaduran
 */
@XmlRootElement
public class NotifyMessage {

    private String id;
    private String html;
    private String action;
    private ListMessage<Object> values = new ListMessage<Object>();

    public NotifyMessage() {
    }

    public NotifyMessage(String id, String html) {
        this.id = id;
        this.html = html;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public ListMessage<Object> getValues() {
        return values;
    }

    public void setValues(ListMessage<Object> values) {
        this.values = values;
    }

}
