package com.droiddevsa.budgetplanner.MVP.UI.ViewInterface;

import com.droiddevsa.budgetplanner.MVP.MVPContract;

public interface AddItemView extends MVPContract.BaseView {

   void toastMessage(String message);

   void startCurrentBudgetActivity(String budgetID);
}
