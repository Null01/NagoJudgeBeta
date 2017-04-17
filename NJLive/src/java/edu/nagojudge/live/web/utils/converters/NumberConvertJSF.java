/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.live.web.utils.converters;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.SelectItem;

/**
 *
 * @author andresfelipegarciaduran
 */
@FacesConverter("numberConvertJSF")
public class NumberConvertJSF extends AbstractConvertJSF implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty() || value.trim().equals("")) {
            return null;
        } else {
            Iterator<SelectItem> items = createSelectItems(component).iterator();
            return findValueByStringConversion(context, component, items, value, this);
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null || value.equals("")) {
            return "";
        } else {
            if (value instanceof BigDecimal) {
                return String.valueOf((BigDecimal) value);
            }
            if (value instanceof BigInteger) {
                return String.valueOf((BigInteger) value);
            }
            if (value instanceof Long) {
                return String.valueOf((Long) value);
            }
            if (value instanceof Integer) {
                return String.valueOf((Integer) value);
            }
            return String.valueOf(value);
        }
    }
}
