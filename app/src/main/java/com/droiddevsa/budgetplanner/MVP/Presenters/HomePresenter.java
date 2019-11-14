package com.droiddevsa.budgetplanner.MVP.Presenters;
import android.os.Bundle;
import android.util.Log;

import com.droiddevsa.budgetplanner.Async.BackgroundTask;
import com.droiddevsa.budgetplanner.Async.BackgroundTaskManger;
import com.droiddevsa.budgetplanner.MVP.Data.Models.Budget;
import com.droiddevsa.budgetplanner.MVP.Data.Repository.Repository;
import com.droiddevsa.budgetplanner.MVP.UI.ViewInterface.HomeView;
import com.droiddevsa.budgetplanner.MVP.MVPContract;

import java.util.ArrayList;

public class HomePresenter implements MVPContract.BasePresenter {
    private static String TAG = "HomePresenter";
    Repository repository;
    HomeView view;
    BackgroundTaskManger backgroundTaskManger;

    public HomePresenter(Repository repository) {
        this.repository = repository;
    }

    public void setBackgroundTaskManger(BackgroundTaskManger taskManger) {
        this.backgroundTaskManger = taskManger;
    }

    public void attachView(HomeView view) {
        this.view = view;
    }

    public void deleteBudget(final String budgetID) {
        Log.d(TAG, "deleteBudget: ");
        if (backgroundTaskManger == null)
            return;


        BackgroundTask task = new BackgroundTask() {
            String BUNDLE_RESULT_BUDGETLIST = "allbudgets";

            @Override
            public Bundle doInBackground() {
                repository.deleteBudget(budgetID);
                ArrayList<Budget> allbudgets = repository.getAllBudgets();
                Bundle result = new Bundle();
                result.putParcelableArrayList(BUNDLE_RESULT_BUDGETLIST, allbudgets);
                return result;
            }

            @Override
            public void onPostExecute(Bundle result) {
                if (result != null) {
                    ArrayList<Budget> allbudgetslist = result.getParcelableArrayList(BUNDLE_RESULT_BUDGETLIST);
                    if (allbudgetslist == null)
                        view.displayGetStartedMessage(true);

                    view.onBudgetsReady(allbudgetslist);
                    view.toastMessage("Selected budget has been deleted.");
                }

            }
        };

        backgroundTaskManger.executeTask(task);

    }

    public void createBudget() {
        Log.d(TAG, "createBudget: ");

        if (backgroundTaskManger == null)
            return;

        BackgroundTask task = new BackgroundTask() {
            String BUNDLE_RESULT_BUDGETID_AUTOINCREMENT = "newbudgetID";

            @Override
            public Bundle doInBackground() {
                repository.createNewBudget();
                Bundle result = new Bundle();
                result.putString(BUNDLE_RESULT_BUDGETID_AUTOINCREMENT, repository.getLatestBudgetID());
                return result;
            }

            @Override
            public void onPostExecute(Bundle result) {
                Log.d(TAG, "onPostExecute: ");
                String budgetID = result.getString(BUNDLE_RESULT_BUDGETID_AUTOINCREMENT);
                view.toastMessage("New budget created, opening spreadsheet...");
                view.onBudgetCreated(budgetID);
            }
        };
        backgroundTaskManger.executeTask(task);

    }

    public void onViewResume() {
        Log.d(TAG, "onViewResume: ");

        if (backgroundTaskManger == null)
            return;

        BackgroundTask task = new BackgroundTask() {
            String BUNDLE_RESULT_BUDGETLIST = "allbudgets";

            @Override
            public Bundle doInBackground() {

                Log.d(TAG, "doInBackground: ");
                Bundle result = new Bundle();
                result.putParcelableArrayList(BUNDLE_RESULT_BUDGETLIST, repository.getAllBudgets());
                return result;

            }

            @Override
            public void onPostExecute(Bundle result) {
                Log.d(TAG, "onPostExecute: ");

                ArrayList<Budget> allbudgets = result.getParcelableArrayList(BUNDLE_RESULT_BUDGETLIST);
                view.displayloadingScreen(false);

                if (allbudgets == null)
                        view.displayGetStartedMessage(true);

                else {

                    view.displayGetStartedMessage(false);
                    view.onBudgetsReady(allbudgets);
                }
            }
        };
        backgroundTaskManger.executeTask(task);

    }


}