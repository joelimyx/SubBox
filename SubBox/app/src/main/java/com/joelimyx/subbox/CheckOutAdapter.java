package com.joelimyx.subbox;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Joe on 11/2/16.
 */

public class CheckOutAdapter extends RecyclerView.Adapter<CheckOutAdapter.CheckOutViewHolder> {

    @Override
    public CheckOutViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(CheckOutViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    static class CheckOutViewHolder extends RecyclerView.ViewHolder{
        public CheckOutViewHolder(View itemView) {
            super(itemView);
        }
    }
}
