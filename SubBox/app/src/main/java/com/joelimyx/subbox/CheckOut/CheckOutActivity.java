package com.joelimyx.subbox.checkout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.joelimyx.subbox.R;

public class CheckOutActivity extends AppCompatActivity{
    private boolean mIsLandScape;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Checkout");

        if(findViewById(R.id.is_checkout_landscape)==null){
            mIsLandScape = false;
        }else{
            mIsLandScape = true;
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

    //Go home when the up button is pressed and create an animation
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.no_animation, R.anim.checkout_scale_out);
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.no_animation, R.anim.checkout_scale_out);
    }
}
