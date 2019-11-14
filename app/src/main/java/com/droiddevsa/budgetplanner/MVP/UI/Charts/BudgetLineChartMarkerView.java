package com.droiddevsa.budgetplanner.MVP.UI.Charts;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.droiddevsa.budgetplanner.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BudgetLineChartMarkerView extends MarkerView {
    private static final String TAG = "BudgetLChartMarkerView";
    private TextView markerDate;
    private TextView markerAmount;

    public BudgetLineChartMarkerView(Context context, int layoutResource){
        super(context,layoutResource);
        Log.e(TAG,"BudgetLineChart");
        markerDate = findViewById(R.id.date_timelineMarkerTV);
        markerAmount = findViewById(R.id.amount_timelineMarkerTV);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        Log.d(TAG, "refreshContent: ");
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yy", Locale.ENGLISH);
        Date date = new Date((long)e.getX());
        String dateValue = dateFormatter.format(date);

        markerDate.setText(String.format(Locale.ENGLISH,"Test"));
        markerAmount.setText(String.format(Locale.ENGLISH,"Test"));

        /*
        * markerDate.setText(String.format(Locale.ENGLISH,"%s",dateValue));
        markerAmount.setText(String.format(Locale.ENGLISH,"Amount: %.2f",e.getY()));
        * */


        super.refreshContent(e, highlight);
    }

}
