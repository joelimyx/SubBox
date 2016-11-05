package com.joelimyx.subbox.CheckOut;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.joelimyx.subbox.Classes.CheckOutItem;
import com.joelimyx.subbox.R;
import com.joelimyx.subbox.dbassethelper.SubBoxHelper;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Joe on 11/2/16.
 */

public class CheckOutAdapter extends RecyclerView.Adapter<CheckOutAdapter.CheckOutViewHolder> {

    List<CheckOutItem> mCheckOutItems;
    private OnCheckOutItemModifyListener mListener;

    //Interface for notifying CheckOutFragment total update
    interface OnCheckOutItemModifyListener {
        void onCheckOutItemModify();
    }

    public CheckOutAdapter(List<CheckOutItem> checkOutItems, OnCheckOutItemModifyListener listener) {
        mCheckOutItems = checkOutItems;
        mListener = listener;
    }

    @Override
    public CheckOutViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CheckOutViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_checkout,parent,false));
    }

    @Override
    public void onBindViewHolder(final CheckOutViewHolder holder, int position) {
        holder.mCheckoutTitle.setText(mCheckOutItems.get(position).getName());
        double total = mCheckOutItems.get(position).getSubtotalPrice();

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault());
        holder.mCheckoutPrice.setText(currencyFormat.format(total));

        holder.mCountText.setText(String.valueOf(mCheckOutItems.get(position).getCount()));

        //Add or Minus Checkout item Count
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int holderPos = holder.getAdapterPosition();
                CheckOutItem item = mCheckOutItems.get(holderPos);

                switch (view.getId()){
                    case R.id.red_minus:
                        //Remove the item if the count is at 1
                        if (item.getCount()==1){
                            mCheckOutItems.remove(holderPos);
                            SubBoxHelper.getsInstance(view.getContext())
                                    .modifyCheckOutItemCount(0,item.getItemId());
                            notifyItemRemoved(holderPos);
                        }else{
                            SubBoxHelper.getsInstance(view.getContext())
                                    .modifyCheckOutItemCount(item.getCount()-1,item.getItemId());
                            mCheckOutItems.get(holderPos).addOrMinusCount('-');
                            notifyItemChanged(holderPos);
                        }
                        break;
                    case R.id.green_plus:
                        SubBoxHelper.getsInstance(view.getContext())
                                .modifyCheckOutItemCount(item.getCount()+1,item.getItemId());
                        mCheckOutItems.get(holderPos).addOrMinusCount('+');
                        notifyItemChanged(holderPos);
                        break;
                }
                mListener.onCheckOutItemModify();
            }
        };
        holder.mRedMinus.setOnClickListener(listener);
        holder.mGreenPlus.setOnClickListener(listener);

        //Image or title onclick

    }

    @Override
    public int getItemCount() {
        return mCheckOutItems.size();
    }


    static class CheckOutViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.checkout_title_text) TextView mCheckoutTitle;
        @BindView(R.id.checkout_price_text) TextView mCheckoutPrice;
        @BindView(R.id.checkout_count_text) TextView mCountText;
        @BindView(R.id.red_minus) ImageView mRedMinus;
        @BindView(R.id.green_plus) ImageView mGreenPlus;

        public CheckOutViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
