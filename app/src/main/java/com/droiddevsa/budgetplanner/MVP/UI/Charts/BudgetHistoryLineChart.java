package com.droiddevsa.budgetplanner.MVP.UI.Charts;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.droiddevsa.budgetplanner.MVP.Data.Models.Budget;
import com.droiddevsa.budgetplanner.R;
import com.droiddevsa.budgetplanner.Utilities.MaterialColorTemplate;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class BudgetHistoryLineChart extends Fragment implements BaseBudgetHistoryChart{

    private static final String TAG = "BudgetHistoryLineChart";
    private static String INIT_BUNDLE_DATA="budgetItems";
    private String LABEL_INCOME_DATASET ="Income";
    private String LABEL_EXPENSE_DATASET ="Expense";
    private String LABEL_BALANCE_DATASET ="Balance";
    private static String BUNDLE_LAST_SELECTED_INDEX="lastindex";
    final private static int XAxisValuesOffset=1;

    private LineChart lineChart;

    private ArrayList<Budget> budgetlist;
    private  int lastBudgetSelectedIndex=0;


    public static BudgetHistoryLineChart getInstance(ArrayList<Budget> budgetlist){
        Log.d(TAG,"getInstance()");
        BudgetHistoryLineChart fragment = new BudgetHistoryLineChart();
        Bundle data = new Bundle();
        data.putParcelableArrayList(INIT_BUNDLE_DATA,budgetlist);
            fragment.setArguments(data);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(BUNDLE_LAST_SELECTED_INDEX,lastBudgetSelectedIndex);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState!=null)
            lastBudgetSelectedIndex = savedInstanceState.getInt(BUNDLE_LAST_SELECTED_INDEX);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_linechart,container,false);

        lineChart = rootView.findViewById(R.id.linechart);

        if(bundleArgumentsAreValid())
                setupLineChart(budgetlist);


        return rootView;
    }

    private boolean bundleArgumentsAreValid(){
        if(getArguments()==null)
            return false;

       if (getArguments().getParcelableArrayList(INIT_BUNDLE_DATA)==null)
           return false;

       budgetlist = getArguments().getParcelableArrayList(INIT_BUNDLE_DATA);

       return true;
    }

    private float getNewHighlighValue(float highestValue, float income, float expense, float balance){

        if(income >highestValue)
            highestValue = income;

        if(expense> highestValue)
            highestValue = expense;

        if(balance> highestValue)
            highestValue = balance;

        return highestValue;
    }




    private void setupLineChart(ArrayList<Budget> budgetlist ){


        ArrayList<Entry> incomeEntries = new ArrayList<>();
        ArrayList<Entry> expenseEntries = new ArrayList<>();
        ArrayList<Entry> balanceEntries = new ArrayList<>();

        //Model -> Chart data
        float higestValue=-99999999;
        int index=0;

        //Empty
        incomeEntries.add(new Entry(0,0));
        expenseEntries.add(new Entry(0,0));
        balanceEntries.add(new Entry(0,0));


        for(Budget budget:budgetlist){
            float income = (float)budget.getTotalIncome();
            float expense = (float)budget.getTotalExpense();
            float balance =(float)budget.getBalance();

            higestValue = getNewHighlighValue(higestValue,income,expense,balance);

            incomeEntries.add(new Entry(index+XAxisValuesOffset,(float)budget.getTotalIncome()));
            expenseEntries.add(new Entry(index+XAxisValuesOffset,(float)budget.getTotalExpense()));
            balanceEntries.add(new Entry(index+XAxisValuesOffset,(float)budget.getBalance()));

            index++;
        }




        //Chart Data
        LineDataSet incomesDS = new LineDataSet(incomeEntries, LABEL_INCOME_DATASET);
        incomesDS.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        LineDataSet expensesDS = new LineDataSet(expenseEntries, LABEL_EXPENSE_DATASET);
        expensesDS.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        LineDataSet balanceDS = new LineDataSet(balanceEntries, LABEL_BALANCE_DATASET);
        balanceDS.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        incomesDS.setDrawCircleHole(false);
        incomesDS.setDrawValues(false);
        incomesDS.setColor(MaterialColorTemplate.Green);
        incomesDS.setCircleColor(MaterialColorTemplate.Green);

        expensesDS.setDrawCircleHole(false);
        expensesDS.setDrawValues(false);
        expensesDS.setColor(MaterialColorTemplate.DeepOrange);
        expensesDS.setCircleColor(MaterialColorTemplate.DeepOrange);

        int balanceColor =getResources().getColor(R.color.colorSecondaryText_new);
        balanceDS.setDrawValues(false);
        balanceDS.setCircleColor(balanceColor);
        balanceDS.setDrawCircleHole(false);
        balanceDS.setColor(balanceColor);

        LineData data = new LineData();
        data.addDataSet(incomesDS);
        data.addDataSet(expensesDS);
        data.addDataSet(balanceDS);


        lineChart.setData(data);

        formatChart(lineChart,higestValue);
    }

    private  void formatChart(LineChart lineChart,float maxY){

        lineChart.setHighlightPerTapEnabled(false);
        lineChart.setHighlightPerDragEnabled(false);

        lineChart.getLegend().setEnabled(false);
        lineChart.getDescription().setText("");


        lineChart.setDrawBorders(true);


        lineChart.getAxisRight().setAxisMaximum(maxY*2f);
        lineChart.getAxisRight().setAxisMinimum(0);
        lineChart.getAxisRight().setDrawGridLines(false);
        lineChart.getAxisRight().setDrawAxisLine(false);


        lineChart.getAxisLeft().setAxisMaximum(maxY*2f);
        lineChart.getAxisLeft().setAxisMinimum(0);
        lineChart.getAxisLeft().setDrawLabels(false);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getAxisLeft().setDrawAxisLine(false);


        lineChart.getXAxis().setAxisMaximum(budgetlist.size()*1.2f);
        lineChart.getXAxis().setValueFormatter(new TimeLineXFormatter(budgetlist,XAxisValuesOffset));
        lineChart.getXAxis().setDrawLabels(false);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getXAxis().setDrawAxisLine(true);
        lineChart.getXAxis().setDrawLimitLinesBehindData(false);

    }


    @Override
    public void onXAxisSelectedChanged(int xPosition) {
        Log.d(TAG, "onXAxisSelectedChanged: ");

        if(budgetlist==null|| getContext()==null)
            return;

        lastBudgetSelectedIndex=xPosition;
        lineChart.setRendererRightYAxis(new CustomYRenderer(lineChart.getViewPortHandler(),lineChart.getAxisRight(),lineChart.getTransformer(YAxis.AxisDependency.RIGHT)));

        lineChart.getXAxis().removeAllLimitLines();
        lineChart.getAxisRight().removeAllLimitLines();

        LimitLine indicator = new LimitLine(xPosition+XAxisValuesOffset,"");
        indicator.setLineWidth(1f);
        indicator.setLineColor(MaterialColorTemplate.Orange);

        lineChart.getXAxis().addLimitLine(indicator);
        lineChart.invalidate();
        lineChart.moveViewToX(xPosition+XAxisValuesOffset);
    }

    private LimitLine createYLimitLine(String label, int xAxisPos){
        Log.d(TAG, "createYLimitLine: ");
        if(getContext()==null)
            return null;

        Budget budget = budgetlist.get(xAxisPos);
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
        Log.d(TAG, "setIncomeVisibility: ");
        lineChart.getData().getDataSetByIndex(0).setVisible(visible);
        lineChart.invalidate();
        onXAxisSelectedChanged(lastBudgetSelectedIndex);

    }

    @Override
    public void setExpenseVisibility(Boolean visible) {
        Log.d(TAG, "setExpenseVisibility: ");
        lineChart.getData().getDataSetByIndex(1).setVisible(visible);
        lineChart.invalidate();
        onXAxisSelectedChanged(lastBudgetSelectedIndex);
    }

    @Override
    public void setBalanceVisibility(Boolean visible) {
        Log.d(TAG, "setBalanceVisibility: ");
        lineChart.getData().getDataSetByIndex(2).setVisible(visible);
        lineChart.invalidate();
        onXAxisSelectedChanged(lastBudgetSelectedIndex);
    }

    private void createLimitLine(){

    }



}
