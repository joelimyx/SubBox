package com.joelimyx.subbox.Classes;

/**
 * Created by Joe on 11/8/16.
 */

public class HistoryItem {
    private int mId;
    private long mDate;
    private double mSubtotal;

    public HistoryItem(int id, long date, double subtotal) {
        mId = id;
        mDate = date;
        mSubtotal = subtotal;
    }

    public int getId() {
        return mId;
    }

    public long getDate() {
        return mDate;
    }

    public double getSubtotal() {
        return mSubtotal;
    }
}
