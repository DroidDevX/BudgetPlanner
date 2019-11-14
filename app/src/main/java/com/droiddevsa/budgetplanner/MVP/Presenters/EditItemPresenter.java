package com.droiddevsa.budgetplanner.MVP.Presenters;


import com.droiddevsa.budgetplanner.MVP.Data.Models.BudgetItem;
import com.droiddevsa.budgetplanner.MVP.Data.Repository.Repository;
import com.droiddevsa.budgetplanner.MVP.UI.ViewInterface.EditItemView;

public class EditItemPresenter {
    private Repository m_repo;
    private EditItemView m_view;
    private final static String TAG ="EditItemPresenter";

    public EditItemPresenter(Repository repo)
    {
        this.m_repo = repo;
    }

    public void attachView(EditItemView view) {
        this.m_view = view;
    }

    public void viewOnFinishEditClicked(BudgetItem updatedItem)
    {

        int itemID = m_repo.getItemID(updatedItem);

        boolean itemExists = itemID!=-1;

        if(!itemExists)
            itemID=m_repo.insertItem(updatedItem);

        updatedItem.setItemID(itemID);
        m_view.toastMessage("Changes have been saved!");
        m_view.updateItemComplete(updatedItem);
    }


    public void initSpinner()
    {
        m_view.populateSpinner(m_repo.getCategories());
    }




}
