package com.joelimyx.subbox;

/**
 * Created by Joe on 11/1/16.
 */

public class SubBox {
    private String mName;
    private double mPrice;
    private String mDescription;

    public SubBox(String name, double price, String description) {
        mName = name;
        mPrice = price;
        mDescription = description;
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
}
