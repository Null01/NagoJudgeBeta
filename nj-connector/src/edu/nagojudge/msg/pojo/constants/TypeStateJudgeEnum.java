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

    public static TypeStateJudgeEnum getAC() {
        return AC;
    }

    public static TypeStateJudgeEnum getWR() {
        return WR;
    }

    public static TypeStateJudgeEnum getTL() {
        return TL;
    }

    public static TypeStateJudgeEnum getCE() {
        return CE;
    }

    public static TypeStateJudgeEnum getRE() {
        return RE;
    }

    public static TypeStateJudgeEnum getCS() {
        return CS;
    }

    public static TypeStateJudgeEnum getIP() {
        return IP;
    }

}
