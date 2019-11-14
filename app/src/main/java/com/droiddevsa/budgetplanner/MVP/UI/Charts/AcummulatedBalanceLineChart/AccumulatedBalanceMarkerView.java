package com.droiddevsa.budgetplanner.MVP.UI.Charts.AcummulatedBalanceLineChart;

import android.content.Context;
import android.widget.TextView;

import com.droiddevsa.budgetplanner.MVP.Data.Models.Budget;
import com.droiddevsa.budgetplanner.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;

import java.util.ArrayList;
import java.util.Locale;

public class AccumulatedBalanceMarkerView extends MarkerView {

    ArrayList<Budget> budgetlist;
    TextView markerDateTV;
    TextView markerAmountTV;
    int XAxisIndexOffset;

    public AccumulatedBalanceMarkerView(Context context, int layoutResource, ArrayList<Budget> budgetlist,int XAxisIndexOffset) {
        super(context, layoutResource);
        markerDateTV =findViewById(R.id.dateMarkerTV);
        markerAmountTV = findViewById(R.id.amountMarkerTV);
        this.budgetlist= budgetlist;
        this.XAxisIndexOffset = XAxisIndexOffset;
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        int selectedIndex = (int)e.getX()- XAxisIndexOffset;
        if(selectedIndex==-1)
            return;

        markerDateTV.setText(String.format(Locale.ENGLISH,"%s",budgetlist.get(selectedIndex).getFormattedDateStr("dd/MM/yyyy")));
        markerAmountTV.setText(String.format(Locale.ENGLISH,"Amount: %.2f",e.getY()));

        super.refreshContent(e, highlight);

    }
}
