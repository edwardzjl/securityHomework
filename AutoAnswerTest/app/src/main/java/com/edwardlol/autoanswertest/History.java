package com.edwardlol.autoanswertest;

import android.content.Context;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by edwardlol on 15/3/4.
 */
public class History implements Serializable {
    private ArrayList<HistoryItem> history;
    private Context context;

    public History(Context context) {
        this.context = context;
        history = new ArrayList<HistoryItem>();
    }

    public void add(String text) {
        if (!text.equals("")) {
            history.add(0, new HistoryItem(text));
        }
    }
}