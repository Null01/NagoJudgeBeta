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
 * @author andres.garcia
 */
@XmlRootElement
public class PairMessage implements Serializable {

    private String _metaId, first, second;

    public PairMessage() {
    }

    public PairMessage(String first, String second) {
        this.first = first;
        this.second = second;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }

    public String getMetaId() {
        return _metaId;
    }

    public void setMetaId(String _metaId) {
        this._metaId = _metaId;
    }

    @Override
    public String toString() {
        return "PairMessage{" + "_metaId=" + _metaId + ", first=" + first + ", second=" + second + '}';
    }

}
