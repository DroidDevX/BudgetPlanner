package com.droiddevsa.budgetplanner.MVP.Presenters;

import android.os.Bundle;
import android.util.Log;

import com.droiddevsa.budgetplanner.Async.BackgroundTask;
import com.droiddevsa.budgetplanner.Async.BackgroundTaskManger;
import com.droiddevsa.budgetplanner.MVP.Data.Models.Budget;
import com.droiddevsa.budgetplanner.MVP.Data.Models.BudgetItem;
import com.droiddevsa.budgetplanner.MVP.Data.Repository.Repository;
import com.droiddevsa.budgetplanner.MVP.UI.ViewInterface.CurrentBudgetView;

import java.util.ArrayList;
import java.util.Date;

public class CurrentBudgetActivityPresenter {

    private final static String TAG = "currBudgetActPresenter";
    Repository repository;
    private CurrentBudgetView view;
    BackgroundTaskManger backgroundTaskManger=null;

    /*-------------------------------------------------------------------------------------*/
    public CurrentBudgetActivityPresenter(Repository repo)
    {
        this.repository = repo;
    }
    public void attachView(CurrentBudgetView view)
    {
        this.view = view;
    }
    public void setBackgroundTaskManger(BackgroundTaskManger backgroundTaskManger){this.backgroundTaskManger=backgroundTaskManger;}
    /*-------------------------------------------------------------------------------------*/

    public void viewOnResume(final String budgetID)
    {
        Log.d(TAG,"onChartSettingsReady()");

        if(backgroundTaskManger==null )return;

        BackgroundTask task = new BackgroundTask() {
            String RESULT_BUDGET="budget";
            String RESULT_BUDGETITEMS="budgetItems";
            @Override
            public Bundle doInBackground() {
                Bundle result = new Bundle();
                result.putParcelable(RESULT_BUDGET,repository.getBudget(budgetID));
                result.putParcelableArrayList(RESULT_BUDGETITEMS,repository.getItemEntries(budgetID));

                return result;
            }

            @Override
            public void onPostExecute(Bundle result) {
                Budget budget = result.getParcelable(RESULT_BUDGET);
                ArrayList<BudgetItem> items =result.getParcelableArrayList(RESULT_BUDGETITEMS);
                budget.setBudgetItems(items);
                view.onBudgetItemsReady(budget,repository.getItemColumnsToShow());
            }
        };
        backgroundTaskManger.executeTask(task);
    }

    public void viewOnDeleteItemBtnClicked(final String budgetID, final BudgetItem selectedItem){
        Log.d(TAG, "viewOnDeleteItemBtnClicked: ");
        if(backgroundTaskManger==null)
            return;

        BackgroundTask task = new BackgroundTask() {
            String RESULT_BUDGET="budget";
            String RESULT_BUDGET_ITEMS="budgetItems";

            @Override
            public Bundle doInBackground() {
                repository.deleteItemEntry(selectedItem);
                Budget budget =repository.getBudget(budgetID);
                Bundle result = new Bundle();
                result.putParcelable(RESULT_BUDGET,budget);
                result.putParcelableArrayList(RESULT_BUDGET_ITEMS,repository.getItemEntries(budgetID));
                return result;
            }

            @Override
            public void onPostExecute(Bundle result) {
                Budget budget = result.getParcelable(RESULT_BUDGET);
                ArrayList<BudgetItem> budgetItems = result.getParcelableArrayList(RESULT_BUDGET_ITEMS);
                budget.setBudgetItems(budgetItems);
                view.onBudgetItemsReady(budget,repository.getItemColumnsToShow());
            }
        };
        backgroundTaskManger.executeTask(task);
    }

    public void viewOnColumnsToShowChanged(String budgetID,boolean[] columnVisibility) {
        Log.d(TAG, "viewOnColumnsToShowChanged: ");
        Budget budget =repository.getBudget(budgetID);
        budget.setBudgetItems(repository.getItemEntries(budgetID));
        repository.setColumnsToShow(columnVisibility);
        view.onBudgetItemsReady(budget, repository.getItemColumnsToShow());

    }

    public void saveChanges( final String budgetID,final Date createdDate, final ArrayList<BudgetItem> itemList){
        Log.d(TAG, "saveChanges: ");
        repository.setBudgetDate(budgetID,createdDate);
        if(itemList==null|| itemList.size()==0) {
            return;
        }

        repository.deleteItemEntries(budgetID);
        repository.insertItemEntries(budgetID,itemList);

    }

}

