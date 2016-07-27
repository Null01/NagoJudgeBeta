/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.tools.utils;

import java.io.Serializable;

/**
 *
 * @author andresfelipegarciaduran
 */
public class FormatUtil implements Serializable {

    private static FormatUtil formatUtil;

    public static FormatUtil getInstance() {
        if (formatUtil == null) {
            formatUtil = new FormatUtil();
        }
        return formatUtil;
    }

    public String buildZerosToLeft(long number, int size) {
        String format = String.format("%" + size + "d", number);
        return format.replaceAll(" ", "0");
    }
}
