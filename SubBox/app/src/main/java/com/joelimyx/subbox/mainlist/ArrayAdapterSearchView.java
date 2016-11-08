package com.joelimyx.subbox.mainlist;

import android.content.Context;
import android.support.v7.widget.SearchView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

/**
 * Created by Joe on 11/8/16.
 */

public class ArrayAdapterSearchView extends SearchView {
    private SearchView.SearchAutoComplete mSearchAutoComplete;

    public ArrayAdapterSearchView(Context context) {
        super(context);
        mSearchAutoComplete = (SearchAutoComplete) findViewById(android.support.v7.appcompat.R.id.search_src_text);
        this.setAdapter(null);
        this.setOnItemClickListener(null);
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener){
        mSearchAutoComplete.setOnItemClickListener(listener);
    }

    public void setAdapter(ArrayAdapter<?> adapter) {
        mSearchAutoComplete.setAdapter(adapter);
    }

    public void setText(String text){
        mSearchAutoComplete.setText(text);
    }
}
