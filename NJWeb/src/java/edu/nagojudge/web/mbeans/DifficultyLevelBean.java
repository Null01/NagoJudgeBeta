/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.web.mbeans;

import edu.nagojudge.app.business.dao.beans.DifficultyLevelFacadeDAO;
import edu.nagojudge.app.business.dao.entities.DifficultyLevel;
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
public class DifficultyLevelBean implements Serializable {

    @EJB
    private DifficultyLevelFacadeDAO difficultyLevelFacade;

    private List<SelectItem> difficultyLevelItems;
    private DifficultyLevel difficultyLevel = new DifficultyLevel();

    private final Logger logger = Logger.getLogger(DifficultyLevelBean.class);

    public DifficultyLevelBean() {
    }

    @PostConstruct
    public void init() {
        if (difficultyLevelItems == null) {
            this.difficultyLevelItems = parceToSelectItemDifficultyLevel(difficultyLevelFacade.findAll());
        }
    }

    public List<SelectItem> getDifficultyLevelItems() {
        return difficultyLevelItems;
    }

    public void setDifficultyLevelItems(List<SelectItem> difficultyLevelItems) {
        this.difficultyLevelItems = difficultyLevelItems;
    }

    public DifficultyLevel getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(DifficultyLevel difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    private List<SelectItem> parceToSelectItemDifficultyLevel(List<DifficultyLevel> findDifficultyLevelEntities) {
        List<SelectItem> outcome = new ArrayList<SelectItem>();
        for (DifficultyLevel s : findDifficultyLevelEntities) {
            outcome.add(new SelectItem(s, s.getNameDifficulty()));
        }
        return outcome;
    }

}
