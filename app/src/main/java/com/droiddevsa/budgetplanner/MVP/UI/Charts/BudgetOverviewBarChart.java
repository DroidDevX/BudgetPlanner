package com.droiddevsa.budgetplanner.MVP.UI.Charts;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.droiddevsa.budgetplanner.MVP.Data.Models.CategorySubtotal;
import com.droiddevsa.budgetplanner.R;
import com.droiddevsa.budgetplanner.Utilities.MaterialColorTemplate;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

import static com.droiddevsa.budgetplanner.Utilities.MaterialColorTemplate.Green;

public class BudgetOverviewBarChart extends Fragment implements BaseBudgetOverviewBarChart {

    private static final String TAG = BudgetOverviewBarChart.class.getSimpleName();
    
    private static String BUNDLE_DATA_INCOME_SUBTOTALS ="incomeSubtotals";
    BarChart chart;
    //Flags

    public static BudgetOverviewBarChart getInstance(ArrayList<CategorySubtotal> incomeSubtotals){
        Log.d(TAG,"getInstance()");


        BudgetOverviewBarChart fragment = new BudgetOverviewBarChart();
        Bundle initdata = new Bundle();
        initdata.putParcelableArrayList(BUNDLE_DATA_INCOME_SUBTOTALS,incomeSubtotals);
        fragment.setArguments(initdata);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG,"onCreateView()");
        View rootView = inflater.inflate(R.layout.fragment_barchart,container,false);
        chart= rootView.findViewById(R.id.barchart);

        if(getArguments()!=null) {
            ArrayList<CategorySubtotal> subtotals = getArguments().getParcelableArrayList(BUNDLE_DATA_INCOME_SUBTOTALS);
            if(subtotals!=null) {
                setupMPBarChart(subtotals);
            }

        }
        return rootView;
    }



    public void setupMPBarChart(ArrayList<CategorySubtotal> subtotals){
        //Maximum of 7 categories
        int[] Colors = {MaterialColorTemplate.Amber,
                MaterialColorTemplate.Lime, Green, MaterialColorTemplate.Cyan,
                MaterialColorTemplate.Blue, MaterialColorTemplate.DeepPurple, MaterialColorTemplate.Pink};


        Log.d(TAG,"setupMPBarChart()");
        /*for(int i=0;i< subtotals.size();i++)
            Log.e(TAG,subtotals.get(i).toString());*/


        int numberColumns = subtotals.size();

        ArrayList<BarEntry> entries;
        BarDataSet dataSet;
        BarData data = new BarData();
        for(int col =0;col< numberColumns;col++){
            entries = new ArrayList<>();
            float yValue =(float)subtotals.get(col).getSubtotal();
            BarEntry entry = new BarEntry((float) col,yValue);
            entries.add(entry);
            dataSet = new BarDataSet(entries,subtotals.get(col).getCategoryName());
            dataSet.setColor(Colors[col]);
            data.addDataSet(dataSet);
        }

        data.setBarWidth(0.25f);

        (chart.getDescription()).setText("");
        chart.getLegend().setXOffset(20f);
        chart.getLegend().setYEntrySpace(5f);
        chart.getLegend().setWordWrapEnabled(true);
        chart.getXAxis().setEnabled(false);
        chart.getAxisRight().setEnabled(false);
        chart.getAxisLeft().setAxisMinimum(0);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.setData(data);



    }

    /**********************/
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
    public void onAttachFragment(@NonNull Fragment childFragment) {
        super.onAttachFragment(childFragment);
        Log.d(TAG,"onAttachFragment");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"onResume()");

    }
}
