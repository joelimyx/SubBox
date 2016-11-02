package com.joelimyx.subbox.dbassethelper;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by Joe on 10/31/16.
 */

public class DBAssetHelper extends SQLiteAssetHelper {
    public static final String DATABASE_NAME = "subbox.db";
    public static final int DATABASE_VERSION = 1;

    public DBAssetHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}
