package com.droiddevsa.budgetplanner.MVP.UI.View.CurrentBudgetActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment {
    DatePickerFragmentListener activity;

    public interface DatePickerFragmentListener extends DatePickerDialog.OnDateSetListener {
        void onDatePickerDismissed();
        void onDatePickerCancel();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (DatePickerFragmentListener)context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        return new android.app.DatePickerDialog(getActivity(),(DatePickerDialog.OnDateSetListener)getActivity(),
                                                year,month,day);
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        activity.onDatePickerDismissed();
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        activity.onDatePickerCancel();;
    }
}
