/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.live.web.utils.converters;

import edu.nagojudge.msg.pojo.LanguageProgrammingMessage;
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
@FacesConverter("languageProgrammingConvertJSF")
public class LanguageProgrammingConvertJSF extends AbstractConvertJSF implements Converter {

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
            return String.valueOf(((LanguageProgrammingMessage) value).getIdLanguage());
        }
    }
}
