package com.droiddevsa.budgetplanner.MVP.UI.View.HomeActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.droiddevsa.budgetplanner.MVP.Data.Models.Budget;
import com.droiddevsa.budgetplanner.MVP.Data.Repository.SharedPreferenceManager;
import com.droiddevsa.budgetplanner.MVP.UI.Charts.BaseBudgetHistoryChart;
import com.droiddevsa.budgetplanner.MVP.UI.Charts.BudgetHistogram.HistogramOptionsDialog;
import com.droiddevsa.budgetplanner.MVP.UI.Charts.BudgetHistogram.IHistrogramDialogClickListener;
import com.droiddevsa.budgetplanner.MVP.UI.Charts.BudgetHistoryLineChart;
import com.droiddevsa.budgetplanner.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


public class BudgetHistogramFragmentPage extends Fragment {
    private static final String TAG = "BudgetHistogramFragment";
    private static String BUDGETLIST_DATA = "budgetlist";
    private static String LAST_BUDGET_SELECTED_INDEX;

    private SharedPreferenceManager.ConfigHistogram configHistogram;
    private ArrayList<Budget> budgetList;



    private BaseBudgetHistoryChart historyChart;
    private int lastBudgetSelectedIndex=0;
    private TextView dateSelectedTV;
    private TextView incomeTV;
    private TextView expenseTV;
    private TextView balanceTV;
    private SeekBar dateseekBar;
    private SeekBar.OnSeekBarChangeListener seekBarListener;

    public static BudgetHistogramFragmentPage getInstance(ArrayList<Budget> budgetlist) {
        Log.d(TAG, "getInstance()");
        BudgetHistogramFragmentPage fragment = new BudgetHistogramFragmentPage();
        Bundle data = new Bundle();
        data.putParcelableArrayList(BUDGETLIST_DATA, budgetlist);
        fragment.setArguments(data);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(LAST_BUDGET_SELECTED_INDEX,lastBudgetSelectedIndex);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState!=null)
            lastBudgetSelectedIndex = savedInstanceState.getInt(LAST_BUDGET_SELECTED_INDEX);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View rootview = inflater.inflate(R.layout.fragment_home_histogram_page, container, false);


        if (bundleArgumentsAreValid()) {
            findViewsByID(rootview);
            setHistoryChart(budgetList);
        }
        return rootview;
    }

    private void findViewsByID(View rootview){
        dateSelectedTV = rootview.findViewById(R.id.dateSelectedTV);

        dateseekBar= rootview.findViewById(R.id.histogramSeekBar);
        seekBarListener=createSeekBarChangeListener();
        dateseekBar.setOnSeekBarChangeListener(seekBarListener);

        View histogramOptionsButton = rootview.findViewById(R.id.histogramOptionsButton);
        histogramOptionsButton.setOnClickListener(createOptionsBtnClickListener());

        incomeTV = rootview.findViewById(R.id.histogramIncomeTV);
        expenseTV = rootview.findViewById(R.id.histogramExpenseTV);
        balanceTV = rootview.findViewById(R.id.histogramBalanceTV);
    }

    private boolean bundleArgumentsAreValid(){
        if(getArguments() == null)
            return false;

        if(getArguments().getParcelableArrayList(BUDGETLIST_DATA) == null)
            return false;

        budgetList =getArguments().getParcelableArrayList(BUDGETLIST_DATA);
        return true;

    }


    @Override
    public void onStart() {
        super.onStart();
        if(historyChart !=null) {
            toogleHistogramVisibilityOptions(configHistogram.getVisibleSeries());
            seekBarListener.onProgressChanged(dateseekBar,0,true);
        }
    }

    private String formatDate(String format, Date date){
        SimpleDateFormat formatter = new SimpleDateFormat(format,Locale.ENGLISH);
        return formatter.format(date);
    }

    private void setHistoryChart(final ArrayList<Budget> budgetList) {
        Log.d(TAG, "setHistoryChart: ");

        //Shared Preferences
        if(getContext()!=null)
            configHistogram = new SharedPreferenceManager(getContext()).getConfigHistogram();

        //Insert Fragment Chart
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        historyChart = BudgetHistoryLineChart.getInstance(budgetList);
        ft.replace(R.id.histogramContainer, (Fragment)historyChart);
        ft.commit();

        //Set Date
        String formattedDate = formatDate("dd/MM/yyyy",budgetList.get(lastBudgetSelectedIndex).getCreatedDate());
        dateSelectedTV.setText(formattedDate);
    }

    private  View.OnClickListener createOptionsBtnClickListener(){
        Log.d(TAG, "createOptionsBtnClickListener: ");
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: ");
                HistogramOptionsDialog dialog = new HistogramOptionsDialog(getActivity());
                dialog.setClickListener(new IHistrogramDialogClickListener() {
                    @Override
                    public void okClicked(HashMap<String, Boolean> visibilityOptions) {
                        Log.d(TAG, "okClicked: ");
                        configHistogram.setVisibleSeries(visibilityOptions);
                        toogleHistogramVisibilityOptions(visibilityOptions);
                    }
                });
                dialog.show(getChildFragmentManager(),"");
            }
        };
    }

    private SeekBar.OnSeekBarChangeListener createSeekBarChangeListener(){
        return new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d(TAG, "onProgressChanged: ");

                if(budgetList==null||!fromUser || budgetList.isEmpty())
                    return;


                lastBudgetSelectedIndex= calculatedSelectedBudgetIndex(progress,budgetList);

                Budget selectedBudget = budgetList.get(lastBudgetSelectedIndex);

                dateSelectedTV.setText(selectedBudget.getFormattedDateStr("dd/MM/yyyy"));
                incomeTV.setText(String.format(Locale.ENGLISH,"%.2f",selectedBudget.getTotalIncome()));
                expenseTV.setText(String.format(Locale.ENGLISH,"%.2f",selectedBudget.getTotalExpense()));
                balanceTV.setText(String.format(Locale.ENGLISH,"%.2f",selectedBudget.getBalance()));
                historyChart.onXAxisSelectedChanged(lastBudgetSelectedIndex);

            }


            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };
    }

    private int calculatedSelectedBudgetIndex(float seekBarProgress,@NonNull ArrayList<Budget> budgetList){
        Log.d(TAG, "calculatedSelectedBudgetIndex: ");


        int lastBudgetIndex = budgetList.size()-1;
        return Math.round(lastBudgetIndex* (float)(seekBarProgress/100.00));
    }

    private void toogleHistogramVisibilityOptions(HashMap<String,Boolean> selectedSeries){
        Log.d(TAG, "toogleHistogramVisibilityOptions: ");
        if (historyChart ==null||getContext()==null)
            return;

        for(String key:selectedSeries.keySet()){

            switch (key){

                case HistogramOptionsDialog.INCOME_SERIES:
                              historyChart.setIncomeVisibility(selectedSeries.get(key));
                              break;
                case HistogramOptionsDialog.EXPENSE_SERIES:
                              historyChart.setExpenseVisibility(selectedSeries.get(key));
                              break;
                case HistogramOptionsDialog.BALANCE_SERIES:
                              historyChart.setBalanceVisibility(selectedSeries.get(key));
                              break;
                default:break;

            }
        }
    }


}
