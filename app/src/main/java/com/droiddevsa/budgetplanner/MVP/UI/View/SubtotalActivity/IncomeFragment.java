package com.droiddevsa.budgetplanner.MVP.UI.View.SubtotalActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import com.droiddevsa.budgetplanner.MVP.Data.Models.CategorySubtotal;
import com.droiddevsa.budgetplanner.MVP.UI.Charts.BaseBudgetOverviewBarChart;
import com.droiddevsa.budgetplanner.MVP.UI.Charts.BaseBudgetOverviewPieChart;
import com.droiddevsa.budgetplanner.MVP.UI.Charts.BudgetOverviewBarChart;
import com.droiddevsa.budgetplanner.MVP.UI.Charts.BudgetOverviewPiechart;
import com.droiddevsa.budgetplanner.R;
import java.util.ArrayList;

public class IncomeFragment extends Fragment {
    private static String BUNDLE_INCOME_DATA="budgetIncomeData";
    private static final String TAG = "IncomeFragment";

    BaseBudgetOverviewBarChart incomeBarChart;
    BaseBudgetOverviewPieChart incomePieChart;

    public static IncomeFragment getInstance(ArrayList<CategorySubtotal> subtotals){
        Log.d(TAG,"IncomeFragment, getInstance()");

        IncomeFragment fragment = new IncomeFragment();
        Bundle data = new Bundle();
        data.putParcelableArrayList(BUNDLE_INCOME_DATA,subtotals);
        fragment.setArguments(data);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG,"onCreateView()");
        View rootView = inflater.inflate(R.layout.fragment_income,container,false);

        Bundle initData = getArguments();
        if(initData!=null) {
            ArrayList<CategorySubtotal> subtotals =initData.getParcelableArrayList(BUNDLE_INCOME_DATA);
            incomeBarChart= BudgetOverviewBarChart.getInstance(subtotals);
            incomePieChart= BudgetOverviewPiechart.getInstance(subtotals);
            bindFragmentsToContainers(incomeBarChart,incomePieChart);
        }
        return rootView;
    }

    public void bindFragmentsToContainers(BaseBudgetOverviewBarChart incomeBarChart, BaseBudgetOverviewPieChart incomePieChart){
        Log.d(TAG,"Income Fragment, bindFragmentsToContainers()");
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.add(R.id.incomeBarchart_FragmentContainer,(Fragment) incomeBarChart);
        ft.add(R.id.incomePieChart_FragmentContainer,(Fragment) incomePieChart);
        ft.commit();
    }


}
