package com.joelimyx.subbox.history;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joelimyx.subbox.Classes.HistoryItem;
import com.joelimyx.subbox.R;
import com.joelimyx.subbox.transactiondetails.TransactionActivity;
import com.joelimyx.subbox.dbassethelper.SubBoxHelper;
import com.joelimyx.subbox.transactiondetails.TransactionFragment;

import java.util.List;

public class HistoryFragment extends Fragment implements HistoryAdapter.OnHistoryItemSelectedListener{
    private List<HistoryItem> mHistoryItemList = SubBoxHelper.getsInstance(getContext()).getHistoryList();

    public static final String ARG_PARAM1 = "param1";
    public static final String ARG_PARAM2 = "param2";

    private boolean mIsTwoPane;
    private int mTransactionId;

    public HistoryFragment() {
        // Required empty public constructor
    }

    public static HistoryFragment newInstance(boolean param1,int id) {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2,id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mIsTwoPane = getArguments().getBoolean(ARG_PARAM1);
            mTransactionId = getArguments().getInt(ARG_PARAM2);
        }else{
            mIsTwoPane = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Create a transaction detail if a value in mTransactionID(which is from main) was passed along
        if (mTransactionId>0){
            TransactionFragment fragment = TransactionFragment.newInstance(mTransactionId);
            mTransactionId=-1;
            getFragmentManager().beginTransaction().replace(R.id.detail_or_checkout_container,fragment).addToBackStack(null).commit();
        }

        RecyclerView recyclerview = (RecyclerView) view.findViewById(R.id.history_recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        recyclerview.setAdapter(new HistoryAdapter(mHistoryItemList,this,getContext()));
    }

    //--------------------------------------------------------------------------------------------------------------------
    //Interface AREA
    //--------------------------------------------------------------------------------------------------------------------
    @Override
    public void onHistoryItemSelected(int id) {
        //If it is two pane, start fragment instead of a new activity
        if (mIsTwoPane){
            TransactionFragment fragment = TransactionFragment.newInstance(id);
            getFragmentManager().beginTransaction().replace(R.id.detail_or_checkout_container,fragment).addToBackStack(null).commit();
        }else{
            Intent intent = new Intent(getContext(), TransactionActivity.class);
            intent.putExtra("id",id);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.fade_out);
        }
    }
}
