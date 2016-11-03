package com.joelimyx.subbox.CheckOut;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.joelimyx.subbox.Classes.CheckOutItem;
import com.joelimyx.subbox.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Joe on 11/2/16.
 */

public class CheckOutAdapter extends RecyclerView.Adapter<CheckOutAdapter.CheckOutViewHolder> {

    List<CheckOutItem> mCheckOutItems;

    public CheckOutAdapter(List<CheckOutItem> checkOutItems) {
        mCheckOutItems = checkOutItems;
    }

    @Override
    public CheckOutViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CheckOutViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.checkout_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(CheckOutViewHolder holder, int position) {
        holder.mCheckoutTitle.setText(mCheckOutItems.get(position).getName());
        double total = mCheckOutItems.get(position).getSubtotalPrice()* mCheckOutItems.get(position).getCount();
        holder.mCheckoutPrice.setText("$"+total);
        holder.mCountText.setText(mCheckOutItems.get(position).getCount());
        //Image or title onclick

    }

    @Override
    public int getItemCount() {
        return mCheckOutItems.size();
    }

    static class CheckOutViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.checkout_title_text) TextView mCheckoutTitle;
        @BindView(R.id.checkout_price_text) TextView mCheckoutPrice;
        @BindView(R.id.item_count_text) TextView mCountText;
        public CheckOutViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
