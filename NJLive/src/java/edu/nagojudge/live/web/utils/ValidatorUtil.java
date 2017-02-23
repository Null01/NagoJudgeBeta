package edu.nagojudge.live.web.utils;

import edu.nagojudge.live.web.exceptions.UtilNagoJudgeException;
import java.util.regex.Pattern;

/**
 *
 * @author andresfelipegarciaduran
 */
public class ValidatorUtil {

    private final String ONLY_LETTERS = "^[A-Za-z]++$";
    private final String ONLY_LETTERS_SPACE = "^[A-Za-z ]++$";
    private final String ONLY_LETTERS_NUMBERES_SPACE = "^[A-Za-z0-9 ]++$";
    private final String ONLY_LETTERS_NUMBERS = "^[A-Za-z0-9]++$";
    private final String ONLY_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private static ValidatorUtil utilValidator;

    public void onlyLetter(String text) throws UtilNagoJudgeException {
        Pattern compile = Pattern.compile(ONLY_LETTERS);
        if (!compile.matcher(text).matches()) {
            throw new UtilNagoJudgeException("Solo se permiten letras. {" + text + "}");
        }
    }

    public void onlyLetterNumberSpace(String text) throws UtilNagoJudgeException {
        Pattern compile = Pattern.compile(ONLY_LETTERS_NUMBERES_SPACE);
        if (!compile.matcher(text).matches()) {
            throw new UtilNagoJudgeException("Solo se permiten letras. {" + text + "}");
        }
    }

    public void onlyLetterSpace(String text) throws UtilNagoJudgeException {
        Pattern compile = Pattern.compile(ONLY_LETTERS_SPACE);
        if (!compile.matcher(text).matches()) {
            throw new UtilNagoJudgeException("Solo se permiten letras. {" + text + "}");
        }
    }

    public void onlyLetterNumber(String text) throws UtilNagoJudgeException {
        Pattern compile = Pattern.compile(ONLY_LETTERS_NUMBERS);
        if (!compile.matcher(text).matches()) {
            throw new UtilNagoJudgeException("Solo se permiten letras y/o numeros. {" + text + "}");
        }
    }

    public void onlyEmail(String text) throws UtilNagoJudgeException {
        Pattern compile = Pattern.compile(ONLY_EMAIL);
        if (!compile.matcher(text).matches()) {
            throw new UtilNagoJudgeException("Correo electronico invalido. {" + text + "}");
        }
    }

    public static ValidatorUtil getUtilValidator() {
        if (utilValidator == null) {
            utilValidator = new ValidatorUtil();
        }
        return utilValidator;
    }

}
