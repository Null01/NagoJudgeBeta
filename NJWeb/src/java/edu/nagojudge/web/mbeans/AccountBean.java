/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.web.mbeans;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.log4j.Logger;

/**
 *
 * @author andresfelipegarciaduran
 */
@ManagedBean
@ViewScoped
public class AccountBean implements Serializable {

    private final Logger LOGGER = Logger.getLogger(AccountBean.class);

    /**
     * Creates a new instance of AccountBean
     */
    public AccountBean() {
    }

}
