package com.joelimyx.subbox.detail;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.joelimyx.subbox.R;
import com.joelimyx.subbox.Classes.SubBox;
import com.joelimyx.subbox.dbassethelper.SubBoxHelper;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DetailFragment extends Fragment {
    @BindView(R.id.detail_image) ImageView detailImage;
    @BindView(R.id.title_text) TextView mTitleText;
    @BindView(R.id.detail_price_text) TextView mPriceText;
    @BindView(R.id.detail_text) TextView mDetailText;
    @BindView(R.id.detail_button) Button mDetailButton;
    private static final String ARG_PARAM1 = "param1";
    private int mIdSelected;

    public DetailFragment() {
    }

    public static DetailFragment newInstance(int param1) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mIdSelected = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);

        DetailFragAsync task = new DetailFragAsync();
        task.execute(1);

        //Change the text to done if it is already in the checkout
        task = new DetailFragAsync();
        task.execute(2);

        task = new DetailFragAsync();
        task.execute(3);

    }
    class DetailFragAsync extends AsyncTask<Integer,Void,Object>{
        @Override
        protected Object doInBackground(Integer... integers) {
            switch (integers[0]){
                case 1:
                    return SubBoxHelper.getsInstance(getContext()).getSubBoxByID(mIdSelected);
                case 2:
                    return SubBoxHelper.getsInstance(getContext()).isSubBoxInCheckOut(mIdSelected);
                case 3:
                    if(!SubBoxHelper.getsInstance(getContext()).isSubBoxInCheckOut(mIdSelected)) {
                        SubBoxHelper.getsInstance(getContext()).addSubBoxToCheckOut(mIdSelected);
                        return 1;
                    }else
                        return 0;
            }
            return null;
        }

        @Override
        protected void onPostExecute(final Object o) {
            super.onPostExecute(o);
            if (o instanceof SubBox){
                //Setup title and configure to automatically scroll sideway if too long
                mTitleText.setText(((SubBox)o).getName());
                mTitleText.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                mTitleText.setSingleLine(true);
                mTitleText.setMarqueeRepeatLimit(10);
                mTitleText.setSelected(true);

                Picasso.with(getContext()).load(((SubBox)o).getImgUrl()).resize(200,200).centerCrop().into(detailImage);

                NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault());
                double priceValue = ((SubBox)o).getPrice();
                mPriceText.setText(currencyFormat.format(priceValue));

                mDetailText.setText(((SubBox)o).getDescription());
            }else if (o instanceof Integer) {
                mDetailButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Add to checkout and change the text to done
                        if (((int)o)==1) {
                            mDetailButton.setText(R.string.detail_in_cart);
                            Toast.makeText(getContext(), "Added to cart", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }else if (((boolean)o)){
                mDetailButton.setText(R.string.detail_in_cart);
            }
        }
    }
}
