package com.joelimyx.subbox.history;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.joelimyx.subbox.R;

public class HistoryActivity extends AppCompatActivity{
    private boolean mIsLandscape;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("History");

        if (findViewById(R.id.is_history_landscape)==null){
            mIsLandscape = false;
        }else {
            mIsLandscape = true;
        }

        HistoryFragment fragment = HistoryFragment.newInstance(false,-1);
        getSupportFragmentManager().beginTransaction().replace(R.id.history_fragment_container,fragment).commit();
    }
    @Override
    protected void onResume() {
        if (mIsLandscape){
            setResult(RESULT_OK,null);
            finish();
        }
        super.onResume();
    }

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
