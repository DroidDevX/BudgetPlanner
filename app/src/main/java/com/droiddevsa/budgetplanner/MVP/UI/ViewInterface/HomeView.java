package com.droiddevsa.budgetplanner.MVP.UI.ViewInterface;

import com.droiddevsa.budgetplanner.MVP.Data.Models.Budget;
import com.droiddevsa.budgetplanner.MVP.MVPContract;

import java.util.ArrayList;

public interface HomeView extends MVPContract.BaseView {

    void onBudgetsReady(ArrayList<Budget> budgets);
    void onBudgetCreated(String newBudgetID);
    void toastMessage(String message);
    void displayGetStartedMessage(boolean isVisible);
    void displayloadingScreen(boolean isVisible);


}
