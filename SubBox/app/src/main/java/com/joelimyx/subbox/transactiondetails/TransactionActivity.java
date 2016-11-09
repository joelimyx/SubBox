package com.joelimyx.subbox.transactiondetails;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.joelimyx.subbox.R;

public class TransactionActivity extends AppCompatActivity {
    private boolean mIsLandscape;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Transaction Detail");

        if (findViewById(R.id.is_transaction_landscape)==null){
            mIsLandscape = false;
        }else {
            mIsLandscape = true;
        }

        TransactionFragment fragment = TransactionFragment.newInstance(getIntent().getIntExtra("id",-1));
        getSupportFragmentManager().beginTransaction().replace(R.id.transaction_container, fragment).commit();
    }
    @Override
    protected void onResume() {
        if (mIsLandscape){
            SharedPreferences sharepreference = getSharedPreferences("intercept id", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharepreference.edit();
            editor.putInt("id",getIntent().getIntExtra("id",-1));
            editor.commit();
            setResult(RESULT_OK,null);
            finish();
        }
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.pop_in,R.anim.out_to_right);
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.pop_in,R.anim.out_to_right);
    }
}
