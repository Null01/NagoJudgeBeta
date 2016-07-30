/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.web.mbeans;

import edu.nagojudge.app.business.dao.beans.FunctionUserFacadeDAO;
import edu.nagojudge.app.business.dao.entities.FunctionUser;
import edu.nagojudge.app.exceptions.NagoJudgeException;
import edu.nagojudge.app.utils.FacesUtil;
import edu.nagojudge.app.utils.constants.IKeysApplication;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.log4j.Logger;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author andresfelipegarciaduran
 */
@ManagedBean
@ViewScoped
public class NavigationBean implements Serializable {

    @EJB
    private FunctionUserFacadeDAO functionUserFacade;

    private final Logger logger = Logger.getLogger(NavigationBean.class);

    private TreeNode treeNavigation;

    public NavigationBean() {
    }

    @PostConstruct
    public void init() {
        if (treeNavigation == null) {
            treeNavigation = buildTreeNavigation();
        }
    }

    public TreeNode getTreeNavigation() {
        return treeNavigation;
    }

    public void setTreeNavigation(TreeNode treeNavigation) {
        this.treeNavigation = treeNavigation;
    }

    public String actionRedirect(String urlRedirect) {
        return urlRedirect + "?faces-redirect=true";
    }

    private TreeNode buildTreeNavigation() {
        TreeNode tree = new DefaultTreeNode("root", null);
        try {
            Object object = FacesUtil.getFacesUtil().getSessionMap().get(IKeysApplication.KEY_DATA_TYPE_USER);
            if (object == null) {
                throw new NagoJudgeException("NO EXISTE UN TIPO USUARIO ASOCIADO A LA SESION.");
            }
            List<FunctionUser> list = functionUserFacade.findFunctionsAccessByRole(new Long(String.valueOf(object)));
            if (list != null) {
                Map<Long, List<FunctionUser>> map = new HashMap<Long, List<FunctionUser>>();
                for (FunctionUser f : list) {
                    if (f.getIdParent() == null) {
                        DefaultTreeNode defaultTreeNode = new DefaultTreeNode(f, tree); // Nodos principales del arbol.
                    } else {
                        if (map.get(f.getIdParent().longValue()) == null) {
                            map.put(f.getIdParent().longValue(), new ArrayList());
                        }
                        map.get(f.getIdParent().longValue()).add(f);
                    }
                }
                Queue<TreeNode> queue = new LinkedList<TreeNode>(tree.getChildren());
                while (!queue.isEmpty()) {
                    TreeNode treeNode = queue.poll();
                    if (treeNode != null) {
                        FunctionUser funcion = (FunctionUser) treeNode.getData();
                        if (map.get(funcion.getIdFunction()) != null) {
                            for (FunctionUser f : map.get(funcion.getIdFunction())) {
                                DefaultTreeNode defaultTreeNode = new DefaultTreeNode(f, treeNode);
                                queue.add(defaultTreeNode);
                            }
                        }
                    }
                }
            }

        } catch (Exception ex) {
            logger.error(ex);
            FacesUtil.getFacesUtil().addMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage());
        }
        return tree;
    }

}
