package com.joelimyx.subbox.mainlist;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.joelimyx.subbox.R;
import com.joelimyx.subbox.Classes.SubBox;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Joe on 11/1/16.
 */

public class SubBoxAdapter extends RecyclerView.Adapter<SubBoxAdapter.SubBoxViewHolder> {
    public static final String SELECTED_ID = "id";
    List<SubBox> mSubBoxes;
    private OnItemSelectedListener mListener;
    Context mContext;

    public SubBoxAdapter(List<SubBox> subBoxes, OnItemSelectedListener listener, Context context) {
        mSubBoxes = subBoxes;
        mListener = listener;
        mContext =context;
    }

    @Override
    public SubBoxViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_subbox,parent,false);
        return new SubBoxViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SubBoxViewHolder holder, final int position) {

        Picasso.with(mContext)
                .load(mSubBoxes.get(position).getImgUrl())
                .into(holder.mIconImage);

        holder.mNameText.setText(mSubBoxes.get(position).getName());

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault());
        double priceValue = mSubBoxes.get(position).getPrice();
        holder.mPriceText.setText(currencyFormat.format(priceValue));

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemSelected(mSubBoxes.get(position).getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSubBoxes.size();
    }

    static class SubBoxViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.icon_image) ImageView mIconImage;
        @BindView(R.id.name_text) TextView mNameText;
        @BindView(R.id.price_text) TextView mPriceText;
        @BindView(R.id.cardview) CardView mCardView;

        public SubBoxViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public void replaceData(List<SubBox> newData){
        mSubBoxes = newData;
        notifyDataSetChanged();
    }

    public interface OnItemSelectedListener{
        void onItemSelected(int id);
    }
}
