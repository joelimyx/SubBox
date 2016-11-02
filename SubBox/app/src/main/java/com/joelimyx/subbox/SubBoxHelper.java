package com.joelimyx.subbox;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+ITEM_TABLE_NAME);
        this.onCreate(db);
    }

    public List<SubBox> getList(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                ITEM_TABLE_NAME,
                new String[]{COL_NAME,COL_PRICE,COL_DETAIL},
                null,null,null,null,null,null);
        List<SubBox> list = new ArrayList<>();
        if (cursor.moveToFirst()){
            while (!cursor.isAfterLast()){
                list.add(new SubBox(
                        cursor.getString(cursor.getColumnIndex(COL_NAME)),
                        cursor.getDouble(cursor.getColumnIndex(COL_PRICE)),
                        cursor.getString(cursor.getColumnIndex(COL_DETAIL))));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return list;
    }
}