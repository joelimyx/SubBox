package com.joelimyx.subbox.CheckOut;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.joelimyx.subbox.R;

public class CheckOutActivity extends AppCompatActivity implements CheckOutFragment.OnCheckoutItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CheckOutFragment checkOutFragment = new CheckOutFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.checkout_fragment_container, checkOutFragment).commit();
    }

    @Override
    public void onCheckoutItemSelect(int id) {

    }
}
