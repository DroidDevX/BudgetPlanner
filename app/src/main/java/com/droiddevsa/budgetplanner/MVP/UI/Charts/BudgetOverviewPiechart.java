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
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

import static com.droiddevsa.budgetplanner.Utilities.MaterialColorTemplate.Green;

public class BudgetOverviewPiechart extends Fragment implements BaseBudgetOverviewPieChart {
    private static final String TAG = BudgetOverviewPiechart.class.getSimpleName();

    private static String BUNDLE_DATA_BUDGETITEMS ="budgetdata";

    PieChart pieChart;
    //FLAGS

    public static BudgetOverviewPiechart getInstance(ArrayList<CategorySubtotal> budgetlist){
        Log.d(TAG,"getInstance()");
        BudgetOverviewPiechart fragment = new BudgetOverviewPiechart();
        Bundle initData = new Bundle();
        initData.putParcelableArrayList(BUNDLE_DATA_BUDGETITEMS,budgetlist);
        fragment.setArguments(initData);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG,"onCreateView()");
        View rootView = inflater.inflate(R.layout.fragment_piechart,container,false);
        pieChart = rootView.findViewById(R.id.pieChart);

        if(getArguments()!=null) {
            ArrayList<CategorySubtotal> subtotals = getArguments().getParcelableArrayList(BUNDLE_DATA_BUDGETITEMS);
            if(subtotals!=null)
                setupPieChart(subtotals);


        }

        return rootView;
    }

    public void setupPieChart(ArrayList<CategorySubtotal> subtotals){
        Log.d(TAG,"setupPieChart()");
       /* Log.e(TAG,"setupPieChart():Param:subtoals");
        for(CategorySubtotal subtotal:subtotals)
            Log.e(TAG,subtotal.toString());*/

        //Maximum of 7 categories
        int[] Colors = {MaterialColorTemplate.Amber,
                MaterialColorTemplate.Lime, Green, MaterialColorTemplate.Cyan,
                MaterialColorTemplate.Blue, MaterialColorTemplate.DeepPurple, MaterialColorTemplate.Pink};

        float total =0;
        for(int i=0;i< subtotals.size();i++)
            total+=subtotals.get(i).getSubtotal();


        int numberColumns = subtotals.size();
        ArrayList<PieEntry> entries=new ArrayList<>();
        PieDataSet dataSet;
        PieData data = new PieData();

        for(int col =0;col< numberColumns;col++){
            float amount =(float)subtotals.get(col).getSubtotal();
            float percentage =(amount/total)*100;

            PieEntry entry =new PieEntry(percentage,subtotals.get(col).getCategoryName());
            entries.add(entry);
        }

        dataSet = new PieDataSet(entries,"");
        dataSet.setValueTextColor(getResources().getColor(R.color.colorPrimary_new));
        dataSet.setColors(Colors);
        data.addDataSet(dataSet);


        pieChart.getLegend().setXOffset(20f);
        pieChart.getLegend().setYEntrySpace(5f);
        pieChart.getLegend().setWordWrapEnabled(true);
        pieChart.setCenterText("%");
        pieChart.setDrawEntryLabels(false);
        pieChart.getDescription().setText("");
        pieChart.setData(data);
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
