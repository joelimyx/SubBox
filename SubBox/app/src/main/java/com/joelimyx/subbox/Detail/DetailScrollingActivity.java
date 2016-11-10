package com.joelimyx.subbox.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.joelimyx.subbox.Classes.SubBox;
import com.joelimyx.subbox.checkout.CheckOutActivity;
import com.joelimyx.subbox.mainlist.SubBoxAdapter;
import com.joelimyx.subbox.R;
import com.joelimyx.subbox.dbassethelper.SubBoxHelper;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailScrollingActivity extends AppCompatActivity {
    @BindView(R.id.toolbar_image) ImageView mToolbarImage;
    private boolean mIsLandScape;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_scrolling);
        ButterKnife.bind(this);

        int id = getIntent().getIntExtra(SubBoxAdapter.SELECTED_ID,-1);

        if(findViewById(R.id.is_detail_landscape)==null){
            mIsLandScape =false;
        }else{
            mIsLandScape =true;
        }

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        Picasso.with(this)
                .load(SubBoxHelper.getsInstance(this).getSubBoxByID(id).getImgUrl())
                .fit()
                .into(mToolbarImage);

        DetailFragment detailFragment = DetailFragment.newInstance(id);
        getSupportFragmentManager().beginTransaction().replace(R.id.detail_fragment_container,detailFragment).commit();
    }

    //--------------------------------------------------------------------------------------------------------------------
    //Check to see if it is in two pane
    //--------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onResume() {
        if (mIsLandScape){
            Intent intent = new Intent();
            intent.putExtra("id",getIntent().getIntExtra(SubBoxAdapter.SELECTED_ID,-1));
            setResult(RESULT_OK,intent);
            finish();
        }
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail_scrolling,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.cart:
                Intent intent = new Intent(getApplicationContext(),CheckOutActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.checkout_scale_in,R.anim.no_animation);
                return true;
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.pop_in,R.anim.out_to_right);
                return true;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.pop_in,R.anim.out_to_right);
    }
}
