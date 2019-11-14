package com.droiddevsa.budgetplanner.MVP.UI.Charts;

public interface BaseBudgetHistoryChart {

    void setIncomeVisibility(Boolean visible);
    void setExpenseVisibility(Boolean visible);
    void setBalanceVisibility(Boolean visible);
    void onXAxisSelectedChanged(int xPosition);
}
