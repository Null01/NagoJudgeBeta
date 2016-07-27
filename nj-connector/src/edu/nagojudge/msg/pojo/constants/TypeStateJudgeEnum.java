package edu.nagojudge.msg.pojo.constants;

/**
 *
 * @author andresfelipegarciaduran
 */
public enum TypeStateJudgeEnum {

    AC("ACCEPTED"),
    WR("WRONG ANSWER"),
    TL("TIME LIMIT"),
    CE("COMPILATION ERROR"),
    RE("RUNTIME ERROR"),
    CS("CONTACT STAFF NOW"),
    IP("IN PROGRESS...");

    private TypeStateJudgeEnum(String value) {
        this.value = value;
    }
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
