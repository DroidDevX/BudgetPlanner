package com.droiddevsa.budgetplanner.MVP.UI.ViewInterface;

import com.droiddevsa.budgetplanner.MVP.Data.Models.BudgetItem;

public interface ActivityNavigator {


    void startCurrentBudgetActivity(String budgetID);
    void startCurrentBudgetActivity(String budgetID, BudgetItem updatedItem);
    void startAddItemActivity(String budgetID);
    void startEditItemActivity(String budgetID, BudgetItem budgetItem);
    void startBudgetSubtotalActivity(String budgetID);
}
