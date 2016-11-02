package com.joelimyx.subbox;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Joe on 11/1/16.
 */

public class SubBoxAdapter extends RecyclerView.Adapter<SubBoxAdapter.SubBoxViewHolder> {
    List<SubBox> mSubBoxes;

    public SubBoxAdapter(List<SubBox> subBoxes) {
        mSubBoxes = subBoxes;
    }

    @Override
    public SubBoxViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subbox_list_item,parent,false);
        return new SubBoxViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SubBoxViewHolder holder, int position) {
        holder.mNameText.setText(mSubBoxes.get(position).getName());
        holder.mPriceText.setText("$"+mSubBoxes.get(position).getPrice());
    }

    @Override
    public int getItemCount() {
        return mSubBoxes.size();
    }

    static class SubBoxViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.name_text) TextView mNameText;
        @BindView(R.id.price_text) TextView mPriceText;

        public SubBoxViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
