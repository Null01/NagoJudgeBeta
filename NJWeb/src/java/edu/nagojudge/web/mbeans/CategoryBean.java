/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.web.mbeans;

import edu.nagojudge.app.business.dao.beans.CategoryProblemFacade;
import edu.nagojudge.app.business.dao.entities.Category;
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
    private CategoryProblemFacade categoryProblemFacade;

    private List<SelectItem> categoryProblemItems;
    private Category categoryProblemView = new Category();

    public CategoryBean() {
    }

    @PostConstruct
    public void init() {
        if (categoryProblemItems == null) {
            this.categoryProblemItems = parceToSelectItemCategoryProblem(categoryProblemFacade.findAll());
        }
    }

    public Category getCategoryProblemView() {
        return categoryProblemView;
    }

    public void setCategoryProblemView(Category categoryProblemView) {
        this.categoryProblemView = categoryProblemView;
    }

    public List<SelectItem> getCategoryProblemItems() {
        return categoryProblemItems;
    }

    public void setCategoryProblemItems(List<SelectItem> categoryProblemItems) {
        this.categoryProblemItems = categoryProblemItems;
    }

    private List<SelectItem> parceToSelectItemCategoryProblem(List<Category> findCategoryProblemEntities) {
        List<SelectItem> outcome = new ArrayList<SelectItem>();
        for (Category s : findCategoryProblemEntities) {
            outcome.add(new SelectItem(s, s.getNameCategory()));
        }
        return outcome;
    }

}
