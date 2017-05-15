/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.web.mbeans;

import edu.nagojudge.app.business.dao.beans.ComplexityAlgorithmFacade;
import edu.nagojudge.app.business.dao.entities.Category;
import edu.nagojudge.app.business.dao.entities.ComplexityAlgorithm;
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
public class ComplexityBean implements Serializable {

    private final Logger logger = Logger.getLogger(ComplexityBean.class);

    @EJB
    private ComplexityAlgorithmFacade complexityAlgorithmFacade;

    private List<SelectItem> complexityProblemItems;
    private ComplexityAlgorithm complexityProblemView = new ComplexityAlgorithm();

    public ComplexityBean() {
    }

    @PostConstruct
    public void init() {
        if (complexityProblemItems == null) {
            this.complexityProblemItems = parceToSelectItemComplexityProblem(complexityAlgorithmFacade.findAll());
        }
    }

    public List<SelectItem> getComplexityProblemItems() {
        return complexityProblemItems;
    }

    public void setComplexityProblemItems(List<SelectItem> complexityProblemItems) {
        this.complexityProblemItems = complexityProblemItems;
    }

    public ComplexityAlgorithm getComplexityProblemView() {
        return complexityProblemView;
    }

    public void setComplexityProblemView(ComplexityAlgorithm complexityProblemView) {
        this.complexityProblemView = complexityProblemView;
    }

    private List<SelectItem> parceToSelectItemComplexityProblem(List<ComplexityAlgorithm> findAll) {
        List<SelectItem> outcome = new ArrayList<SelectItem>();
        for (ComplexityAlgorithm s : findAll) {
            outcome.add(new SelectItem(s, s.getNameComplexityAlgorithm()));
        }
        return outcome;
    }

}
