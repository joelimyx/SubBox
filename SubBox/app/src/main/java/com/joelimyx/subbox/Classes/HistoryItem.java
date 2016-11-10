package com.joelimyx.subbox.Classes;

/**
 * Created by Joe on 11/8/16.
 */

public class HistoryItem {
    private int mId;
    private long mDate;

    public HistoryItem(int id, long date) {
        mId = id;
        mDate = date;
    }

    public int getId() {
        return mId;
    }

    public long getDate() {
        return mDate;
    }

}
