package com.joelimyx.subbox.history;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.joelimyx.subbox.Classes.HistoryItem;
import com.joelimyx.subbox.R;
import com.joelimyx.subbox.dbassethelper.SubBoxHelper;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Joe on 11/8/16.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private List<HistoryItem> mHistoryItems;
    private OnHistoryItemSelectedListener mListener;
    Context mContext;

    interface OnHistoryItemSelectedListener{
        void onHistoryItemSelected(int id);
    }

    public HistoryAdapter(List<HistoryItem> historyItems, OnHistoryItemSelectedListener listener,Context context) {
        mHistoryItems = historyItems;
        mListener = listener;
        mContext = context;
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HistoryViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_history,parent,false));
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, final int position) {
        //Set Date with format depding on the locale
        DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mHistoryItems.get(position).getDate());
        holder.mHistoryDate.setText(
                formatter.format(calendar.getTime()) );

        //Set price according to locale

        double subtotal=
                SubBoxHelper.getsInstance(mContext).getSubtotal(
                        SubBoxHelper.getsInstance(mContext).getTransactionDetail(
                                mHistoryItems.get(position).getId()));
        double total = (subtotal*0.0875d)+subtotal;
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault());
        holder.mTotalText.setText(currencyFormat.format(total));

        holder.mHistoryCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onHistoryItemSelected(mHistoryItems.get(position).getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mHistoryItems.size();
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.history_cardview) CardView mHistoryCardView;
        @BindView(R.id.history_total_text) TextView mTotalText;
        @BindView(R.id.history_date_text) TextView mHistoryDate;
        private HistoryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
