package com.lanacion.ahora.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Leandro on 07/02/2015.
 */
public class Formatter {

    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd/MM");

    public static final String formatDate(Date date) {
        return DATE_FORMATTER.format(date);
    }
}
