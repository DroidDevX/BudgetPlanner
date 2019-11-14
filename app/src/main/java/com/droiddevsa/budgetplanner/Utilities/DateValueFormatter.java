package com.droiddevsa.budgetplanner.Utilities;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateValueFormatter implements IAxisValueFormatter{

        @Override
    public String getFormattedValue(float value, AxisBase axis) {
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd");
            Date date = new Date(new Float(value).longValue());
            return dateFormatter.format(date);
    }
}
