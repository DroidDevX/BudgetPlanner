package com.droiddevsa.budgetplanner.MVP.Data.Repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.SparseBooleanArray;

import com.droiddevsa.budgetplanner.MVP.Data.Models.Budget;
import com.droiddevsa.budgetplanner.MVP.Data.Models.BudgetItem;
import com.droiddevsa.budgetplanner.MVP.Data.Models.CategorySubtotal;
import com.droiddevsa.budgetplanner.MVP.MVPContract;
import com.droiddevsa.budgetplanner.MVP.UI.BudgetRecyclerView.BudgetItemAdapter;

import java.util.ArrayList;
import java.util.Date;

public class Repository implements MVPContract.BaseRepository {
    final String TAG = "Repository";
    SqliteDatabaseManager database;

    @Deprecated
    SharedPreferences columnsPreferences;
    SharedPreferenceManager sharedPreferenceManager;

    //Repository initialization of members
    public Repository() {
    }

    public void setDatabaseManager(Context context, String DATABASE_NAME, int DATABASE_VERSION) {
        Log.d(TAG, "setDatabaseManager: ");
        database = new SqliteDatabaseManager(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void setSharedPreferenceManager(SharedPreferenceManager manager){
        this.sharedPreferenceManager = manager;
    }

    @Deprecated
    public void setColumnSharedPreferences(SharedPreferences config) {
        Log.d(TAG, "setColumnSharedPreferences: ");
        this.columnsPreferences = config;
        SharedPreferences.Editor editor = columnsPreferences.edit();

        boolean useDefaultSettings = config.getBoolean("DEFAULT_SETTINGS", true);
        if (useDefaultSettings) {

            editor.putBoolean("ITEM_NAME_VISIBLE", true);

            editor.putBoolean("CATEGORY_VISIBLE", true);

            editor.putBoolean("BRAND_VISIBLE", false);

            editor.putBoolean("AMOUNT_VISIBLE", true);

            editor.putBoolean("QUANTITY_VISIBLE", true);

            editor.putBoolean("CASHFLOW_VISIBLE", false);

            editor.putBoolean("DEFAULT_SETTINGS", false);
            editor.apply();
        }

    }


    //Shared Preference
    @Deprecated
    public SparseBooleanArray getItemColumnsToShow(){
        Log.i(TAG,"getItemColumnsToShow()");
        SparseBooleanArray showColumn = new SparseBooleanArray();

        Log.i(TAG,"ITEM_NAME_VISIBLE: "+ (columnsPreferences.getBoolean("ITEM_NAME_VISIBLE", false)));
        showColumn.put(BudgetItemAdapter.COLUMN_ITEM_NAME, columnsPreferences.getBoolean("ITEM_NAME_VISIBLE",false));

        Log.i(TAG,"CATEGORY_VISIBLE: "+ (columnsPreferences.getBoolean("CATEGORY_VISIBLE", false)));
        showColumn.put(BudgetItemAdapter.COLUMN_CATEGORY_NAME, columnsPreferences.getBoolean("CATEGORY_VISIBLE",false));

        Log.i(TAG,"BRAND_VISIBLE: "+ (columnsPreferences.getBoolean("BRAND_VISIBLE", false)));
        showColumn.put(BudgetItemAdapter.COLUMN_BRAND, columnsPreferences.getBoolean("BRAND_VISIBLE",false));

        Log.i(TAG,"AMOUNT_VISIBLE: "+(columnsPreferences.getBoolean("AMOUNT_VISIBLE", false)));
        showColumn.put(BudgetItemAdapter.COLUMN_AMOUNT, columnsPreferences.getBoolean("AMOUNT_VISIBLE",false));

        Log.i(TAG,"QUANTITY_VISIBLE: "+ (columnsPreferences.getBoolean("QUANTITY_VISIBLE", false)));
        showColumn.put(BudgetItemAdapter.COLUMN_QUANTITY, columnsPreferences.getBoolean("QUANTITY_VISIBLE",false));

        Log.i(TAG,"CASH_VISIBLE: "+ (columnsPreferences.getBoolean("NUMBER_VISIBLE", false)));
        showColumn.put(BudgetItemAdapter.COLUMN_CASHFLOW, columnsPreferences.getBoolean("CASHFLOW_VISIBLE",false));

        return  showColumn;
    }
    @Deprecated
    public void setColumnsToShow(boolean[] columnVisible) {
        Log.d(TAG, "setColumnsToShow: ");
        for(int i=0;i< columnVisible.length;i++)
            Log.e(TAG,"Column "+i+" "+columnVisible[i]);

        SharedPreferences.Editor editor = columnsPreferences.edit();
        for(int columnPos=0;columnPos<columnVisible.length;columnPos++)
        {

            if(columnPos==0)
                editor.putBoolean("ITEM_NAME_VISIBLE",columnVisible[columnPos]);

            else if(columnPos==1)
                editor.putBoolean("CATEGORY_VISIBLE",columnVisible[columnPos]);

            else if(columnPos==2)
                editor.putBoolean("BRAND_VISIBLE",columnVisible[columnPos]);

            else if(columnPos==3)
                editor.putBoolean("AMOUNT_VISIBLE",columnVisible[columnPos]);

            else if(columnPos==4)
                editor.putBoolean("QUANTITY_VISIBLE",columnVisible[columnPos]);

            else if(columnPos==5)
                editor.putBoolean("CASHFLOW_VISIBLE",columnVisible[columnPos]);

        }
        editor.putBoolean("DEFAULT_SETTINGS",false);
        editor.apply();
    }



    // SQL CRUD OPERATIONS

    // Budget Table

    public void createNewBudget() {
        Log.d(TAG, "createNewBudget: ");
        database.createNewBudget();
    }
    public String getLatestBudgetID(){
        Log.d(TAG, "getLatestBudgetID: ");
        return String.valueOf(database.getLastGeneratedBudgetID());
    }
    public Budget getBudget(String budgetID){
        Log.d(TAG, "getBudget: ");
        Budget budget = database.getBudget(budgetID);
        return budget;
    }
    public void deleteBudget(String budgetID){
        Log.d(TAG, "deleteBudget: ");
        database.deleteBudget(budgetID);
    }
    public ArrayList<Budget> getAllBudgets(){
        Log.d(TAG, "getAllBudgets: ");
       ArrayList<Budget> budgetlist = database.getAllBudgets();
       if(budgetlist==null)
           return null;

      for(Budget budget:budgetlist){
          String budgetID = String.valueOf(budget.getBudgetID());
           budget.setBudgetItems(getItemEntries(budgetID));
       }

        return budgetlist;

    }
    public void setBudgetDate(String budgetID, Date newDate){
        Log.d(TAG, "setBudgetDate: ");
        database.setBudgetDate(budgetID,newDate);
        Log.e(TAG,"After updating");
        ArrayList<Budget> budgetList = getAllBudgets();
        if(budgetList==null)
            Log.e(TAG,"List of budgets is empty");
        else
            for(Budget budget:budgetList)
                Log.e(TAG,budget.toString());
    }

    //Categories table
    public ArrayList<String> getCategories()
    {
        Log.d(TAG, "getCategories: ");
        return database.getCategories();
    }

    //Item entries table
    public ArrayList<BudgetItem> getItemEntries(String budgetID){
        Log.d(TAG, "getItemEntries: ");
        ArrayList<BudgetItem > budgetItems = database.getItemEntries(budgetID);
        /*Log.e(TAG,"Item entries");
        for(BudgetItem budget:budgetItems)
            Log.e(TAG,budget.toString());*/
        return budgetItems;
    }
    public void deleteItemEntry(BudgetItem budgetItem){
        Log.d(TAG, "deleteItemEntry: ");
        database.deleteBudgetItemEntry(budgetItem);
    }
    public void deleteItemEntries(String budgetID){
        Log.d(TAG, "deleteItemEntries: ");
        database.deleteBudgetItemEntries(budgetID);
    }
    public void insertItemEntries(String budgetID,ArrayList<BudgetItem> items){
        Log.d(TAG, "insertItemEntries: ");
        database.insertMultipleItemEntries(budgetID,items);
    }
    public void insertItemEntry(String BUDGET_ID, BudgetItem newItem) {
        Log.d(TAG, "insertItemEntry: ");
        database.insertItemEntry(BUDGET_ID,newItem);
    }
    public ArrayList<CategorySubtotal> getIncomeSubtotals(String budgetID){
        Log.d(TAG,"getIncomeSubtotal(), budgetID: "+budgetID);
        return database.getSubtotalsByCategory(budgetID,"INCOME");
    }
    public ArrayList<CategorySubtotal> getExpenseSubtotals(String budgetID){
        Log.d(TAG,"getExpenseSubtotal(), budgetID: "+budgetID);
        return database.getSubtotalsByCategory(budgetID,"EXPENSE");
    }

    //Item Table
    public int getItemID(BudgetItem item){
            Log.d(TAG, "getItemID: ");
            return database.getItemID(item.get_itemName(),item.getBrand(),item.getCategoryName());
    }
    public int insertItem(BudgetItem newItem){
        Log.d(TAG, "insertItem: ");
        return database.insertItem(newItem); //AUTO_INCREMENT ID
    }


}
