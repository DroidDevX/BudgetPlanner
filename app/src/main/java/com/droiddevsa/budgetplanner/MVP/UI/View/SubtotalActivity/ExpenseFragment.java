package com.droiddevsa.budgetplanner.MVP.UI.View.SubtotalActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

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

public class ExpenseFragment extends Fragment {
    private static String BUNDLE_EXPENSE_DATA="bundleExpenseData";
    private static final String TAG = "ExpenseFragment";
    FrameLayout barChartContainer;
    FrameLayout pieChartContainer;

    BaseBudgetOverviewBarChart expenseBarChart;
    BaseBudgetOverviewPieChart expensePieChart;

    public static ExpenseFragment getInstance(ArrayList<CategorySubtotal> subtotals){
        Log.d(TAG,"ExpenseFragment, getInstance()");


        ExpenseFragment fragment = new ExpenseFragment();
        Bundle data = new Bundle();
        data.putParcelableArrayList(BUNDLE_EXPENSE_DATA,subtotals);
        fragment.setArguments(data);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG,"onCreateView()");

        View rootView = inflater.inflate(R.layout.fragment_expense,container,false);
        barChartContainer = rootView.findViewById(R.id.expenseBarchart_fragmentContainer);
        pieChartContainer = rootView.findViewById(R.id.expensePieChart_fragmentContainer);

        Bundle data = getArguments();
        if(data !=null)
        {  ArrayList<CategorySubtotal> expenseSubtotals=data.getParcelableArrayList(BUNDLE_EXPENSE_DATA);
            expenseBarChart = BudgetOverviewBarChart.getInstance(expenseSubtotals);
            expensePieChart = BudgetOverviewPiechart.getInstance(expenseSubtotals);
            bindFragmentsToContainers(expenseBarChart,expensePieChart);
        }
        return rootView;
    }

    public void bindFragmentsToContainers(BaseBudgetOverviewBarChart expenseBarChart, BaseBudgetOverviewPieChart expensePieChart){
        Log.d(TAG, "bindFragmentsToContainers: ");
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.expenseBarchart_fragmentContainer,(Fragment)expenseBarChart);
        ft.replace(R.id.expensePieChart_fragmentContainer,(Fragment)expensePieChart);
        ft.commit();

    }


    @Override
    public void onAttachFragment(@NonNull Fragment childFragment) {
        super.onAttachFragment(childFragment);

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG,"onActivityCreated()");
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d(TAG,"onAttach()");

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"onResume()");
    }
}
