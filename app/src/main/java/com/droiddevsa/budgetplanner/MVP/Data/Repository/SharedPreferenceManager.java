package com.droiddevsa.budgetplanner.MVP.Data.Repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.SparseBooleanArray;

import androidx.annotation.NonNull;

import com.droiddevsa.budgetplanner.MVP.UI.BudgetRecyclerView.BudgetItemAdapter;

import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

public class SharedPreferenceManager {
    public static final String TAG = "SharedPreferenceManager";
    private static String CONFIG_CURRENT_BUDGET="config_currentBudget";
    private static String CONFIG_HISTOGRAM_CHART="config_histogram";

    Context context;


    public SharedPreferenceManager (@NonNull Context context){
        this.context = context;
    }

    public ConfigHistogram getConfigHistogram(){
        SharedPreferences preferences=context.getSharedPreferences(CONFIG_HISTOGRAM_CHART,MODE_PRIVATE);
        return new ConfigHistogram(preferences);
    }



    public ConfigCurrentBudget getConfigCurrentBudget(){
        SharedPreferences preferences=context.getSharedPreferences(CONFIG_CURRENT_BUDGET,MODE_PRIVATE);
        return new ConfigCurrentBudget(preferences);
    }



    public class ConfigCurrentBudget {
        private static final String TAG = "ConfigCurrentBudget";
        SharedPreferences preferences;

        private  ConfigCurrentBudget(SharedPreferences preferences){
            this.preferences =preferences;
            applyDefaultSettings();
        }

        public void setVisibleColumns(boolean[] visibleColumns){
            Log.d(TAG, "setVisibleColumns: ");

            SharedPreferences.Editor editor = preferences.edit();
            for(int columnPos=0;columnPos<visibleColumns.length;columnPos++)
            {

                if(columnPos==0)
                    editor.putBoolean("ITEM_NAME_VISIBLE",visibleColumns[columnPos]);

                else if(columnPos==1)
                    editor.putBoolean("CATEGORY_VISIBLE",visibleColumns[columnPos]);

                else if(columnPos==2)
                    editor.putBoolean("BRAND_VISIBLE",visibleColumns[columnPos]);

                else if(columnPos==3)
                    editor.putBoolean("AMOUNT_VISIBLE",visibleColumns[columnPos]);

                else if(columnPos==4)
                    editor.putBoolean("QUANTITY_VISIBLE",visibleColumns[columnPos]);

                else if(columnPos==5)
                    editor.putBoolean("CASHFLOW_VISIBLE",visibleColumns[columnPos]);

            }


            editor.putBoolean("DEFAULT_SETTINGS",false);
            editor.apply();
        }

        public SparseBooleanArray getVisibleColumns(){
            Log.d(TAG, "getVisibleColumns: ");

            SparseBooleanArray showColumn = new SparseBooleanArray();

            showColumn.put(BudgetItemAdapter.COLUMN_ITEM_NAME, preferences.getBoolean("ITEM_NAME_VISIBLE",false));

            showColumn.put(BudgetItemAdapter.COLUMN_CATEGORY_NAME, preferences.getBoolean("CATEGORY_VISIBLE",false));

            showColumn.put(BudgetItemAdapter.COLUMN_BRAND, preferences.getBoolean("BRAND_VISIBLE",false));

            showColumn.put(BudgetItemAdapter.COLUMN_AMOUNT, preferences.getBoolean("AMOUNT_VISIBLE",false));

            showColumn.put(BudgetItemAdapter.COLUMN_QUANTITY, preferences.getBoolean("QUANTITY_VISIBLE",false));

            showColumn.put(BudgetItemAdapter.COLUMN_CASHFLOW, preferences.getBoolean("CASHFLOW_VISIBLE",false));

            return  showColumn;
        }

        private void applyDefaultSettings(){
            Log.d(TAG, "setColumnSharedPreferences: ");
            SharedPreferences.Editor editor = preferences.edit();

            boolean useDefaultSettings = preferences.getBoolean("DEFAULT_SETTINGS", true);
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

    }

    public class  ConfigHistogram {
        private static final String TAG = "ConfigHistogram";

        SharedPreferences preferences;
        private String CHART_SERIES_INCOME="Income";
        private String CHART_SERIES_EXPENSE="Expense";
        private String CHART_SERIES_BALANCE="Balance";
        String CHART_USES_DEFAULT_SETTINGS ="chartUsesDefaultSettings";

        private ConfigHistogram(SharedPreferences preferences){
            this.preferences = preferences;

            initDefaultSettings();
        }


        private void initDefaultSettings(){
            Log.d(TAG, "initDefaultSettings: ");
            SharedPreferences.Editor editor = preferences.edit();
            if(preferences.getBoolean(CHART_USES_DEFAULT_SETTINGS,true)){
                editor.putBoolean(CHART_SERIES_INCOME,true);
                editor.putBoolean(CHART_SERIES_EXPENSE,true);
                editor.putBoolean(CHART_SERIES_BALANCE,true);
                editor.putBoolean(CHART_USES_DEFAULT_SETTINGS,false);
            }
            editor.apply();
        }


        public HashMap<String,Boolean> getVisibleSeries(){
            Log.d(TAG, "getVisibleSeries: ");
            HashMap<String,Boolean> visibleSeries = new HashMap<>();

            visibleSeries.put(CHART_SERIES_INCOME,preferences.getBoolean(CHART_SERIES_INCOME,false));
            visibleSeries.put(CHART_SERIES_EXPENSE,preferences.getBoolean(CHART_SERIES_EXPENSE,false));
            visibleSeries.put(CHART_SERIES_BALANCE,preferences.getBoolean(CHART_SERIES_BALANCE,false));

            Log.d(TAG, "getVisibleSeries: "+visibleSeries.toString());

            return visibleSeries;
        }

        public void setVisibleSeries(HashMap<String,Boolean> visibleSeries){
            Log.d(TAG, "setVisibleSeries: ");
            SharedPreferences.Editor editor = preferences.edit();

            for(String key:visibleSeries.keySet()){
                    Boolean seriesIsVisible =visibleSeries.get(key);
                    if(seriesIsVisible==null)
                        continue;

                    switch (key){

                        case "Income":editor.putBoolean(CHART_SERIES_INCOME,seriesIsVisible);break;
                        case "Expense:":editor.putBoolean(CHART_SERIES_EXPENSE,seriesIsVisible); break;
                        case "Balance":editor.putBoolean(CHART_SERIES_BALANCE,seriesIsVisible);break;
                    }

            }
            editor.apply();

        }
    }

}
