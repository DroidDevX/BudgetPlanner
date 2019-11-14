package com.droiddevsa.budgetplanner.MVP.UI.Charts;


import com.droiddevsa.budgetplanner.MVP.Data.Models.Budget;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;

public class TimeLineXFormatter extends ValueFormatter {
    private static final String TAG = "TimeLineXFormatter";

    ArrayList<Budget> budgetlist;
    int xAxisOffset;

    public TimeLineXFormatter(ArrayList<Budget> budgetlist,int xAxisOffset){
        this.budgetlist= budgetlist;
        this.xAxisOffset= xAxisOffset;
    }

    @Override
    public String getFormattedValue(float value) {
        int xAxisIndex = (int)value;
        int budgetIndex = xAxisIndex-xAxisOffset;

        if(budgetIndex!=-1 && budgetIndex<budgetlist.size())
                return budgetlist.get(budgetIndex).getFormattedDateStr("dd/MM/yyyy");
        else
            return "";

    }
}
