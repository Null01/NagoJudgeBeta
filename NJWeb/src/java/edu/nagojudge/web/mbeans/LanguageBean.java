/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.web.mbeans;

import edu.nagojudge.app.business.dao.beans.LanguageProgrammingFacadeDAO;
import edu.nagojudge.app.business.dao.entities.LanguageProgramming;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import org.apache.log4j.Logger;

/**
 *
 * @author andresfelipegarciaduran
 */
@ManagedBean
@ViewScoped
public class LanguageBean implements Serializable {

    @EJB
    private LanguageProgrammingFacadeDAO languageProgrammingFacade;

    private final Logger logger = Logger.getLogger(LanguageBean.class);

    private List<SelectItem> listLanguageProgrammingItems;

    private LanguageProgramming languageProgrammingView = new LanguageProgramming();

    /**
     * Creates a new instance of LanguageBean
     */
    public LanguageBean() {
    }

    @PostConstruct
    public void init() {

        this.listLanguageProgrammingItems = parseToLanguageProgrammingItems(languageProgrammingFacade.findAll());
    }

    public LanguageProgramming getLanguageProgrammingView() {
        return languageProgrammingView;
    }

    public void setLanguageProgrammingView(LanguageProgramming languageProgrammingView) {
        this.languageProgrammingView = languageProgrammingView;
    }

    public List<SelectItem> getListLanguageProgrammingItems() {
        return listLanguageProgrammingItems;
    }

    public void setListLanguageProgrammingItems(List<SelectItem> listLanguageProgrammingItems) {
        this.listLanguageProgrammingItems = listLanguageProgrammingItems;
    }

    private List<SelectItem> parseToLanguageProgrammingItems(List<LanguageProgramming> findLanguageProgrammingEntities) {
        List<SelectItem> outcome = new ArrayList<SelectItem>();
        for (LanguageProgramming s : findLanguageProgrammingEntities) {
            outcome.add(new SelectItem(s, s.getNameLanguage()));
        }
        return outcome;
    }

}
