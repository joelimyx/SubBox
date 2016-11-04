package com.joelimyx.subbox.CheckOut;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.joelimyx.subbox.Classes.CheckOutItem;
import com.joelimyx.subbox.R;
import com.joelimyx.subbox.dbassethelper.SubBoxHelper;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;


public class CheckOutFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private int mCheckoutSelectedId;

    private OnCheckoutItemSelectedListener mListener;

    public CheckOutFragment() {
        // Required empty public constructor
    }

    public static CheckOutFragment newInstance(int param1) {
        CheckOutFragment fragment = new CheckOutFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCheckoutSelectedId = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_check_out, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<CheckOutItem> checkOutItems = SubBoxHelper.getsInstance(getContext()).getCheckoutList();

        RecyclerView recyclerview = (RecyclerView) view.findViewById(R.id.checkout_recyclerview);
        TextView subtotalText = (TextView) view.findViewById(R.id.subtotal_text);
        TextView taxText= (TextView) view.findViewById(R.id.tax_text);
        TextView totalText= (TextView) view.findViewById(R.id.total_text);

        LinearLayoutManager manager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerview.setLayoutManager(manager);
        recyclerview.setAdapter(new CheckOutAdapter(checkOutItems));

        double subTotal= 0d;
        for (CheckOutItem item: checkOutItems) {
            subTotal+=item.getSubtotalPrice();
        }
        double tax = subTotal*0.0875d;
        double total = subTotal+tax;


        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault());

        subtotalText.setText("Subtotal: "+currencyFormat.format(subTotal));
        taxText.setText("Tax: "+currencyFormat.format(tax));
        totalText.setText("Total: "+currencyFormat.format(total));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCheckoutItemSelectedListener) {
            mListener = (OnCheckoutItemSelectedListener) context;
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
    public interface OnCheckoutItemSelectedListener {
        // TODO: Update argument type and name
        void onCheckoutItemSelect(int id);
    }
}
