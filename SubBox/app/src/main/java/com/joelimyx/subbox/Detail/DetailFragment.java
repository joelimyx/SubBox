package com.joelimyx.subbox.detail;

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


public class DetailFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";

    private int mIdSelected;

    //private OnFragmentInteractionListener mListener;

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

        SubBox subBox = SubBoxHelper.getsInstance(getContext()).getSubBoxByID(mIdSelected);

        //Reference
        ImageView detailImage  = (ImageView) view.findViewById(R.id.detail_image);
        TextView titleText = (TextView) view.findViewById(R.id.title_text);
        TextView priceText = (TextView) view.findViewById(R.id.detail_price_text);
        TextView detailText = (TextView) view.findViewById(R.id.detail_text);
        final Button detailButton = (Button) view.findViewById(R.id.detail_button);

        //Setup title and configure to automatically scroll sideway if too long
        titleText.setText(subBox.getName());
        titleText.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        titleText.setSingleLine(true);
        titleText.setMarqueeRepeatLimit(10);
        titleText.setSelected(true);

        Picasso.with(getContext()).load(subBox.getImgUrl()).resize(200,200).centerCrop().into(detailImage);

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault());
        double priceValue = subBox.getPrice();
        priceText.setText(currencyFormat.format(priceValue));

        detailText.setText(subBox.getDescription());

        //Change the text to done if it is already in the checkout
        if (SubBoxHelper.getsInstance(getContext()).isSubBoxInCheckOut(mIdSelected))
            detailButton.setText(R.string.detail_in_cart);

        detailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Add to checkout and change the text to done
                if(!SubBoxHelper.getsInstance(getContext()).isSubBoxInCheckOut(mIdSelected)){
                    SubBoxHelper.getsInstance(getContext()).addSubBoxToCheckOut(mIdSelected);
                    detailButton.setText(R.string.detail_in_cart);
                    Toast.makeText(getContext(), "Added to cart", Toast.LENGTH_SHORT).show();
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
