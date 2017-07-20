package edu.nagojudge.msg.pojo.constants;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author andresfelipegarciaduran
 */
@XmlRootElement
public enum TypeRoleEnum {

    ADMIN, PUBLIC_USER, ADD_CONTENT, TEAM, JUDGE_HUMAN;
}
