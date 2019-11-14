package com.droiddevsa.budgetplanner.MVP.UI.ViewInterface;

import com.droiddevsa.budgetplanner.MVP.Data.Models.CategorySubtotal;
import com.droiddevsa.budgetplanner.MVP.Data.Repository.Repository;

import java.util.ArrayList;

public class SubtotalPresenter {
    private static final String TAG = "SubtotalPresenter";
    SubtotalView view;
    Repository repository;


    public SubtotalPresenter(Repository repository){
        this.repository = repository;
    }

    public void attachView(SubtotalView view){
        this.view = view;
    }

    public void viewOnCreate(String budgetID){
        ArrayList<CategorySubtotal> incSubtotals = repository.getIncomeSubtotals(budgetID);
        view.onIncomeSubtotalsReady(incSubtotals);

        ArrayList<CategorySubtotal> exSubtotals = repository.getExpenseSubtotals(budgetID);
        view.onExpenseSubtotalsReady(exSubtotals);
    }

}
