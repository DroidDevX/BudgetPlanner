package com.droiddevsa.budgetplanner.MVP.UI.ViewInterface;

import com.droiddevsa.budgetplanner.MVP.Data.Models.CategorySubtotal;
import com.droiddevsa.budgetplanner.MVP.MVPContract;

import java.util.ArrayList;

public interface SubtotalView extends MVPContract.BaseView {

    void onIncomeSubtotalsReady(ArrayList<CategorySubtotal> subtotals);
    void onExpenseSubtotalsReady(ArrayList<CategorySubtotal> subtotals);

}
