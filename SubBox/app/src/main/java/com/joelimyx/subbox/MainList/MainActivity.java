package com.joelimyx.subbox.mainlist;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.joelimyx.subbox.Classes.CheckOutItem;
import com.joelimyx.subbox.Classes.HistoryItem;
import com.joelimyx.subbox.checkout.CheckOutActivity;
import com.joelimyx.subbox.checkout.CheckOutFragment;
import com.joelimyx.subbox.Classes.SubBox;
import com.joelimyx.subbox.detail.DetailFragment;
import com.joelimyx.subbox.detail.DetailScrollingActivity;
import com.joelimyx.subbox.R;
import com.joelimyx.subbox.dbassethelper.DBAssetHelper;
import com.joelimyx.subbox.dbassethelper.SubBoxHelper;
import com.joelimyx.subbox.history.HistoryActivity;
import com.joelimyx.subbox.history.HistoryFragment;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.IllegalFormatCodePointException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements SubBoxAdapter.OnItemSelectedListener {
    @BindView(R.id.recyclerview) RecyclerView mRecyclerView;
    @BindView(R.id.progressbar) ProgressBar mProgressBar;
    ArrayAdapterSearchView mArrayAdapterSearchView;
    ArrayAdapter<String> mArrayAdapter;
    FrameLayout container;
    SubBoxAdapter mAdapter;
    private boolean mTwoPane;
    public static final int DETAIL_REQUEST_CODE = 1;
    public static final int CHECKOUT_REQUEST_CODE = 2;
    public static final int HISTORY_REQUEST_CODE = 3;
    private MainAsyncTask mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        container = (FrameLayout) findViewById(R.id.detail_or_checkout_container);
        mTwoPane = container!=null;

        DBAssetHelper dbSetup = new DBAssetHelper(MainActivity.this);
        dbSetup.getWritableDatabase();

        mRecyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this,2, LinearLayoutManager.VERTICAL,false));
        mTask = new MainAsyncTask();
        mTask.execute(new Object());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    //--------------------------------------------------------------------------------------------------------------------
    //Make sure the detail_or_checkout_container stay the same as it as it(detail or checkout) was before rotation
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
            }//Intercept the transaction selected if from transaction activity when turn to landscape using Sharedpreferences
            else if(requestCode == HISTORY_REQUEST_CODE){
                if (resultCode == RESULT_OK){
                    SharedPreferences preference = getSharedPreferences("intercept id",MODE_PRIVATE);
                    int id = preference.getInt("id", -1);
                    if (id==-1){
                        HistoryFragment historyFragment= new HistoryFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.detail_or_checkout_container, historyFragment).commit();
                    }else{
                        HistoryFragment historyFragment= HistoryFragment.newInstance(true,id);
                        getSupportFragmentManager().beginTransaction().replace(R.id.detail_or_checkout_container, historyFragment).commit();
                    }//else id
                }//if result
            }//if request code
        }//if two pane
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

        mArrayAdapterSearchView = (ArrayAdapterSearchView) MenuItemCompat.getActionView(menu.findItem(R.id.search));

        ComponentName componentName = new ComponentName(this,MainActivity.class);
        mArrayAdapterSearchView.setSearchableInfo(searchManager.getSearchableInfo(componentName));

        //Autocomplete AREA
        AutoCompleteAsyncTask task = new AutoCompleteAsyncTask();
        task.execute();

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
                mTask = new MainAsyncTask();
                mTask.execute(new Object());
                return true;
            }
        });

        switch (item.getItemId()){
            case R.id.price_sort:
                mTask = new MainAsyncTask();
                mTask.execute(new HistoryItem(1,2));
                return true;

            //Menu item for cart to start CheckOut Activity or CheckOut Fragment
            case R.id.cart:
                if(mTwoPane){
                    CheckOutFragment checkOutFragment = new CheckOutFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.detail_or_checkout_container,checkOutFragment).commit();
                }else {
                    Intent intent = new Intent(getApplicationContext(),CheckOutActivity.class);
                    startActivityForResult(intent,CHECKOUT_REQUEST_CODE);
                    overridePendingTransition(R.anim.checkout_scale_in,R.anim.no_animation);
                }
                return true;
            case R.id.filter:

                //Setup multiple checkbox for type filter in dialog
                final String [] type = getResources().getStringArray(R.array.type);
                final List<String> selectedType = new ArrayList<>();
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMultiChoiceItems(type, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean isChecked) {
                        if (isChecked){
                            selectedType.add(type[i]);
                        }else if(selectedType.contains(type[i])){
                            selectedType.remove(type[i]);
                        }
                    }
                })
                //Setup Positive and negative button in dialog
                .setPositiveButton("Filter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (selectedType.size()!=0) {
                            mTask = new MainAsyncTask();
                            mTask.execute(selectedType);
                        }
                    }
                }).setNegativeButton("Cancel", null).create().show();

                return true;
            //Go to history
            case R.id.history:
                if(mTwoPane){
                    HistoryFragment historyFragment = new HistoryFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.detail_or_checkout_container,historyFragment).commit();
                }else {
                    Intent intent = new Intent(this, HistoryActivity.class);
                    startActivityForResult(intent,HISTORY_REQUEST_CODE);
                    overridePendingTransition(R.anim.checkout_scale_in,R.anim.no_animation);
                }
                return true;
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
            mTask = new MainAsyncTask();
            mTask.execute(query);
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
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction().replace(R.id.detail_or_checkout_container,detailFragment);
            ft.addToBackStack(null).commit();
        }else {
            Intent intent = new Intent(this, DetailScrollingActivity.class);
            intent.putExtra(SubBoxAdapter.SELECTED_ID, id);
            startActivityForResult(intent,DETAIL_REQUEST_CODE);
            overridePendingTransition(R.anim.in_from_right,R.anim.fade_out);
        }
    }

    //--------------------------------------------------------------------------------------------------------------------
    //AsyncArea AREA
    //--------------------------------------------------------------------------------------------------------------------
    class MainAsyncTask extends AsyncTask<Object,Void,List<SubBox> >{

        @Override
        protected List<SubBox> doInBackground(Object... objects) {
            if (objects[0] instanceof HistoryItem) {
                return SubBoxHelper.getsInstance(getApplicationContext()).sortListByPrice();
            }else if (objects[0] instanceof List) {
                List<SubBox> temp = SubBoxHelper.getsInstance(getApplicationContext()).getFilteredList((List<String>)objects[0]);
                ((List<String>)objects[0]).clear();
                return temp;
            }else if (objects[0] instanceof String) {
                return SubBoxHelper.getsInstance(MainActivity.this).getSearchList((String) objects[0]);
            }else {
                return SubBoxHelper.getsInstance(MainActivity.this).getSubBoxList();
            }
        }

        @Override
        protected void onPostExecute(List<SubBox> subBoxes) {
            super.onPostExecute(subBoxes);
            mAdapter = new SubBoxAdapter(subBoxes,MainActivity.this,MainActivity.this);
            mAdapter.replaceData(subBoxes);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    class AutoCompleteAsyncTask extends AsyncTask<Void,Void,ArrayAdapter>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayAdapter doInBackground(Void... voids) {
            return new ArrayAdapter<>(
                    MainActivity.this,R.layout.custom_autocomplete,
            SubBoxHelper.getsInstance(MainActivity.this).getTitleList());
        }

        @Override
        protected void onPostExecute(ArrayAdapter arrayAdapter) {
            super.onPostExecute(arrayAdapter);
            mArrayAdapter = arrayAdapter;
            mArrayAdapterSearchView.setAdapter(mArrayAdapter);
            mArrayAdapterSearchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    mArrayAdapterSearchView.setText(mArrayAdapter.getItem(i));
                }
            });
        }
    }

}
