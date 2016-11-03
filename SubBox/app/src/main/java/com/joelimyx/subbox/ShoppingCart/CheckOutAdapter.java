package com.joelimyx.subbox.ShoppingCart;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.joelimyx.subbox.Classes.SubBox;
import com.joelimyx.subbox.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Joe on 11/2/16.
 */

public class CheckOutAdapter extends RecyclerView.Adapter<CheckOutAdapter.CheckOutViewHolder> {

    List<SubBox> mSubBoxes;

    public CheckOutAdapter(List<SubBox> subBoxes) {
        mSubBoxes = subBoxes;
    }

    @Override
    public CheckOutViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CheckOutViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.checkout_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(CheckOutViewHolder holder, int position) {
        holder.mCheckoutTitle.setText(mSubBoxes.get(position).getName());
        double total = mSubBoxes.get(position).getPrice()*mSubBoxes.get(position).getCount();
        holder.mCheckoutPrice.setText("$"+total);
        holder.mCountText.setText(mSubBoxes.get(position).getCount());
        //Image or title onclick

    }

    @Override
    public int getItemCount() {
        return mSubBoxes.size();
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
