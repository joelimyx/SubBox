package com.joelimyx.subbox.MainList;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.joelimyx.subbox.CheckOut.CheckOutActivity;
import com.joelimyx.subbox.CheckOut.CheckOutFragment;
import com.joelimyx.subbox.Classes.SubBox;
import com.joelimyx.subbox.Detail.DetailFragment;
import com.joelimyx.subbox.Detail.DetailScrollingActivity;
import com.joelimyx.subbox.R;
import com.joelimyx.subbox.dbassethelper.DBAssetHelper;
import com.joelimyx.subbox.dbassethelper.SubBoxHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements SubBoxAdapter.OnItemSelectedListener{
    @BindView(R.id.recyclerview) RecyclerView mRecyclerView;
    FrameLayout container;
    SubBoxAdapter mAdapter;
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        container = (FrameLayout) findViewById(R.id.detail_or_checkout_container);
        mTwoPane = container!=null;

        DBAssetHelper dbSetup = new DBAssetHelper(MainActivity.this);
        dbSetup.getReadableDatabase();

        mRecyclerView.setLayoutManager(new GridLayoutManager(this,2, LinearLayoutManager.VERTICAL,false));

        mAdapter = new SubBoxAdapter(SubBoxHelper.getsInstance(this).getSubBoxList(),this);
        mRecyclerView.setAdapter(mAdapter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mTwoPane){
                    CheckOutFragment checkOutFragment = new CheckOutFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.detail_or_checkout_container,checkOutFragment).commit();
                }else {
                    Intent intent = new Intent(getApplicationContext(),CheckOutActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    //--------------------------------------------------------------------------------------------------------------------
    //Menu AREA
    //--------------------------------------------------------------------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        ComponentName componentName = new ComponentName(this,MainActivity.class);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                List<SubBox> restore = SubBoxHelper.getsInstance(getApplicationContext()).getSubBoxList();
                mAdapter.replaceData(restore);
                return true;
            }
        });

        //noinspection SimplifiableIfStatement
        if (id == R.id.price_sort) {
            List<SubBox> restore = SubBoxHelper.getsInstance(getApplicationContext()).sortListByPrice();
            mAdapter.replaceData(restore);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //--------------------------------------------------------------------------------------------------------------------
    //Handling search intent
    //--------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);

    }

    private void handleIntent(Intent intent){

        if(Intent.ACTION_SEARCH.equals(intent.getAction())){
            String query = intent.getStringExtra(SearchManager.QUERY);
            List<SubBox> searchList = SubBoxHelper.getsInstance(this).getSearchList(query);

            mAdapter.replaceData(searchList);
        }
    }


    //--------------------------------------------------------------------------------------------------------------------
    //Interface Area
    //--------------------------------------------------------------------------------------------------------------------
    @Override
    public void onItemSelected(int id) {

        if(mTwoPane){
            DetailFragment detailFragment = DetailFragment.newInstance(id,true);

        }else {
            Intent intent = new Intent(this, DetailScrollingActivity.class);
            intent.putExtra(SubBoxAdapter.SELECTED_ID, id);
            startActivity(intent);
        }
    }
}
