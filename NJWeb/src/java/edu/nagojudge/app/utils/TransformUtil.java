/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.app.utils;

import edu.nagojudge.app.exceptions.UtilNagoJudgeException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.log4j.Logger;

/**
 *
 * @author andres.garcia
 */
public class TransformUtil {

    private final Logger logger = Logger.getLogger(TransformUtil.class);

    private static TransformUtil transformUtil;

    public Date addFormatHHmmaToDate(Date date, String time) throws UtilNagoJudgeException {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm a");
            Date parse = dateFormat.parse(time);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.HOUR_OF_DAY, parse.getHours());
            calendar.add(Calendar.MINUTE, parse.getMinutes());
            return calendar.getTime();
        } catch (ParseException ex) {
            logger.error(ex);
            throw new UtilNagoJudgeException(ex);
        }
    }

    public static TransformUtil getUtilTransform() {
        if (transformUtil == null) {
            transformUtil = new TransformUtil();
        }
        return transformUtil;
    }
}
