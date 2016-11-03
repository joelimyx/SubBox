package com.joelimyx.subbox.Detail;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.joelimyx.subbox.Detail.DetailFragment;
import com.joelimyx.subbox.MainList.SubBoxAdapter;
import com.joelimyx.subbox.R;

public class DetailScrollingActivity extends AppCompatActivity implements DetailFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_scrolling);

        int id = getIntent().getIntExtra(SubBoxAdapter.SELECTED_ID,-1);

        DetailFragment detailFragment = DetailFragment.newInstance(id);

        getSupportFragmentManager().beginTransaction().replace(R.id.detail_fragment_container,detailFragment).commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
