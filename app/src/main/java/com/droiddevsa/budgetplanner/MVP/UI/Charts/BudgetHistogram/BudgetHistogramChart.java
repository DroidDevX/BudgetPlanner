package com.droiddevsa.budgetplanner.MVP.UI.Charts.BudgetHistogram;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.droiddevsa.budgetplanner.MVP.Data.Models.Budget;
import com.droiddevsa.budgetplanner.MVP.UI.Charts.BaseBudgetHistoryChart;
import com.droiddevsa.budgetplanner.MVP.UI.Charts.CustomYRenderer;
import com.droiddevsa.budgetplanner.R;
import com.droiddevsa.budgetplanner.Utilities.MaterialColorTemplate;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;

public class BudgetHistogramChart extends Fragment implements BaseBudgetHistoryChart
{
    private static final String BUDGETLIST_DATA ="budgetlistData";
    private static final String TAG = "BudgetHistogramChart";
    private BarChart histogramView;
    private ArrayList<Budget> budgetList =null;
    private float BarGroupIntervalRange = 1f;
    private float largestYValue;
    private int lastSelectedPosition=0;


    public static BudgetHistogramChart getInstance(ArrayList<Budget> budgetlist){
        Log.d(TAG,"getInstance()");
        BudgetHistogramChart fragment = new BudgetHistogramChart();
        Bundle data = new Bundle();
        data.putParcelableArrayList(BUDGETLIST_DATA,budgetlist);
        fragment.setArguments(data);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View rootview = inflater.inflate(R.layout.chart_budget_histogram,container,false);
        histogramView = rootview.findViewById(R.id.barchart_budgetHistogram);


        if(getArguments()!=null)
            if(getArguments().getParcelableArrayList(BUDGETLIST_DATA)!=null)
            {
                budgetList = getArguments().getParcelableArrayList(BUDGETLIST_DATA);
                setHistogramData(budgetList);
                formatHistogram();
            }

        return rootview;
    }

    public float getLargestValue(float currentLargestValue,float income, float expense, float balance){

        if(income>currentLargestValue)
            currentLargestValue = income;

        if(expense> currentLargestValue)
            currentLargestValue = expense;

        if(balance > currentLargestValue)
            currentLargestValue = balance;

        return currentLargestValue;
    }

    private void setHistogramData(ArrayList<Budget> budgetlist){
        Log.e(TAG, "setHistogramData: ");
        if(budgetlist==null) {
            Log.e(TAG, "Budget list is null");
            return;
        }
        else
            for(int i=0;i< budgetlist.size();i++)
                Log.e(TAG,budgetlist.get(i).toString());

            //Init mp chart

        ArrayList<BarEntry> incomeEntries = new ArrayList<>();
        ArrayList<BarEntry> expenseEntries = new ArrayList<>();
        ArrayList<BarEntry> balanceEntries = new ArrayList<>();

        for(int pos=0;pos< budgetlist.size();pos++){

            largestYValue= getLargestValue(largestYValue,(float)budgetlist.get(pos).getTotalIncome(),
                    (float)budgetlist.get(pos).getTotalExpense(),(float)budgetlist.get(pos).getBalance());

            incomeEntries.add(new BarEntry(pos,(float)budgetlist.get(pos).getTotalIncome()));
            expenseEntries.add(new BarEntry(pos,(float)budgetlist.get(pos).getTotalExpense()));
            balanceEntries.add(new BarEntry(pos,(float)budgetlist.get(pos).getBalance()));

        }
        BarDataSet incomeDataSet = new BarDataSet(incomeEntries,"Income");
            incomeDataSet.setColor(getResources().getColor(R.color.colorGreen));
            incomeDataSet.setDrawValues(false);

        BarDataSet expenseDataSet = new BarDataSet(expenseEntries,"Expense");
            expenseDataSet.setColor(getResources().getColor(R.color.colorDeepOrange));
            expenseDataSet.setDrawValues(false);

        BarDataSet balanceDataSet = new BarDataSet(balanceEntries,"Balance");
            balanceDataSet.setColor(getResources().getColor(R.color.colorSecondaryText_new));
            balanceDataSet.setDrawValues(false);


        BarData chartData = new BarData();

        chartData.addDataSet(incomeDataSet);
        chartData.addDataSet(expenseDataSet);
        chartData.addDataSet(balanceDataSet);

        histogramView.setData(chartData);


    }

