package com.joelimyx.subbox.CheckOut;

import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.joelimyx.subbox.Classes.CheckOutItem;
import com.joelimyx.subbox.R;
import com.joelimyx.subbox.dbassethelper.SubBoxHelper;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;


public class CheckOutFragment extends Fragment implements CheckOutAdapter.OnCheckOutItemModifyListener {

    private static final String ARG_PARAM1 = "param1";

    private int mCheckoutSelectedId;
    //private OnCheckoutItemSelectedListener mListener;

    TextView mSubtotalText,mTaxText, mTotalText;

    public CheckOutFragment() {
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
        return inflater.inflate(R.layout.fragment_check_out, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<CheckOutItem> checkOutItems = SubBoxHelper.getsInstance(getContext()).getCheckoutList();

        //Reference to fragment_check_out
        Button checkOutButton = (Button) view.findViewById(R.id.checkout_button);
        mSubtotalText = (TextView) view.findViewById(R.id.subtotal_text);
        mTaxText= (TextView) view.findViewById(R.id.tax_text);
        mTotalText= (TextView) view.findViewById(R.id.total_text);

        //Recycler View
        RecyclerView recyclerview = (RecyclerView) view.findViewById(R.id.checkout_recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        final CheckOutAdapter adapter = new CheckOutAdapter(checkOutItems,this);
        recyclerview.setAdapter(adapter);

        UpdateTotal();

        checkOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Proceed to Checkout?").setTitle("Checkout").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SubBoxHelper.getsInstance(getContext()).clearCheckOut();
                        adapter.clearCheckOutList();
                        Toast.makeText(getContext(), "Thank you for shopping at SubBox!!", Toast.LENGTH_LONG).show();
                    }
                }).setNegativeButton("Cancel",null).create().show();
            }
        });
    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnCheckoutItemSelectedListener) {
//            mListener = (OnCheckoutItemSelectedListener) context;
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

    /**--------------------------------------------------------------------------------------------------------------------
     * Helper method to update the total
     --------------------------------------------------------------------------------------------------------------------*/
    public void UpdateTotal(){
        List<CheckOutItem> checkOutItems = SubBoxHelper.getsInstance(getContext()).getCheckoutList();
        double subtotal= 0d;
        for (CheckOutItem item: checkOutItems) {
            subtotal+=item.getSubtotalPrice();
        }
        double tax = subtotal*0.0875d;
        double total = subtotal+tax;

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault());

        mSubtotalText.setText("Subtotal: "+currencyFormat.format(subtotal));
        mTaxText.setText("Tax: "+currencyFormat.format(tax));
        mTotalText.setText("Total: "+currencyFormat.format(total));
    }

    //--------------------------------------------------------------------------------------------------------------------
    //Interface AREA
    //--------------------------------------------------------------------------------------------------------------------
    @Override
    public void onCheckOutItemModify() {
        UpdateTotal();
    }


//    interface OnCheckoutItemSelectedListener {
//        // TODO: Update argument type and name
//        void onCheckoutItemSelect(int id);
//    }
}
