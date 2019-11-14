package com.droiddevsa.budgetplanner;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.droiddevsa.budgetplanner.MVP.Data.Models.Budget;
import com.droiddevsa.budgetplanner.MVP.UI.Charts.AcummulatedBalanceLineChart.AccumulatedBalanceChart;

import java.util.ArrayList;

public class AccumulatedBalancePage extends Fragment {
    private static final String BUDGETLIST_DATA="budgetlistData";
    private static final String TAG = "AccumulatedBalancePage";
    public static AccumulatedBalancePage getInstance(ArrayList<Budget> budgetlist){
        Log.d(TAG,"getInstance()");


        AccumulatedBalancePage fragment = new AccumulatedBalancePage();
        Bundle data = new Bundle();
        data.putParcelableArrayList(BUDGETLIST_DATA,budgetlist);
        fragment.setArguments(data);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View rootview = inflater.inflate(R.layout.fragment_accumulated_balance,container,false);

        if(getArguments()!=null)
            if(getArguments().getParcelableArrayList(BUDGETLIST_DATA)!=null)
            {
                ArrayList<Budget> budgetList = getArguments().getParcelableArrayList(BUDGETLIST_DATA);
                setAccumnulatedBalanceLineChart(budgetList);
            }

        return rootview;
    }


private void setAccumnulatedBalanceLineChart(ArrayList<Budget> budgetlist){
    Log.d(TAG, "setAccumnulatedBalanceLineChart: ");
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.accumulatedBalanceContainer, AccumulatedBalanceChart.getInstance(budgetlist));
        ft.commit();
    }

}
