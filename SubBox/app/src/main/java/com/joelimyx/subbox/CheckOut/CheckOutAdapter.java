package com.joelimyx.subbox.checkout;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.joelimyx.subbox.Classes.CheckOutItem;
import com.joelimyx.subbox.R;
import com.joelimyx.subbox.dbassethelper.SubBoxHelper;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Joe on 11/2/16.
 */

public class CheckOutAdapter extends RecyclerView.Adapter<CheckOutAdapter.CheckOutViewHolder> {

    private List<CheckOutItem> mCheckOutItemList;
    private OnCheckOutItemModifyListener mCheckOutItemModifyListener;
    private Context mContext;
    private OnCheckOutItemSelectedListener mCheckOutItemSelectedListener;

    //Interface for notifying CheckOutFragment to update total
    interface OnCheckOutItemModifyListener {
        void onCheckOutItemModify();
    }

    //Interface to communicate from checkout fragment to detail fragment
    interface OnCheckOutItemSelectedListener{
        void onCheckOutItemSelected(int id);
    }

    public CheckOutAdapter(List<CheckOutItem> checkOutItemList, OnCheckOutItemModifyListener checkOutItemModifyListener, OnCheckOutItemSelectedListener itemSelectedListener, Context context) {
        mCheckOutItemList = checkOutItemList;
        mCheckOutItemModifyListener = checkOutItemModifyListener;
        mContext = context;
        mCheckOutItemSelectedListener = itemSelectedListener;
    }

    @Override
    public CheckOutViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CheckOutViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_checkout,parent,false));
    }

    @Override
    public void onBindViewHolder(final CheckOutViewHolder holder, int position) {
        Picasso.with(mContext)
                .load(mCheckOutItemList.get(position).getImgUrl())
                .resize(200,200).centerCrop()
                .into(holder.mCheckoutImage);
        holder.mCheckoutTitle.setText(mCheckOutItemList.get(position).getName());

        double total = mCheckOutItemList.get(position).getSubtotalPrice();
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault());
        holder.mCheckoutPrice.setText(currencyFormat.format(total));

        holder.mCountText.setText(String.valueOf(mCheckOutItemList.get(position).getCount()));

        //Add or Minus Checkout item Count
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int holderPos = holder.getAdapterPosition();
                CheckOutItem item = mCheckOutItemList.get(holderPos);

                switch (view.getId()){

                //Remove the item if the count is at 1
                case R.id.red_minus:
                    if (item.getCount()==1){
                        mCheckOutItemList.remove(holderPos);
                        SubBoxHelper.getsInstance(view.getContext())
                                .modifyCheckOutItemCount(0,item.getItemId());
                        notifyItemRemoved(holderPos);
                        mCheckOutItemModifyListener.onCheckOutItemModify();

                    }else{
                        //Else minus one count
                        SubBoxHelper.getsInstance(view.getContext())
                                .modifyCheckOutItemCount(item.getCount()-1,item.getItemId());
                        mCheckOutItemList.get(holderPos).addOrMinusCount('-');
                        notifyItemChanged(holderPos);
                        mCheckOutItemModifyListener.onCheckOutItemModify();
                    }
                    break;

                //Increase count by one
                case R.id.green_plus:
                    SubBoxHelper.getsInstance(view.getContext())
                            .modifyCheckOutItemCount(item.getCount()+1,item.getItemId());
                    mCheckOutItemList.get(holderPos).addOrMinusCount('+');
                    notifyItemChanged(holderPos);
                    mCheckOutItemModifyListener.onCheckOutItemModify();
                    break;
                //Bring up detail page when item is selected in checkout
                case R.id.checkout_item:
                    mCheckOutItemSelectedListener.onCheckOutItemSelected(mCheckOutItemList.get(holderPos).getItemId());
                    break;
                }

            }
        };

        holder.mRedMinus.setOnClickListener(listener);
        holder.mGreenPlus.setOnClickListener(listener);
        holder.mCheckoutItemLayout.setOnClickListener(listener);
    }

    public void clearCheckOutList(){
        mCheckOutItemList = new ArrayList<>();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mCheckOutItemList.size();
    }


    static class CheckOutViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.checkout_item) RelativeLayout mCheckoutItemLayout;
        @BindView(R.id.checkout_image) ImageView mCheckoutImage;
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