    private void formatHistogram(){
        Log.d(TAG, "formatHistogram: ");
        if(budgetList==null)
            return;


        ViewPortHandler viewPortHandler = histogramView.getViewPortHandler();
        histogramView.setViewPortOffsets(0f,viewPortHandler.contentTop(),viewPortHandler.offsetRight()-10f,viewPortHandler.offsetBottom());
        BarData chartData = histogramView.getData();

        float GraphGranularity= 1f;
        float BarGroupSpaceOffset=(float)0.25*BarGroupIntervalRange;
        float BarGroupSpace = BarGroupIntervalRange-BarGroupSpaceOffset;
        float BarSlice = BarGroupSpace/chartData.getDataSetCount();
        float BarWidth = (float)0.8*BarSlice;
        float BarSpace = BarSlice-BarWidth;

        chartData.setBarWidth(BarWidth);
        histogramView.groupBars(0f,BarGroupSpaceOffset,BarSpace);

        histogramView.getXAxis().setGranularity(GraphGranularity);


        histogramView.moveViewToX(budgetList.size());
        histogramView.setFitBars(true);

        histogramView.getLegend().setEnabled(false);
        histogramView.getDescription().setEnabled(false);


        histogramView.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        histogramView.getXAxis().setDrawAxisLine(false);
        histogramView.getXAxis().setAxisMaximum((float)1.2*BarGroupIntervalRange*budgetList.size());

        histogramView.setDrawBorders(true);
        histogramView.getAxisRight().setDrawAxisLine(false);
        histogramView.getAxisLeft().setDrawAxisLine(false);
        histogramView.getAxisRight().setDrawGridLines(false);
        histogramView.getAxisLeft().setDrawLabels(false);
        histogramView.getAxisLeft().setDrawGridLines(false);
        histogramView.getAxisRight().setAxisMaximum((float)1.5*largestYValue);
        histogramView.getAxisLeft().setAxisMaximum((float)1.5*largestYValue);

        histogramView.enableScroll();
    }


    public void onXAxisSelectedChanged(int xPosition){
        Log.e(TAG, "onXAxisSelectedChanged: X value -"+ xPosition);
        if(budgetList==null|| getContext()==null)
            return;

        lastSelectedPosition=xPosition;
        histogramView.setRendererRightYAxis(new CustomYRenderer(histogramView.getViewPortHandler(),histogramView.getAxisRight(),histogramView.getTransformer(YAxis.AxisDependency.RIGHT)));
        histogramView.getAxisRight().removeAllLimitLines();
        histogramView.getXAxis().removeAllLimitLines();


        for(int barIndex=0;barIndex<histogramView.getData().getDataSetCount();barIndex++){

            LimitLine line =null;
            boolean lineIsVisible=histogramView.getData().getDataSetByIndex(barIndex).isVisible();

            if(lineIsVisible)
             switch(barIndex){

                case 0:{

                    line = createYLimitLine("Income",xPosition);
                    break;
                }

                case 1:{
                    line = createYLimitLine("Expense",xPosition);
                    break;
                }

                case 2:{
                    line = createYLimitLine("Balance",xPosition);
                    break;
                }
                default: break;
            }
            if(line!=null)
                histogramView.getAxisRight().addLimitLine(line);
        }

        LimitLine indicator =new LimitLine(xPosition,"");
        indicator.setLineColor(MaterialColorTemplate.Orange);
        indicator.setLineWidth(2f);
        histogramView.getXAxis().addLimitLine(indicator);
        histogramView.invalidate();
        histogramView.moveViewToX(xPosition);
    }



    private LimitLine createYLimitLine(String label,int xAxisPos){
        Log.d(TAG, "createYLimitLine: ");
        if(getContext()==null)
            return null;

        Budget budget = budgetList.get(xAxisPos);
        float defaultTextSize=14f;
        LimitLine line=null;

        switch (label){
            case "Income": {
                line = new LimitLine((float) budget.getTotalIncome(), String.valueOf(budget.getTotalIncome()));
                line.setLineColor(getContext().getResources().getColor(R.color.colorGreen));
                break;
            }

            case "Expense": {
                line = new LimitLine((float) budget.getTotalExpense(), String.valueOf(budget.getTotalExpense()));
                line.setLineColor(getContext().getResources().getColor(R.color.colorDeepOrange));
                break;
            }

            case "Balance": {
                line = new LimitLine((float) budget.getBalance(), String.valueOf(budget.getBalance()));
                line.setLineColor(getContext().getResources().getColor(R.color.colorSecondaryText_new));
                break;
            }

                default:break;
        }

        if(line!=null) {
            line.setTextSize(defaultTextSize);

        }

        return line;
    }


    @Override
    public void setIncomeVisibility(Boolean visible) {
        histogramView.getData().getDataSetByIndex(0).setVisible(visible);
        histogramView.invalidate();
        onXAxisSelectedChanged(lastSelectedPosition);
    }

    @Override
    public void setExpenseVisibility(Boolean visible)
    {
        histogramView.getData().getDataSetByIndex(1).setVisible(visible);
        histogramView.invalidate();
        onXAxisSelectedChanged(lastSelectedPosition);
    }

    @Override
    public void setBalanceVisibility(Boolean visible) {
        histogramView.getData().getDataSetByIndex(2).setVisible(visible);
        histogramView.invalidate();
        onXAxisSelectedChanged(lastSelectedPosition);
    }
}
