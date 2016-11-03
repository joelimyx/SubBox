package com.joelimyx.subbox.dbassethelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.joelimyx.subbox.Classes.CheckOutItem;
import com.joelimyx.subbox.Classes.SubBox;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joe on 10/31/16.
 */

public class SubBoxHelper extends SQLiteOpenHelper {
    private static SubBoxHelper sInstance;
    public static final String DATABASE_NAME = "subbox.db";
    public static final int DATABASE_VERSION = 1;

    public static final String ITEM_TABLE_NAME = "item_table";
    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_PRICE = "price";
    public static final String COL_DETAIL = "detail";
    public static final String CREATE_ITEM_TABLE =
            "CREATE TABLE "+ITEM_TABLE_NAME+" ( "+
            COL_ID+ " INTEGER PRIMARY KEY, "+
            COL_NAME+" TEXT, "+
            COL_PRICE+" REAL,"+
            COL_DETAIL+" TEXT )";

    public static final String CHECKOUT_TABLE_NAME = "checkout_table";
    public static final String COL_ITEM_ID ="item_id";
    public static final String COL_COUNT = "count";
    public static final String CREATE_CHECKOUT_TABLE =
            "CREATE TABLE "+ CHECKOUT_TABLE_NAME +" ( "+
            COL_ID+ " INTEGER PRIMARY KEY, "+
            COL_ITEM_ID+ " INTEGER, "+
            COL_COUNT+ " INTEGER )";

    private SubBoxHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static SubBoxHelper getsInstance(Context context){
        if (sInstance == null){
            sInstance = new SubBoxHelper(context);
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ITEM_TABLE);
        db.execSQL(CREATE_CHECKOUT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+ITEM_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ CHECKOUT_TABLE_NAME);
        this.onCreate(db);
    }

    public List<SubBox> getSubBoxList(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                ITEM_TABLE_NAME,
                new String[]{COL_ID,COL_NAME,COL_PRICE,COL_DETAIL},
                null,null,null,null,null,null);
        List<SubBox> list = new ArrayList<>();
        if (cursor.moveToFirst()){
            while (!cursor.isAfterLast()){
                list.add(new SubBox(
                        cursor.getString(cursor.getColumnIndex(COL_NAME)),
                        cursor.getDouble(cursor.getColumnIndex(COL_PRICE)),
                        cursor.getString(cursor.getColumnIndex(COL_DETAIL)),
                        cursor.getInt(cursor.getColumnIndex(COL_ID))));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return list;
    }

    public SubBox getSubBoxByID(int id){

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                ITEM_TABLE_NAME,
                new String[]{COL_ID,COL_NAME,COL_PRICE,COL_DETAIL},
                COL_ID+" = ?",
                new String[]{String.valueOf(id)},
                null,null,null,null);

        if (cursor.moveToFirst()) {
            SubBox subBox = new SubBox(
                    cursor.getString(cursor.getColumnIndex(COL_NAME)),
                    cursor.getDouble(cursor.getColumnIndex(COL_PRICE)),
                    cursor.getString(cursor.getColumnIndex(COL_DETAIL)),
                    cursor.getInt(cursor.getColumnIndex(COL_ID)));
            cursor.close();
            return subBox;
        }else {
            cursor.close();
            return null;
        }
    }
    public List<CheckOutItem> getCheckoutList(){

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT "+COL_ITEM_ID+", "+COL_NAME+", "+COL_PRICE+", "+COL_COUNT+
                " FROM "+ITEM_TABLE_NAME+" JOIN "+ CHECKOUT_TABLE_NAME +
                " WHERE " + ITEM_TABLE_NAME+"."+COL_ID+
                " = "+ CHECKOUT_TABLE_NAME +"."+COL_ITEM_ID,null);
        List<CheckOutItem> list = new ArrayList<>();
        if (cursor.moveToFirst()){
            while (!cursor.isAfterLast()){
                list.add(new CheckOutItem(cursor.getInt(cursor.getColumnIndex(COL_ID)),
                        cursor.getString(cursor.getColumnIndex(COL_NAME)),
                        cursor.getInt(cursor.getColumnIndex(COL_COUNT)),
                        cursor.getDouble(cursor.getColumnIndex(COL_PRICE))
                        ));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return list;
    }
}
