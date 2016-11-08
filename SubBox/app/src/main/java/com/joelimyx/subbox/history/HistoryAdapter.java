package com.joelimyx.subbox.history;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.joelimyx.subbox.Classes.HistoryItem;
import com.joelimyx.subbox.R;

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

    public HistoryAdapter(List<HistoryItem> historyItems) {
        mHistoryItems = historyItems;
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HistoryViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_history,parent,false));
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {
        DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mHistoryItems.get(position).getDate());
        holder.mHistoryDate.setText(
                formatter.format(calendar.getTime()) );

        double total = mHistoryItems.get(position).getSubtotal()*0.875+mHistoryItems.get(position).getSubtotal();
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault());
        holder.mTotalText.setText(currencyFormat.format(total));

    }

    @Override
    public int getItemCount() {
        return mHistoryItems.size();
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.history_cardview) CardView mHistoryCardView;
        @BindView(R.id.history_total_text) TextView mTotalText;
        @BindView(R.id.history_date_text) TextView mHistoryDate;
        public HistoryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
