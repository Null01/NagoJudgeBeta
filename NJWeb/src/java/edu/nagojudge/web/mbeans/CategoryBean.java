/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.web.mbeans;

import edu.nagojudge.app.business.dao.beans.CategoryProblemFacadeDAO;
import edu.nagojudge.app.business.dao.entities.CategoryProblem;
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
public class CategoryBean implements Serializable {

    private final Logger logger = Logger.getLogger(CategoryBean.class);

    @EJB
    private CategoryProblemFacadeDAO categoryProblemFacade;

    private List<SelectItem> categoryProblemItems;
    private CategoryProblem categoryProblemView = new CategoryProblem();

    public CategoryBean() {
    }

    @PostConstruct
    public void init() {
        if (categoryProblemItems == null) {
            this.categoryProblemItems = parceToSelectItemCategoryProblem(categoryProblemFacade.findAll());
        }
    }

    public CategoryProblem getCategoryProblemView() {
        return categoryProblemView;
    }

    public void setCategoryProblemView(CategoryProblem categoryProblemView) {
        this.categoryProblemView = categoryProblemView;
    }

    public List<SelectItem> getCategoryProblemItems() {
        return categoryProblemItems;
    }

    public void setCategoryProblemItems(List<SelectItem> categoryProblemItems) {
        this.categoryProblemItems = categoryProblemItems;
    }

    private List<SelectItem> parceToSelectItemCategoryProblem(List<CategoryProblem> findCategoryProblemEntities) {
        List<SelectItem> outcome = new ArrayList<SelectItem>();
        for (CategoryProblem s : findCategoryProblemEntities) {
            outcome.add(new SelectItem(s, s.getNameCategory()));
        }
        return outcome;
    }

}