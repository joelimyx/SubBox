package com.joelimyx.subbox.transactiondetails;

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

public class TransactionFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";

    private int mHistoryItemSelectedId;

    public TransactionFragment() {
        // Required empty public constructor
    }

    public static TransactionFragment newInstance(int param1) {
        TransactionFragment fragment = new TransactionFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mHistoryItemSelectedId = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_transaction, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        double date = SubBoxHelper.getsInstance(getContext()).getTransactionDate(mHistoryItemSelectedId);
        List<CheckOutItem> transactionList = SubBoxHelper.getsInstance(getContext()).getTransactionDetail(mHistoryItemSelectedId);

        //Recycler View
        RecyclerView recyclerview = (RecyclerView) view.findViewById(R.id.transaction_recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        final TransactionAdapter adapter = new TransactionAdapter(transactionList,getContext());
        recyclerview.setAdapter(adapter);

        //Reference to fragment transaction
        TextView mSubtotalText = (TextView) view.findViewById(R.id.transaction_subtotal_text);
        TextView mTaxText= (TextView) view.findViewById(R.id.transaction_tax_text);
        TextView mTotalText = (TextView) view.findViewById(R.id.transaction_total_text);

        double subtotal=
                SubBoxHelper.getsInstance(getContext()).getSubtotal(
                        SubBoxHelper.getsInstance(getContext()).getTransactionDetail(mHistoryItemSelectedId));
        double tax = subtotal*0.0875d;
        double total = subtotal+tax;

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault());

        mSubtotalText.setText("Subtotal: "+currencyFormat.format(subtotal));
        mTaxText.setText("Tax: "+currencyFormat.format(tax));
        mTotalText.setText("Total: "+currencyFormat.format(total));
    }
}
