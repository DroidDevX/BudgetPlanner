package com.droiddevsa.budgetplanner.MVP.UI.Charts.AcummulatedBalanceLineChart;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.droiddevsa.budgetplanner.MVP.Data.Models.Budget;
import com.droiddevsa.budgetplanner.MVP.UI.Charts.BaseBudgetLineChart;
import com.droiddevsa.budgetplanner.MVP.UI.Charts.TimeLineXFormatter;
import com.droiddevsa.budgetplanner.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;

public class AccumulatedBalanceChart extends Fragment implements BaseBudgetLineChart {
    private static final String TAG = "AccumulatedChart";
    private static String BUDGETLIST_DATA="budgetlistData";
    private int XAXIS_INDEX_OFFSET =1;


    private ArrayList<Budget> budgetList;
    private LineChart chart;

    public static AccumulatedBalanceChart getInstance(ArrayList<Budget> budgetlist){
        Log.d(TAG,"getInstance()");
        AccumulatedBalanceChart fragment = new AccumulatedBalanceChart();
        Bundle data = new Bundle();
        data.putParcelableArrayList(BUDGETLIST_DATA,budgetlist);
        fragment.setArguments(data);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View rootview = inflater.inflate(R.layout.chart_accumulated_balance,container,false);
        chart= rootview.findViewById(R.id.linechart_accumulatedBalance);

        if(bundleArgumentsAreValid())
                initializeLineChart(budgetList);


        return rootview;
    }

    private boolean bundleArgumentsAreValid(){
        Log.d(TAG, "bundleArgumentsAreValid: ");
        if(getArguments()==null)
            return false;

        if(getArguments().getParcelableArrayList(BUDGETLIST_DATA)==null)
            return false;

        budgetList= getArguments().getParcelableArrayList(BUDGETLIST_DATA);
        return true;
    }


    private void initializeLineChart(ArrayList<Budget> budgetlist){
        Log.d(TAG, "initializeLineChart: ");



        ArrayList<Entry> lineEntry = new ArrayList<>();
        lineEntry.add(new Entry(0,0));//First point at (0,0)

        float accumulatedBalance=0;

        for(int xAxisIndex=0;xAxisIndex< budgetlist.size();xAxisIndex++){

            Budget budget = budgetlist.get(xAxisIndex);
            accumulatedBalance += budget.getBalance();
            lineEntry.add(new Entry(xAxisIndex+ XAXIS_INDEX_OFFSET,accumulatedBalance));
        }

        LineDataSet balanceTrendline = new LineDataSet(lineEntry,"Balance");

        balanceTrendline.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        int dataSetColor= getResources().getColor(R.color.colorSecondaryText_new);
        balanceTrendline.setColor(dataSetColor);
        balanceTrendline.setCircleColor(dataSetColor);
        balanceTrendline.setCircleHoleColor(dataSetColor);
        fillDataSet(balanceTrendline);

        LineData chartData= new LineData();
        chartData.addDataSet(balanceTrendline);



        chart.setExtraTopOffset(0f);
        chart.getAxisLeft().setAxisMaximum(accumulatedBalance*1.2f);
        chart.getAxisLeft().setDrawGridLines(true);


        chart.getAxisRight().setAxisMaximum(accumulatedBalance*1.2f);
        chart.getAxisRight().setDrawGridLines(false);
        chart.getAxisRight().setDrawLabels(false);


        chart.getXAxis().setAxisMaximum(budgetlist.size()*1.5f);
        chart.getXAxis().setGranularity(1f);
        chart.getXAxis().setDrawGridLines(true);
        chart.getXAxis().setDrawAxisLine(true);
        chart.getXAxis().setDrawGridLines(true);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        chart.getXAxis().setLabelCount(4);
        chart.getXAxis().setValueFormatter(new TimeLineXFormatter(budgetlist,XAXIS_INDEX_OFFSET));
        chart.setData(chartData);
        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.setMarker(new AccumulatedBalanceMarkerView(getContext(),R.layout.accumlated_balance_markerview,budgetlist, XAXIS_INDEX_OFFSET));


    }

    private void fillDataSet(LineDataSet dataSet){
        Log.d(TAG, "fillDataSet: ");
        if(getContext()==null)
            return;

        dataSet.setDrawFilled(true);
        if(Utils.getSDKInt()>=18){
            Log.e(TAG, "fill gradient " );
            Drawable drawable = ContextCompat.getDrawable(getContext(),R.drawable.acummulated_balance_fillgradient);
            dataSet.setFillDrawable(drawable);
        }
        else{
            Log.e(TAG,"Fill solid");
            dataSet.setFillColor(getResources().getColor(R.color.colorSecondaryText_new));
        }
    }

}
