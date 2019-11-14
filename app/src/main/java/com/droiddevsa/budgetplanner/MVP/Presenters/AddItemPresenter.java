package com.droiddevsa.budgetplanner.MVP.Presenters;
import android.os.Bundle;

import com.droiddevsa.budgetplanner.Async.BackgroundTask;
import com.droiddevsa.budgetplanner.Async.BackgroundTaskManger;
import com.droiddevsa.budgetplanner.MVP.Data.Models.BudgetItem;
import com.droiddevsa.budgetplanner.MVP.Data.Repository.Repository;
import com.droiddevsa.budgetplanner.MVP.UI.ViewInterface.AddItemView;

public class AddItemPresenter {
    private static final String TAG = "AddItemPresenter";

    private Repository m_repo;
    private AddItemView m_view;
    private BackgroundTaskManger backgroundTaskManger=null;

    public AddItemPresenter(Repository repo)
    {
        this.m_repo = repo;
       }

    public void attachView(AddItemView view)
    {
            this.m_view = view;
    }


    public void setBackgroundTaskManger(BackgroundTaskManger taskManger){
        this.backgroundTaskManger = taskManger;
    }

    public void viewAddItemClicked(final String budgetID,final BudgetItem newItem)
    {
        if(backgroundTaskManger==null)
            return;

        BackgroundTask myTask=new BackgroundTask() {
            @Override
            public Bundle doInBackground() {

                  int itemID= m_repo.getItemID(newItem);
                  boolean itemExists = itemID!=-1;
                  if(!itemExists)
                        itemID =m_repo.insertItem(newItem);

                  newItem.setItemID(itemID);
                  m_repo.insertItemEntry(budgetID,newItem);
                  return null; //Return null result
            }

            @Override
            public void onPostExecute(Bundle result) {
                m_view.toastMessage("New Entry has been added!");
                m_view.startCurrentBudgetActivity(budgetID);
            }
        };

        backgroundTaskManger.executeTask(myTask);

    }


}
