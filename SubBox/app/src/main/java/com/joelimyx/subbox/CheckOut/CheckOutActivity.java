package com.joelimyx.subbox.checkout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.joelimyx.subbox.R;
import com.joelimyx.subbox.mainlist.SubBoxAdapter;

public class CheckOutActivity extends AppCompatActivity{// implements CheckOutFragment.OnCheckoutItemSelectedListener{
    private boolean mIsLandScape;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(findViewById(R.id.is_checkout_landscape)==null){
            mIsLandScape =false;
        }else{
            mIsLandScape =true;
        }

        CheckOutFragment checkOutFragment = CheckOutFragment.newInstance(false);
        getSupportFragmentManager().beginTransaction().replace(R.id.checkout_fragment_container, checkOutFragment).commit();
    }

    //--------------------------------------------------------------------------------------------------------------------
    //Check to see if it is in two pane
    //--------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onResume() {
        if (mIsLandScape){
            setResult(RESULT_OK,null);
            finish();
        }
        super.onResume();
    }
//
//    @Override
//    public void onCheckoutItemSelect(int id) {
//
//    }
}
