package com.joelimyx.subbox.mainlist;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.joelimyx.subbox.checkout.CheckOutActivity;
import com.joelimyx.subbox.checkout.CheckOutFragment;
import com.joelimyx.subbox.Classes.SubBox;
import com.joelimyx.subbox.detail.DetailFragment;
import com.joelimyx.subbox.detail.DetailScrollingActivity;
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
    public static final int DETAIL_REQUEST_CODE = 1;
    public static final int CHECKOUT_REQUEST_CODE = 2;

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

        mAdapter = new SubBoxAdapter(SubBoxHelper.getsInstance(this).getSubBoxList(),this,this);
        mRecyclerView.setAdapter(mAdapter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    //--------------------------------------------------------------------------------------------------------------------
    //Make sure the detail_or_checkout_container stay the same as it as it was(detail or checkout) before rotation
    //--------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(mTwoPane) {
            if (requestCode == DETAIL_REQUEST_CODE) {
                if (resultCode == RESULT_OK) {
                    int id = data.getIntExtra("id", -1);
                    DetailFragment detailFragment = DetailFragment.newInstance(id);
                    getSupportFragmentManager().beginTransaction().replace(R.id.detail_or_checkout_container, detailFragment).commit();
                }
            }else if(requestCode == CHECKOUT_REQUEST_CODE){
                if (resultCode == RESULT_OK){
                    CheckOutFragment checkOutFragment = new CheckOutFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.detail_or_checkout_container, checkOutFragment).commit();

                }
            }
        }
    }

    //--------------------------------------------------------------------------------------------------------------------
    //MENU AREA
    //--------------------------------------------------------------------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        //Search Service activator
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

        MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            //Restore list if back button is pressed in searchview
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                List<SubBox> restore = SubBoxHelper.getsInstance(getApplicationContext()).getSubBoxList();
                mAdapter.replaceData(restore);
                return true;
            }
        });

        switch (item.getItemId()){
            case R.id.price_sort:
                List<SubBox> restore = SubBoxHelper.getsInstance(getApplicationContext()).sortListByPrice();
                mAdapter.replaceData(restore);
                return true;

            //Menu item for cart to start DetailActivity or DetailFragment
            case R.id.cart:
                if(mTwoPane){
                    CheckOutFragment checkOutFragment = new CheckOutFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.detail_or_checkout_container,checkOutFragment).commit();
                }else {
                    Intent intent = new Intent(getApplicationContext(),CheckOutActivity.class);
                    startActivityForResult(intent,CHECKOUT_REQUEST_CODE);
                }
        }
        return true;
    }

    //--------------------------------------------------------------------------------------------------------------------
    //Handling search intent AREA
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
    //INTERFACE AREA
    //From SubBoxAdapter to DetailActivity or DetailFragment
    //--------------------------------------------------------------------------------------------------------------------
    @Override
    public void onItemSelected(int id) {

        if(mTwoPane){
            DetailFragment detailFragment = DetailFragment.newInstance(id);
            getSupportFragmentManager().beginTransaction().replace(R.id.detail_or_checkout_container,detailFragment).commit();
        }else {
            Intent intent = new Intent(this, DetailScrollingActivity.class);
            intent.putExtra(SubBoxAdapter.SELECTED_ID, id);
            startActivityForResult(intent,DETAIL_REQUEST_CODE);
        }
    }
}
