package edu.nagojudge.tools.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;

/**
 *
 * @author andresfelipegarciaduran
 */
public class FormatUtil {

    private static FormatUtil fileUtil;
    private StringTokenizer tokenizer;

    public FormatUtil() {
    }

    public static FormatUtil getInstance() {
        if (fileUtil == null) {
            fileUtil = new FormatUtil();
        }
        return fileUtil;
    }

    public String formatDate(Date date, String format) {
        if (format == null) {
            format = "MMMM d, yyyy hh:mm a";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, new Locale("es"));
        return dateFormat.format(date);
    }

    public String buildZerosToLeft(long number, int size) {
        String format = String.format("%" + size + "d", number);
        return format.replaceAll(" ", "0");
    }

    public String onlyFirstLetterFromWordToUpperCase(String string) {
        if (string == null) {
            string = new String();
        }
        string = string.toLowerCase();
        if (!string.isEmpty() && string.length() >= 1) {
            String firstLetter = (string.charAt(0) + "").toUpperCase();
            string = firstLetter + string.substring(1);
        }
        return string;
    }

    public String allFirstLettersForWordToUpperCase(String string) {
        if (string == null) {
            string = new String();
        }
        String outcome = "";
        tokenizer = new StringTokenizer(string.trim());
        while (tokenizer.hasMoreTokens()) {
            String stringLowewCase = tokenizer.nextToken().toLowerCase();
            outcome += (stringLowewCase.charAt(0) + "").toUpperCase();
            if (stringLowewCase.length() > 1) {
                outcome += stringLowewCase.substring(1);
            }
            outcome += " ";
        }
        return outcome.trim();
    }

    public int parseTimeToMinutes(String time) {
        int outcome = 0;
        time = time.trim();
        StringTokenizer st = new StringTokenizer(time);
        while (st.hasMoreTokens()) {
            String part = st.nextToken();
            if (part.toLowerCase().startsWith("semana")) {
                outcome *= (7 * 24 * 60);
            } else {
                if (part.toLowerCase().startsWith("hora")) {
                    outcome *= (60);
                } else {
                    if (part.toLowerCase().startsWith("dia")) {
                        outcome *= (60 * 24);
                    } else {
                        if (isInteger(part)) {
                            outcome = Integer.parseInt(part);
                        }
                    }
                }
            }
        }
        return outcome;
    }

    public Date addTimeToDate(Date dateChallenge, String timeStartSelected) throws ParseException {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm a");
            Date parse = dateFormat.parse(timeStartSelected);
            dateChallenge.setHours(parse.getHours());
            dateChallenge.setMinutes(parse.getMinutes());
            dateChallenge.setSeconds(parse.getSeconds());
        } catch (ParseException ex) {
            throw ex;
        }
        return dateChallenge;
    }

    private boolean isInteger(String string) {
        try {
            int n = Integer.parseInt(string);
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }

    public <T> T nvl(T a, T b) {
        return (a == null) ? b : a;
    }

    public String replaceSeparatorPath(String path) {
        String oldCharacters = path.contains("/") ? "/" : (path.contains("\\") ? "\\" : "");
        String newCharactes = oldCharacters.compareTo("/") == 0 ? "\\" : (oldCharacters.compareTo("\\") == 0 ? "/" : "");
        return path.replace(oldCharacters, newCharactes);
    }

    public boolean isValidNumberFloatPoint(final String string) {
        final String regex = "[+-]?([0-9]*[.])?[0-9]+";
        return string.matches(regex);
    }

}
