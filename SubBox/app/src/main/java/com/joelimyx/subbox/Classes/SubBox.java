package com.joelimyx.subbox.Classes;

/**
 * Created by Joe on 11/1/16.
 */

public class SubBox {
    private String mName;
    private double mPrice;
    private String mDescription;
    private int mId;
    private String mImgUrl;
    private String mType;

    public SubBox(String name, double price, String description, int id, String imgUrl, String type) {
        mName = name;
        mPrice = price;
        mDescription = description;
        mId = id;
        mImgUrl = imgUrl;
        mType = type;
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

    public String getImgUrl() {
        return mImgUrl;
    }

    public String getType() {
        return mType;
    }
}
