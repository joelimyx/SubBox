package com.joelimyx.subbox.Detail;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.joelimyx.subbox.R;
import com.joelimyx.subbox.Classes.SubBox;
import com.joelimyx.subbox.dbassethelper.SubBoxHelper;

import java.text.NumberFormat;
import java.util.Locale;


public class DetailFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private int mIdSelected;
    private boolean mIsTwoPane;

    //private OnFragmentInteractionListener mListener;

    public DetailFragment() {
    }

    public static DetailFragment newInstance(int param1, boolean param2) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putBoolean(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mIdSelected = getArguments().getInt(ARG_PARAM1);
            mIsTwoPane= getArguments().getBoolean(ARG_PARAM2);
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

        SubBox subBox = SubBoxHelper.getsInstance(getContext()).getSubBoxByID(mIdSelected);
        Log.d("TwoPane", "onViewCreated:");

        if (!mIsTwoPane) {
            //Toolbar
            Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
            ((DetailScrollingActivity) getActivity()).setSupportActionBar(toolbar);
            ((DetailScrollingActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((DetailScrollingActivity) getActivity()).getSupportActionBar().setTitle(subBox.getName());
        }

        //Reference
        TextView titleText = (TextView) view.findViewById(R.id.title_text);
        TextView priceText = (TextView) view.findViewById(R.id.detail_price_text);
        TextView detailText = (TextView) view.findViewById(R.id.detail_text);
        final FloatingActionButton detailFAB = (FloatingActionButton) view.findViewById(R.id.add_or_done_fab);

        titleText.setText(subBox.getName());

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault());
        double priceValue = subBox.getPrice();
        priceText.setText(currencyFormat.format(priceValue));

        detailText.setText(subBox.getDescription());

        //Change the icon to done if it is in the checkout
        if (SubBoxHelper.getsInstance(getContext()).isSubBoxInCheckOut(mIdSelected))
            detailFAB.setImageResource(R.drawable.ic_done);

        detailFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Add to checkout and change the icon to done
                if(!SubBoxHelper.getsInstance(getContext()).isSubBoxInCheckOut(mIdSelected)){
                    SubBoxHelper.getsInstance(getContext()).addSubBoxToCheckOut(mIdSelected);
                    detailFAB.setImageResource(R.drawable.ic_done);
                    Snackbar.make(view, "Item added to cart.", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }
    /**
     * TO BE CONTINUE
     */
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnCheckoutItemSelectedListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    public interface OnFragmentInteractionListener {
//        void onFragmentInteraction(Uri uri);
//    }
}
