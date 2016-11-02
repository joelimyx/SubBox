package com.joelimyx.subbox.Classes;

/**
 * Created by Joe on 11/1/16.
 */

public class SubBox {
    private String mName;
    private double mPrice;
    private String mDescription;
    private int mId;

    public SubBox(String name, double price, String description, int id) {
        mName = name;
        mPrice = price;
        mDescription = description;
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public double getPrice() {
        return mPrice;
    }

    public String getDescription() {
        return mDescription;
    }

    public int getId() {
        return mId;
    }
}
