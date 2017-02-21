/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.app.business.dao.beans;

import edu.nagojudge.app.business.dao.entities.ParametersNj;
import edu.nagojudge.msg.pojo.ParametersNJMessage;
import java.util.Calendar;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.log4j.Logger;

/**
 *
 * @author andresfelipegarciaduran
 */
@Stateless
public class ParametersNjFacade extends AbstractFacade<ParametersNj> {

    private final Logger logger = Logger.getLogger(ParametersNjFacade.class);

    @PersistenceContext(unitName = "NJWebPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ParametersNjFacade() {
        super(ParametersNj.class);
    }

    public void updateParametersNJ(ParametersNJMessage parametersNJView) throws Exception {
        try {
            logger.debug("INICIA METODO - updateParametersNJ()");
            parametersNJView.setDateUpdated(Calendar.getInstance().getTime());
            this.edit(parseMessageToEntity(parametersNJView));
            logger.debug("getIdParameter [" + parametersNJView.getIdParameter() + "]");
        } catch (Exception ex) {
            logger.debug(ex);
            throw ex;
        } finally {
            logger.debug("FIN METODO - updateParametersNJ()");
        }

    }

    private ParametersNj parseMessageToEntity(ParametersNJMessage parametersNJMessage) {
        ParametersNj parametersNj = new ParametersNj();
        parametersNj.setIdParameter(parametersNJMessage.getIdParameter());
        parametersNj.setDescription(parametersNJMessage.getDescription());
        parametersNj.setDateCreated(parametersNJMessage.getDateCreated());
        parametersNj.setDateUpdated(parametersNJMessage.getDateUpdated());
        parametersNj.setContentParameter(parametersNJMessage.getContentParameter());
        return parametersNj;
    }

}
