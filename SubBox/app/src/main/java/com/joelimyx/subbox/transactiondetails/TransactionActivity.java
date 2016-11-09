package com.joelimyx.subbox.transactiondetails;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.joelimyx.subbox.R;

public class TransactionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Transaction Detail");

        TransactionFragment fragment = TransactionFragment.newInstance(getIntent().getIntExtra("id",-1));
        getSupportFragmentManager().beginTransaction().replace(R.id.transaction_container, fragment).commit();
    }

}
