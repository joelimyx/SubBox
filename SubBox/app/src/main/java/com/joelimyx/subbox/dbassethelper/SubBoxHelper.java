package com.joelimyx.subbox.dbassethelper;

import android.content.ContentValues;
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
    public static final int DATABASE_VERSION = 2;

    public static final String ITEM_TABLE_NAME = "item_table";
    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_PRICE = "price";
    public static final String COL_DETAIL = "detail";
    public static final String COL_IMG_URL= "img_url";
    public static final String COL_TYPE = "type";
    public static final String [] COL_ITEMS_SELECTION = new String[]{COL_ID,COL_NAME,COL_PRICE,COL_DETAIL,COL_IMG_URL,COL_TYPE};
    public static final String CREATE_ITEM_TABLE =
            "CREATE TABLE "+ITEM_TABLE_NAME+" ( "+
            COL_ID+ " INTEGER PRIMARY KEY, "+
            COL_NAME+" TEXT, "+
            COL_PRICE+" REAL,"+
            COL_DETAIL+" TEXT, "+
            COL_IMG_URL+" TEXT,"+
            COL_TYPE+" TEXT )";

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

    /*----------------------------------------------------------
    MAIN LIST AREA
     ----------------------------------------------------------*/
    public List<SubBox> getSubBoxList(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                ITEM_TABLE_NAME,
                COL_ITEMS_SELECTION,
                null,null,null,null,null,null);
        List<SubBox> list = new ArrayList<>();
        if (cursor.moveToFirst()){
            while (!cursor.isAfterLast()){
                list.add(new SubBox(
                        cursor.getString(cursor.getColumnIndex(COL_NAME)),
                        cursor.getDouble(cursor.getColumnIndex(COL_PRICE)),
                        cursor.getString(cursor.getColumnIndex(COL_DETAIL)),
                        cursor.getInt(cursor.getColumnIndex(COL_ID)),
                        cursor.getString(cursor.getColumnIndex(COL_IMG_URL)),
                        cursor.getString(cursor.getColumnIndex(COL_TYPE))
                        ));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return list;
    }

    public List<SubBox> sortListByPrice(){
        SQLiteDatabase db  = getReadableDatabase();
        Cursor cursor = db.query(
                ITEM_TABLE_NAME,
                COL_ITEMS_SELECTION,
                null,
                null,
                null,null,
                COL_PRICE,
                null);
        List<SubBox> list = new ArrayList<>();
        if (cursor.moveToFirst()){
            while (!cursor.isAfterLast()){
                list.add(new SubBox(
                        cursor.getString(cursor.getColumnIndex(COL_NAME)),
                        cursor.getDouble(cursor.getColumnIndex(COL_PRICE)),
                        cursor.getString(cursor.getColumnIndex(COL_DETAIL)),
                        cursor.getInt(cursor.getColumnIndex(COL_ID)),
                        cursor.getString(cursor.getColumnIndex(COL_IMG_URL)),
                        cursor.getString(cursor.getColumnIndex(COL_TYPE))
                        ));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return list;
    }

    /**
     * @param query = search query
     * @return List of SubBoxes that matches the query with subBox name
     */
    public List<SubBox> getSearchList(String query){
        SQLiteDatabase db  = getReadableDatabase();
        Cursor cursor = db.query(
                ITEM_TABLE_NAME,
                COL_ITEMS_SELECTION,
                COL_NAME+" LIKE ? ",
                new String[]{"%"+query+"%"},
                null,null,null,null);
        List<SubBox> list = new ArrayList<>();
        if (cursor.moveToFirst()){
            while (!cursor.isAfterLast()){
                list.add(new SubBox(
                        cursor.getString(cursor.getColumnIndex(COL_NAME)),
                        cursor.getDouble(cursor.getColumnIndex(COL_PRICE)),
                        cursor.getString(cursor.getColumnIndex(COL_DETAIL)),
                        cursor.getInt(cursor.getColumnIndex(COL_ID)),
                        cursor.getString(cursor.getColumnIndex(COL_IMG_URL)),
                        cursor.getString(cursor.getColumnIndex(COL_TYPE))
                        ));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return list;
    }

    /*----------------------------------------------------------
    DETAIL AREA
     ----------------------------------------------------------*/
    public SubBox getSubBoxByID(int id){

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                ITEM_TABLE_NAME,
                COL_ITEMS_SELECTION,
                COL_ID+" = ?",
                new String[]{String.valueOf(id)},
                null,null,null,null);

        if (cursor.moveToFirst()) {
            SubBox subBox = new SubBox(
                    cursor.getString(cursor.getColumnIndex(COL_NAME)),
                    cursor.getDouble(cursor.getColumnIndex(COL_PRICE)),
                    cursor.getString(cursor.getColumnIndex(COL_DETAIL)),
                    cursor.getInt(cursor.getColumnIndex(COL_ID)),
                    cursor.getString(cursor.getColumnIndex(COL_IMG_URL)),
                    cursor.getString(cursor.getColumnIndex(COL_TYPE)));
            cursor.close();
            return subBox;
        }else {
            cursor.close();
            return null;
        }
    }

    /**
     * @return true if added for snackbar to show up
     */

    public boolean isSubBoxInCheckOut(int id){

        SQLiteDatabase db = getReadableDatabase();

        //Try to check and see if item is already in checkout table
        Cursor cursor = db.rawQuery(
                "SELECT "+ITEM_TABLE_NAME+"."+COL_ID+", "+COL_NAME+", "+COL_PRICE+", "+COL_COUNT+
                        " FROM "+ITEM_TABLE_NAME+" JOIN "+ CHECKOUT_TABLE_NAME +
                        " WHERE " + ITEM_TABLE_NAME+"."+COL_ID+
                        " = "+ CHECKOUT_TABLE_NAME +"."+COL_ITEM_ID+
                        " AND "+COL_ITEM_ID+" = "+id,null);

        Boolean isAdded = cursor.moveToFirst();
        cursor.close();
        return isAdded;
    }

    //Add to CheckOut if SubBox is not in CheckOut
    public void addSubBoxToCheckOut(int id){
        SQLiteDatabase db = getWritableDatabase();

        //If not, insert it into the checkout table
        if (!isSubBoxInCheckOut(id)) {
            ContentValues values = new ContentValues();
            values.put(COL_ITEM_ID, id);
            values.put(COL_COUNT, 1);
            db.insert(CHECKOUT_TABLE_NAME, null, values);
        }
        db.close();
    }

    //--------------------------------------------------------------------------------------------------------------------
    //CheckOut AREA
    //--------------------------------------------------------------------------------------------------------------------
    public List<CheckOutItem> getCheckoutList(){

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT "+ITEM_TABLE_NAME+"."+COL_ID+", "+COL_NAME+", "+COL_PRICE+", "+COL_COUNT+", "+COL_IMG_URL+
                " FROM "+ITEM_TABLE_NAME+" JOIN "+ CHECKOUT_TABLE_NAME +
                " WHERE " + ITEM_TABLE_NAME+"."+COL_ID+
                " = "+ CHECKOUT_TABLE_NAME +"."+COL_ITEM_ID,null);
        List<CheckOutItem> list = new ArrayList<>();
        if (cursor.moveToFirst()){
            while (!cursor.isAfterLast()){
                list.add(new CheckOutItem(cursor.getInt(cursor.getColumnIndex(COL_ID)),
                        cursor.getString(cursor.getColumnIndex(COL_NAME)),
                        cursor.getInt(cursor.getColumnIndex(COL_COUNT)),
                        cursor.getDouble(cursor.getColumnIndex(COL_PRICE)),
                        cursor.getString(cursor.getColumnIndex(COL_IMG_URL))
                        ));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return list;
    }


    public void modifyCheckOutItemCount(int newCount, int itemId){
        SQLiteDatabase db = getWritableDatabase();
        if (newCount==0) {
            db.delete(CHECKOUT_TABLE_NAME,
                    COL_ITEM_ID+ " = ?",
                    new String[]{String.valueOf(itemId)});
        }else{
            ContentValues values = new ContentValues();
            values.put(COL_COUNT, newCount);
            db.update(CHECKOUT_TABLE_NAME,
                    values,
                    COL_ITEM_ID+" = ?",
                    new String[]{ String.valueOf(itemId) });
        }
        db.close();
    }

    public void clearCheckOut(){
        SQLiteDatabase db  = getWritableDatabase();
        db.delete(CHECKOUT_TABLE_NAME,null,null);
        db.close();
    }
}
