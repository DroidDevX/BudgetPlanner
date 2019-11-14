package com.droiddevsa.budgetplanner.MVP.UI.ViewInterface;

import android.content.Context;
import android.content.Intent;

import com.droiddevsa.budgetplanner.MVP.UI.View.*;
import com.droiddevsa.budgetplanner.MVP.Data.Models.BudgetItem;
import com.droiddevsa.budgetplanner.MVP.UI.View.CurrentBudgetActivity.CurrentBudgetActivity;
import com.droiddevsa.budgetplanner.MVP.UI.View.HomeActivity.HomeActivity;
import com.droiddevsa.budgetplanner.MVP.UI.View.SubtotalActivity.SubtotalActivity;


/**
 * This class is used to navigate across different activities using intents
 */

public class ConcreteActivityNavigator implements ActivityNavigator{
    Context context;
    public static String INTENT_EXTRA_BUDGET_ID = "BUDGET_ID";
    public static String INTENT_EXTRA_BUDGETITEM = "BUDGETITEM";
    public static String INTENT_EXTRA_EDITED_ITEM ="EDITED_ITEM";


    public void attachContext(Context context){
        this.context = context;
    }

    public void detachContext(){
        context=null;
    }

    public void startCurrentBudgetActivity(String budgetID){
        if(context!=null){
            Intent i = new Intent (context, CurrentBudgetActivity.class);
            i.putExtra(INTENT_EXTRA_BUDGET_ID,budgetID);
            context.startActivity(i);
        }
    }

    public void startCurrentBudgetActivity(String budgetID,BudgetItem updatedItem){
        if(context!=null){
            Intent i = new Intent (context, CurrentBudgetActivity.class);
            i.putExtra(INTENT_EXTRA_BUDGET_ID,budgetID);
            i.putExtra(INTENT_EXTRA_EDITED_ITEM,updatedItem);
            context.startActivity(i);
        }
    }

    public void startAddItemActivity(String budgetID){
        if(context!=null){
            Intent i = new Intent (context,AddItemActivity.class);
            i.putExtra(INTENT_EXTRA_BUDGET_ID,budgetID);
            context.startActivity(i);
        }
    }

    public void startEditItemActivity(String budgetID, BudgetItem budgetItem){
        if(context!=null){
            Intent i = new Intent (context,EditItemActivity.class);
            i.putExtra(INTENT_EXTRA_BUDGETITEM,budgetItem);
            i.putExtra(INTENT_EXTRA_BUDGET_ID,budgetID);
            context.startActivity(i);
        }
    }

    public void startBudgetSubtotalActivity(String budgetID){
        if(context!=null){
            Intent i = new Intent(context, SubtotalActivity.class);
            i.putExtra(INTENT_EXTRA_BUDGET_ID,budgetID);
            context.startActivity(i);
        }
    }

    public void startHomeActivity(){
        if(context!=null) {
            Intent i = new Intent(context,HomeActivity.class);
            context.startActivity(i);
        }
    }

}
