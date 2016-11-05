package com.joelimyx.subbox.Detail;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private int mIdSelected;

    private OnFragmentInteractionListener mListener;

    public DetailFragment() {
        // Required empty public constructor
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((DetailScrollingActivity)getActivity()).setSupportActionBar(toolbar);
        ((DetailScrollingActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView titleText = (TextView) view.findViewById(R.id.title_text);
        TextView priceText = (TextView) view.findViewById(R.id.detail_price_text);
        TextView detailText = (TextView) view.findViewById(R.id.detail_text);
        final FloatingActionButton detailFAB = (FloatingActionButton) view.findViewById(R.id.add_or_done_fab);

        SubBox subBox = SubBoxHelper.getsInstance(getContext()).getSubBoxByID(mIdSelected);

        titleText.setText(subBox.getName());


        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault());
        double priceValue = subBox.getPrice();
        priceText.setText(currencyFormat.format(priceValue));

        detailText.setText(subBox.getDescription());
        ((DetailScrollingActivity)getActivity()).getSupportActionBar().setTitle(subBox.getName());

        detailFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isAdded = SubBoxHelper.getsInstance(getContext()).addSubBoxToCheckOut(mIdSelected);
                detailFAB.setImageResource(R.drawable.ic_done);
                if (isAdded)
                    Snackbar.make(view,"Item Added to cart.", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCheckoutItemSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
