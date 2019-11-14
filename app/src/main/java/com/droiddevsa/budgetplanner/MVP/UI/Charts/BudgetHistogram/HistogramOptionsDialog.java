package com.droiddevsa.budgetplanner.MVP.UI.Charts.BudgetHistogram;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.droiddevsa.budgetplanner.R;

import java.util.HashMap;

public class HistogramOptionsDialog extends DialogFragment implements View.OnClickListener{
    private static final String TAG = "HistogramOptionsDialog";
    final public static String INCOME_SERIES="Income";
    final public static String EXPENSE_SERIES="Expense";
    final public static String BALANCE_SERIES="Balance";

    private Context context;

    private IHistrogramDialogClickListener listner;

    private CheckBox cb_income;
    private CheckBox cb_expense;
    private CheckBox cb_balance;



    public void setClickListener(IHistrogramDialogClickListener listner){
        this.listner = listner;
    }


    public HistogramOptionsDialog(Context activity){
        this.context = activity;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        Log.d(TAG, "onCreateDialog: ");
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View rootview = inflater.inflate(R.layout.dialog_histogram_options,null);

        rootview.findViewById(R.id.proceedBtn).setOnClickListener(this);
        rootview.findViewById(R.id.dismissBtn).setOnClickListener(this);

        cb_income=rootview.findViewById(R.id.incomeCheckBox);
        cb_expense=rootview.findViewById(R.id.expenseCheckBox);
        cb_balance = rootview.findViewById(R.id.balanceCheckBox);

        builder.setView(rootview);

        return builder.create();
    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick: ");
        switch (view.getId()){

            case R.id.proceedBtn:
                listner.okClicked(getSelectedSeries());
                break;

            case R.id.dismissBtn:
                dismiss();break;

            default:break;
        }
    }

    private HashMap<String,Boolean> getSelectedSeries(){
        Log.d(TAG, "getSelectedSeries: ");
        HashMap<String,Boolean> selectedSeries=new HashMap<>(); //income,expense,balance;

        selectedSeries.put(INCOME_SERIES,cb_income.isChecked());
        selectedSeries.put(EXPENSE_SERIES,cb_expense.isChecked());
        selectedSeries.put(BALANCE_SERIES,cb_balance.isChecked());

        dismiss();

        return selectedSeries;
    }
}
