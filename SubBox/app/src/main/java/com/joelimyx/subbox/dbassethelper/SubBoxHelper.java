package com.joelimyx.subbox.dbassethelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.joelimyx.subbox.Classes.CheckOutItem;
import com.joelimyx.subbox.Classes.HistoryItem;
import com.joelimyx.subbox.Classes.SubBox;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Joe on 10/31/16.
 */

public class SubBoxHelper extends SQLiteOpenHelper {
    private static SubBoxHelper sInstance;
    private static final String DATABASE_NAME = "subbox.db";
    private static final int DATABASE_VERSION = 2;

    private static final String ITEM_TABLE_NAME = "item_table";
    private static final String COL_ID = "id";
    private static final String COL_NAME = "name";
    private static final String COL_PRICE = "price";
    private static final String COL_DETAIL = "detail";
    private static final String COL_IMG_URL= "img_url";
    private static final String COL_TYPE = "type";
    private static final String [] COL_ITEMS_SELECTION = new String[]{COL_ID,COL_NAME,COL_PRICE,COL_DETAIL,COL_IMG_URL,COL_TYPE};
    private static final String CREATE_ITEM_TABLE =
            "CREATE TABLE "+ITEM_TABLE_NAME+" ( "+
            COL_ID+ " INTEGER PRIMARY KEY, "+
            COL_NAME+" TEXT, "+
            COL_PRICE+" REAL,"+
            COL_DETAIL+" TEXT, "+
            COL_IMG_URL+" TEXT,"+
            COL_TYPE+" TEXT )";

    private static final String CHECKOUT_TABLE_NAME = "checkout_table";
    private static final String COL_ITEM_ID ="item_id";
    private static final String COL_COUNT = "count";
    private static final String CREATE_CHECKOUT_TABLE =
            "CREATE TABLE "+ CHECKOUT_TABLE_NAME +" ( "+
            COL_ID+ " INTEGER PRIMARY KEY, "+
            COL_ITEM_ID+ " INTEGER, "+
            COL_COUNT+ " INTEGER )";

    private static final String HISTORY_TABLE_NAME = "history_table";
    private static final String COL_DATE = "date";
    private static final String COL_SUBTOTAL = "subtotal";
    private static final String CREATE_HISTORY_TABLE =
            "CREATE TABLE "+HISTORY_TABLE_NAME+" ( "+
            COL_ID+" INTEGER PRIMARY KEY,"+
            COL_DATE+" REAL, "+
            COL_SUBTOTAL +" REAL )";

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
        db.execSQL(CREATE_HISTORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+ITEM_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ CHECKOUT_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+HISTORY_TABLE_NAME);
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
                        cursor.getString(cursor.getColumnIndex(COL_IMG_URL))
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
                        cursor.getString(cursor.getColumnIndex(COL_IMG_URL))
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
                        cursor.getString(cursor.getColumnIndex(COL_IMG_URL))
                        ));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return list;
    }

    public List<String> getTitleList(){
        SQLiteDatabase db  = getReadableDatabase();
        Cursor cursor = db.query(
                ITEM_TABLE_NAME,
                new String[]{COL_NAME},
                null,null,
                null,null,null,null);
        List<String> list = new ArrayList<>();
        if (cursor.moveToFirst()){
            while (!cursor.isAfterLast()){
                list.add(cursor.getString(cursor.getColumnIndex(COL_NAME)));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return list;
    }

    public List<SubBox> getFilteredList(List<String> types){
        SQLiteDatabase db = getReadableDatabase();
        //Convert the list into array
        String [] query = types.toArray(new String[types.size()]);

        //Use IN OPERATOR FOR MULTIPLE SELECTION
        Cursor cursor = db.rawQuery("SELECT * FROM "+ITEM_TABLE_NAME+" WHERE "+
                COL_TYPE+" IN ("+createPlaceHolderForQuery(query.length)+")",query);
        List<SubBox> list = new ArrayList<>();
        if (cursor.moveToFirst()){
            while (!cursor.isAfterLast()){
                list.add(new SubBox(
                        cursor.getString(cursor.getColumnIndex(COL_NAME)),
                        cursor.getDouble(cursor.getColumnIndex(COL_PRICE)),
                        cursor.getString(cursor.getColumnIndex(COL_DETAIL)),
                        cursor.getInt(cursor.getColumnIndex(COL_ID)),
                        cursor.getString(cursor.getColumnIndex(COL_IMG_URL))
                ));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return list;
    }

    /**
     * Helper method to dynamically add the ? place holder for IN OPERATOR
     * @param length = length of the array
     * @return place holder for IN OPERATOR
     */
    private String createPlaceHolderForQuery(int length){
        if (length<=0){
            return "";
        }
        StringBuilder sb = new StringBuilder(length);
        sb.append("?");
        for (int i = 1; i < length ; i++) {
            sb.append(",?");
        }

        return sb.toString();
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
                    cursor.getString(cursor.getColumnIndex(COL_IMG_URL))
                    );
            cursor.close();
            return subBox;
        }else {
            cursor.close();
            return null;
        }
    }

    /**
     * @return true if added for Toast to show up in DetailFragment
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

    public double getSubtotal(){
        List<CheckOutItem> checkOutItems = getCheckoutList();
        double subtotal= 0d;
        for (CheckOutItem item: checkOutItems) {
            subtotal+=item.getSubtotalPrice();
        }
        return subtotal;
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
        ContentValues values = new ContentValues();
        values.put("date", Calendar.getInstance().getTimeInMillis());
        values.put("subtotal",getSubtotal());
        db.insert(HISTORY_TABLE_NAME,null,values);
        db.delete(CHECKOUT_TABLE_NAME,null,null);
        db.close();
    }
    //--------------------------------------------------------------------------------------------------------------------
    //History AREA
    //--------------------------------------------------------------------------------------------------------------------
    public List<HistoryItem> getHistoryList(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT "+HISTORY_TABLE_NAME+"."+COL_ID+", "+COL_DATE+", "+ COL_SUBTOTAL +
                        " FROM "+HISTORY_TABLE_NAME,null);
        List<HistoryItem> list = new ArrayList<>();
        if (cursor.moveToFirst()){
            while (!cursor.isAfterLast()){
                list.add(new HistoryItem(cursor.getInt(cursor.getColumnIndex(COL_ID)),
                        cursor.getLong(cursor.getColumnIndex(COL_DATE)),
                        cursor.getLong(cursor.getColumnIndex(COL_SUBTOTAL))
                ));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return list;
    }

}
