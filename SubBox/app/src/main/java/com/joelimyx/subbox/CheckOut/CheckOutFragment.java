package com.joelimyx.subbox.checkout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.joelimyx.subbox.Classes.CheckOutItem;
import com.joelimyx.subbox.R;
import com.joelimyx.subbox.dbassethelper.SubBoxHelper;
import com.joelimyx.subbox.detail.DetailFragment;
import com.joelimyx.subbox.detail.DetailScrollingActivity;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;


public class CheckOutFragment extends Fragment
        implements CheckOutAdapter.OnCheckOutItemModifyListener,CheckOutAdapter.OnCheckOutItemSelectedListener {

    private static final String ARG_PARAM1 = "param1";

    private boolean mIsTwoPane;

    TextView mSubtotalText,mTaxText, mTotalText;

    public CheckOutFragment() {
    }

    public static CheckOutFragment newInstance(boolean param1) {
        CheckOutFragment fragment = new CheckOutFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mIsTwoPane = getArguments().getBoolean(ARG_PARAM1);
        }else{
            mIsTwoPane = true;
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
        final CheckOutAdapter adapter = new CheckOutAdapter(checkOutItems,this,this,getContext());
        recyclerview.setAdapter(adapter);

        UpdateTotal();

        checkOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Proceed to Checkout?").setTitle("Checkout").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //Confirm for checkOut
                        SubBoxHelper.getsInstance(getContext()).clearCheckOut();
                        adapter.clearCheckOutList();
                        UpdateTotal();
                        Toast.makeText(getContext(), "Thank you for shopping at SubBox!!", Toast.LENGTH_LONG).show();

                    }
                }).setNegativeButton("Cancel",null).create().show();
            }
        });
    }

    /**--------------------------------------------------------------------------------------------------------------------
     * Helper method to update the total of subtotal textview, tax textview, and total textview
     --------------------------------------------------------------------------------------------------------------------*/
    public void UpdateTotal(){
        double subtotal=
                SubBoxHelper.getsInstance(getContext()).getSubtotal(
                SubBoxHelper.getsInstance(getContext()).getCheckoutList());
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

    @Override
    public void onCheckOutItemSelected(int id) {

        //If it is two pane, start fragment instead of a new activity of detail view of the item selected
        if (mIsTwoPane){
            DetailFragment fragment = DetailFragment.newInstance(id);
            getFragmentManager().beginTransaction().replace(R.id.detail_or_checkout_container,fragment).addToBackStack(null).commit();
        }else{
            Intent intent = new Intent(getContext(), DetailScrollingActivity.class);
            intent.putExtra("id",id);
            startActivity(intent);
        }
    }
}
