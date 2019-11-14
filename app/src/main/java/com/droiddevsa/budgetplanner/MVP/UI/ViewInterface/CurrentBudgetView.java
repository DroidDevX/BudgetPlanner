package com.droiddevsa.budgetplanner.MVP.UI.ViewInterface;

import android.util.SparseBooleanArray;

import com.droiddevsa.budgetplanner.MVP.Data.Models.Budget;
import com.droiddevsa.budgetplanner.MVP.MVPContract;

public interface CurrentBudgetView extends MVPContract.BaseView {

    void toastMessage(String toastMessage);

    void onBudgetItemsReady(Budget currentBudget, SparseBooleanArray columnsToShow);


}
