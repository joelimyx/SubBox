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

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TransactionFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";

    private int mHistoryItemSelectedId;

    public TransactionFragment() {}

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
        double date = SubBoxHelper.getsInstance(getContext()).getTransactionDateByID(mHistoryItemSelectedId);
        List<CheckOutItem> transactionList = SubBoxHelper.getsInstance(getContext()).getTransactionDetail(mHistoryItemSelectedId);

        //Recycler View
        RecyclerView recyclerview = (RecyclerView) view.findViewById(R.id.transaction_recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        final TransactionAdapter adapter = new TransactionAdapter(transactionList,getContext());
        recyclerview.setAdapter(adapter);

        //Reference to fragment transaction
        TextView subtotalText = (TextView) view.findViewById(R.id.transaction_subtotal_text);
        TextView taxText= (TextView) view.findViewById(R.id.transaction_tax_text);
        TextView totalText = (TextView) view.findViewById(R.id.transaction_total_text);
        TextView dateText = (TextView) view.findViewById(R.id.transaction_date_text);

        //Calculation
        double subtotal=
                SubBoxHelper.getsInstance(getContext()).getSubtotal(
                        SubBoxHelper.getsInstance(getContext()).getTransactionDetail(mHistoryItemSelectedId));
        double tax = subtotal*0.0875d;
        double total = subtotal+tax;

        //Format with locale and setText
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault());

        subtotalText.setText("Subtotal: "+currencyFormat.format(subtotal));
        taxText.setText("Tax: "+currencyFormat.format(tax));
        totalText.setText("Total: "+currencyFormat.format(total));

        //Date formatter with locale
        DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis((long)date);
        dateText.setText(
                formatter.format(calendar.getTime()) );
    }
}
