package com.epam.game.filters;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.UnsupportedEncodingException;

public final class EncodingRequestWrapper extends HttpServletRequestWrapper {
    public EncodingRequestWrapper(HttpServletRequest servletRequest) {
        super(servletRequest);
    }
    
    public String[] getParameterValues(String parameter) {
        String[] values = super.getParameterValues(parameter);
        if (values == null) {
            return null;
        }
        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = decode(values[i]);
        }
        return encodedValues;
    }

    public String getParameter(String parameter) {
        String value = super.getParameter(parameter);
        if (value == null) {
            return null;
        }
        return decode(value);
    }

    public String getHeader(String name) {
        String value = super.getHeader(name);
        if (value == null)
            return null;
        return decode(value);
    }

    private String decode(String value) {
        if(value == null){
            return null;
        }
        try {
            value = new String(value.getBytes("iso8859-1"), "UTF8");
        } catch (UnsupportedEncodingException e) {
            //
        }
        return value;
    }
}