/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.msg.pojo.collections;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author andresfelipegarciaduran
 * @param <T>
 */
@XmlRootElement
public class ListTypeMessage<E> {

    @XmlElement
    private List<E> list = new ArrayList<E>();

    public boolean add(E t) {
        return list.add(t);
    }

    public Iterator<E> getIterator() {
        return list.iterator();
    }

    public int getSize() {
        return list.size();
    }

    public List<E> getList() {
        return list;
    }

}
