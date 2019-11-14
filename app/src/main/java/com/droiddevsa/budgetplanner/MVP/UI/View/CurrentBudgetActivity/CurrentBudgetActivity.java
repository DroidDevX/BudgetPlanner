package com.droiddevsa.budgetplanner.MVP.UI.View.CurrentBudgetActivity;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.droiddevsa.budgetplanner.Async.BackgroundTaskManger;
import com.droiddevsa.budgetplanner.Async.RxJavaWrapper;
import com.droiddevsa.budgetplanner.MVP.Data.Models.Budget;
import com.droiddevsa.budgetplanner.MVP.Data.Models.BudgetItem;
import com.droiddevsa.budgetplanner.MVP.Presenters.CurrentBudgetActivityPresenter;
import com.droiddevsa.budgetplanner.MVP.UI.BudgetRecyclerView.BudgetItemAdapter;
import com.droiddevsa.budgetplanner.MVP.UI.ViewInterface.ActivityNavigator;
import com.droiddevsa.budgetplanner.MVP.UI.ViewInterface.ConcreteActivityNavigator;
import com.droiddevsa.budgetplanner.R;
import com.droiddevsa.budgetplanner.MVP.Data.Repository.Repository;
import com.droiddevsa.budgetplanner.MVP.UI.ViewInterface.CurrentBudgetView;
import com.droiddevsa.budgetplanner.Utilities.SingleClickListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.droiddevsa.budgetplanner.MVP.UI.ViewInterface.ConcreteActivityNavigator.INTENT_EXTRA_BUDGET_ID;


