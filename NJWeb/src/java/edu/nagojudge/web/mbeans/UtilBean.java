/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.web.mbeans;

import edu.nagojudge.tools.utils.FormatUtil;
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
public class UtilBean implements Serializable {

    private final Logger logger = Logger.getLogger(UtilBean.class);

    private final int SIZE_FORMAT_PROBLEM_ID = 5;
    private final int SIZE_FORMAT_SUBMIT_ID = 5;
    private final int SIZE_FORMAT_ACCOUNT_ID = 5;
    private final int SIZE_FORMAT_CHALLENGE_ID = 5;

    public UtilBean() {
    }

    public String formatIdProblem(long id) {
        return FormatUtil.getInstance().buildZerosToLeft(id, SIZE_FORMAT_PROBLEM_ID);
    }

    public String formatIdSubmit(long id) {
        return FormatUtil.getInstance().buildZerosToLeft(id, SIZE_FORMAT_SUBMIT_ID);
    }

    public String formatIdAccount(long id) {
        return FormatUtil.getInstance().buildZerosToLeft(id, SIZE_FORMAT_ACCOUNT_ID);
    }

    public String formatIdChallenge(long id) {
        return FormatUtil.getInstance().buildZerosToLeft(id, SIZE_FORMAT_CHALLENGE_ID);
    }

    public String formatCharacter(int codeChar) {
        return String.valueOf(Character.toChars(codeChar));
    }

}
