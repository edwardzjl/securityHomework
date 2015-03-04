package com.edwardlol.autoanswertest;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by edwardlol on 15/3/4.
 */
public class HistoryItem implements Serializable {
    String dataTime;
    String text;

    public HistoryItem(final String text) {
        this.text = text;
        this.dataTime = now();
    }

    private static String now() {
        final Calendar cal = Calendar.getInstance();
        final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy (HH:mm)");
        return sdf.format(cal.getTime());
    }

}