public class CurrentBudgetActivity extends AppCompatActivity
        implements CurrentBudgetView , DatePickerFragment.DatePickerFragmentListener {
    private static String TAG = CurrentBudgetActivity.class.getSimpleName();

    DatePickerFragment datePickerFragment;
    Date budgetDateCreated;
    String budgetID;
    SingleClickListener onSingleClickListener;
    View loadingStatusView;

    BudgetItemAdapter budgetItemAdapter;
    RecyclerView recyclerView;
    ConcreteActivityNavigator activityNavigator = new ConcreteActivityNavigator();

    BackgroundTaskManger rxJavaWrapper = new RxJavaWrapper();
    CurrentBudgetActivityPresenter budgetPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_budget);
        Log.d(TAG,"onCreate()");

        //Init UI
        datePickerFragment = createDatePickerFragment();
        onSingleClickListener = createOnClickListener();
        startfindViewByIDs();

        //Budget item Recycler-view
        budgetItemAdapter = new BudgetItemAdapter();
        recyclerView.setAdapter(budgetItemAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        setupRecyclerViewTouchHelper();

        //Init repository
        Repository repository = new Repository();
        repository.setDatabaseManager(this,getResources().getString(R.string.DATABASE_NAME),getResources().getInteger(R.integer.DATBASE_VERSION));
        SharedPreferences columnConfig = getSharedPreferences(getResources().getString(R.string.column_config),MODE_PRIVATE);
        repository.setColumnSharedPreferences(columnConfig);

        //Init presenter
        budgetPresenter = new CurrentBudgetActivityPresenter(repository);
        budgetPresenter.attachView(this);
        budgetPresenter.setBackgroundTaskManger(rxJavaWrapper);

        activityNavigator.attachContext(this);
    }




    public void startfindViewByIDs(){
        recyclerView = findViewById(R.id.budgetItemRecyclerView);
        loadingStatusView =findViewById(R.id.loadingConstraintLayout);

        findViewById(R.id.addEntryButton).setOnClickListener(onSingleClickListener);
        findViewById(R.id.deleteEntryButton).setOnClickListener(onSingleClickListener);
        findViewById(R.id.editEntryButton).setOnClickListener(onSingleClickListener);
        findViewById(R.id.overviewBudgetBtn).setOnClickListener(onSingleClickListener);
        findViewById(R.id.displayColumnsBtn).setOnClickListener(onSingleClickListener);
        findViewById(R.id.datePickerView).setOnClickListener(onSingleClickListener);

    }

    public SingleClickListener createOnClickListener(){
        Log.d(TAG, "createOnClickListener: ");
        return new SingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                Log.d(TAG, "onSingleClick: ");

                switch(view.getId()){

                    case R.id.addEntryButton: addButtonClicked(); break;

                    case R.id.editEntryButton: editItemButtonClicked(); break;

                    case R.id.deleteEntryButton: deleteButtonClicked(); break;

                    case R.id.displayColumnsBtn: displayColumnsButtonClicked(); break;

                    case R.id.overviewBudgetBtn: overviewButtonClicked(); break;

                    case R.id.datePickerView:datePickerClicked();break;


                    default: break;
                }
            }
        };
    }

    public DatePickerFragment createDatePickerFragment(){
        return new DatePickerFragment();
    }

    public void datePickerClicked(){
        datePickerFragment.show(getSupportFragmentManager(),"datePicker");
    }

    public void addButtonClicked(){
        loadingStatusView.setVisibility(View.VISIBLE);
        activityNavigator.startAddItemActivity(budgetID);
    }

    public void overviewButtonClicked(){
        if(budgetItemAdapter.getBudgetListSize()==0) {
            onSingleClickListener.setClickEnabled(true);
            toastMessage("Budget has no entries.\nClick on the + button to add a new entry.");
        }
        else {
            loadingStatusView.setVisibility(View.VISIBLE);
            activityNavigator.startBudgetSubtotalActivity(budgetID);
        }
    }

    public void displayColumnsButtonClicked(){


        final String[] columns = getResources().getStringArray(R.array.column_display);
        final boolean[] columnToShow = new boolean[columns.length];

        AlertDialog.Builder builder = new AlertDialog.Builder(CurrentBudgetActivity.this);

        builder.setTitle("Select columns to display");
        //Menu items
        builder.setMultiChoiceItems(columns, columnToShow, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int itemPosition, boolean isChecked) {
                columnToShow[itemPosition] = isChecked;
            }
        });

        builder.setCancelable(false);
        //Ok button
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                onSingleClickListener.setClickEnabled(true);
                budgetPresenter.viewOnColumnsToShowChanged(budgetID,columnToShow);

            }
        });

        //Cancel button
        builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                onSingleClickListener.setClickEnabled(true);
                dialog.dismiss();
            }
        });

        builder.create();
        builder.show();
    }

    public void deleteButtonClicked(){
        Log.d(TAG, "deleteButtonClicked: ");
        if(budgetItemAdapter.getBudgetListSize()==0) {
            onSingleClickListener.setClickEnabled(true);
            Toast.makeText(this, "No entries have been added yet. Click '+' to add an entry", Toast.LENGTH_LONG).show();
            return;
        }

        BudgetItem selectedItem = budgetItemAdapter.getSelectedItem();

        if(selectedItem==null) {
            onSingleClickListener.setClickEnabled(true);
            Toast.makeText(this, "Unable to delete no items have been selected", Toast.LENGTH_LONG).show();
            return;
        }

        budgetID = getIntent().getStringExtra(INTENT_EXTRA_BUDGET_ID);
        budgetPresenter.viewOnDeleteItemBtnClicked(budgetID,selectedItem);


    }

    public void editItemButtonClicked(){
        BudgetItem itemToEdit = budgetItemAdapter.getSelectedItem();
        if(itemToEdit!=null) {
            loadingStatusView.setVisibility(View.VISIBLE);
            activityNavigator.startEditItemActivity(budgetID,itemToEdit);
        }
        else {
            Toast.makeText(this, "Select an entry to edit, then try again", Toast.LENGTH_LONG).show();
            onSingleClickListener.setClickEnabled(true);
        }

    }


    @Override
    public void onBudgetItemsReady(Budget budget, SparseBooleanArray columnsToShow) {
        Log.d(TAG, "onBudgetItemsReady: ");
        onSingleClickListener.setClickEnabled(true);

        budgetDateCreated = budget.getCreatedDate();
        SimpleDateFormat dateformatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        ((TextView)findViewById(R.id.dateTitleTV)).setText(dateformatter.format(budgetDateCreated));

        setRecyclerViewHeaderColumns(columnsToShow);

        budgetItemAdapter.setVisibleColumns(columnsToShow);
        setRecyclerViewItems(budget,columnsToShow);
        budgetItemAdapter.notifyDataSetChanged();

        setBudgetSummaryData(budget);
    }

    public void setRecyclerViewHeaderColumns(SparseBooleanArray columnIsVisible){
        int backgroudColor = getResources().getColor(R.color.colorPrimaryLight_new);
        LinearLayout recyclerViewHeader = findViewById(R.id.recyclerViewHeader);
        recyclerViewHeader.setBackgroundColor(backgroudColor);

        int textColor = getResources().getColor(R.color.colorMainTextView);
        int columnPos=0;
        while(columnPos<columnIsVisible.size()){

            TextView recyclerViewItemColumn = (TextView)recyclerViewHeader.getChildAt(columnPos);

            int visibility= columnIsVisible.get(columnPos)? View.VISIBLE:View.GONE;

            recyclerViewItemColumn.setVisibility(visibility);

            recyclerViewItemColumn.setTextColor(textColor);

            columnPos++;
        }

    }

    public void setupRecyclerViewTouchHelper(){
        ItemTouchHelper touchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {

                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder draggedItem, @NonNull RecyclerView.ViewHolder targetItem) {
                Log.i(TAG,"onMove");

                budgetItemAdapter.swapBudgetItems(draggedItem.getAdapterPosition(),targetItem.getAdapterPosition());


                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }

            @Override
            public boolean isLongPressDragEnabled() {
                return true;
            }

            @Override
            public boolean isItemViewSwipeEnabled() {
                return false;
            }
        });

        touchHelper.attachToRecyclerView(recyclerView);

    }

    public void setRecyclerViewItems(Budget budget,SparseBooleanArray columnsToShow){
        budgetItemAdapter.updateBudgetItemList(budget.getBudgetItemList());
        budgetItemAdapter.resetSelectedItem();
        budgetItemAdapter.setVisibleColumns(columnsToShow);
    }

    public void setBudgetSummaryData(Budget budget){
        Log.d(TAG,"setBudgetSummaryData ");
        ((TextView)findViewById(R.id.totalBalanceTV)).setText(String.format(Locale.ENGLISH,"%.2f",budget.getBalance()));
        ((TextView)findViewById(R.id.totalIncomeTextView)).setText(String.format(Locale.ENGLISH,"%.2f",budget.getTotalIncome()));
        ((TextView)findViewById(R.id.totalExpenseTV)).setText(String.format(Locale.ENGLISH,"%.2f",budget.getTotalExpense()));
    }

    @Override
    public void toastMessage(String toastMessage) {
        Toast.makeText(this,toastMessage,Toast.LENGTH_LONG).show();
    }

    @Override
    public ActivityNavigator getActivityNavigator() {
        return activityNavigator;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Log.d(TAG, "onDateSet: ");
        onSingleClickListener.setClickEnabled(true);
        budgetDateCreated = new GregorianCalendar(year,month,day).getTime();

        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy",Locale.ENGLISH);


        ((TextView)findViewById(R.id.dateTitleTV)).setText(dateFormatter.format(budgetDateCreated));
    }

    @Override
    public void onDatePickerDismissed() {
        Log.d(TAG, "onDatePickerDismissed: ");
        onSingleClickListener.setClickEnabled(true);
    }

    @Override
    public void onDatePickerCancel() {
        Log.d(TAG, "onDatePickerCancel: ");
        onSingleClickListener.setClickEnabled(true);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        LinearLayout linearLayout = findViewById(R.id.summaryLinearLayout);
        if(newConfig.orientation== Configuration.ORIENTATION_LANDSCAPE)
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        else
            linearLayout.setOrientation(LinearLayout.VERTICAL);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(intent!=null)
            setIntent(intent);
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop: ");
        super.onStop();
        saveChanges();
    }

    public void saveChanges(){
        Future<Void> result = Executors.newSingleThreadExecutor().submit(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                budgetPresenter.saveChanges(budgetID,budgetDateCreated,budgetItemAdapter.getBudgetItemList());
                return  null;
            }
        });

        try {result.get();}
        catch (ExecutionException e){}
        catch (InterruptedException e){}
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        loadingStatusView.setVisibility(View.GONE);
        budgetID = getIntent().getStringExtra(ConcreteActivityNavigator.INTENT_EXTRA_BUDGET_ID);

        BudgetItem editedItem = getIntent().getParcelableExtra(ConcreteActivityNavigator.INTENT_EXTRA_EDITED_ITEM);
        if(editedItem!=null)
        {
            budgetItemAdapter.resetSelectedItem();
            budgetItemAdapter.updateEditItem(editedItem);
            saveChanges();
        }
        budgetPresenter.viewOnResume(budgetID);

    }

    @Override
    protected void onDestroy() {
        activityNavigator.detachContext();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        Log.e(TAG, "onBackPressed: " );
        saveChanges();
        activityNavigator.startHomeActivity();
    }
}
