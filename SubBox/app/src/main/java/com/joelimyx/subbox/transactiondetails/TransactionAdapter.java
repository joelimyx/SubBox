package com.joelimyx.subbox.transactiondetails;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.joelimyx.subbox.Classes.CheckOutItem;
import com.joelimyx.subbox.R;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Joe on 11/9/16.
 */

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private List<CheckOutItem> mTransactionItems;
    private Context mContext;

    public TransactionAdapter(List<CheckOutItem> transactionItems, Context context) {
        mTransactionItems = transactionItems;
        mContext = context;
    }

    @Override
    public TransactionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TransactionViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_transaction,parent,false));
    }

    @Override
    public void onBindViewHolder(TransactionViewHolder holder, int position) {
        Picasso.with(mContext)
                .load(mTransactionItems.get(position).getImgUrl())
                .resize(200,200).centerCrop()
                .into(holder.mTransactionImage);
        holder.mTransactionTitle.setText(mTransactionItems.get(position).getName());

        double total = mTransactionItems.get(position).getSubtotalPrice();
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault());
        holder.mTransactionPrice.setText(currencyFormat.format(total));

        holder.mTransactionCount.setText(String.valueOf(mTransactionItems.get(position).getCount()));

    }

    @Override
    public int getItemCount() {
        return mTransactionItems.size();
    }

    static class TransactionViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.transaction_image) ImageView mTransactionImage;
        @BindView(R.id.transaction_title_text) TextView mTransactionTitle;
        @BindView(R.id.transaction_price_text) TextView mTransactionPrice;
        @BindView(R.id.transaction_count_text) TextView mTransactionCount;
        public TransactionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
