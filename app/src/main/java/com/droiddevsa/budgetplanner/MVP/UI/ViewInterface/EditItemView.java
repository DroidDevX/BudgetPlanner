package com.droiddevsa.budgetplanner.MVP.UI.ViewInterface;

import com.droiddevsa.budgetplanner.MVP.Data.Models.BudgetItem;
import com.droiddevsa.budgetplanner.MVP.MVPContract;

import java.util.ArrayList;

public interface EditItemView extends MVPContract.BaseView {

    void populateSpinner(ArrayList<String> categories);

    void updateItemComplete(BudgetItem updatedItemData);

    void toastMessage(String message);

}
