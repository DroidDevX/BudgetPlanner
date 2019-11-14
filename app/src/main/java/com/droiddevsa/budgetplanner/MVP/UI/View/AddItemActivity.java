package com.droiddevsa.budgetplanner.MVP.UI.View;


import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.droiddevsa.budgetplanner.Async.BackgroundTaskManger;
import com.droiddevsa.budgetplanner.Async.RxJavaWrapper;
import com.droiddevsa.budgetplanner.MVP.Data.Models.BudgetItem;
import com.droiddevsa.budgetplanner.MVP.Presenters.AddItemPresenter;
import com.droiddevsa.budgetplanner.MVP.UI.ViewInterface.ActivityNavigator;
import com.droiddevsa.budgetplanner.R;
import com.droiddevsa.budgetplanner.MVP.Data.Repository.Repository;
import com.droiddevsa.budgetplanner.MVP.UI.ViewInterface.AddItemView;
import com.droiddevsa.budgetplanner.MVP.UI.ViewInterface.ConcreteActivityNavigator;
import com.droiddevsa.budgetplanner.Utilities.EditTextQuantityFilter;

public class AddItemActivity extends AppCompatActivity implements AddItemView {

    final static String TAG = "AddItemActivity.java";
    AddItemPresenter presenter;
    ConcreteActivityNavigator activityNavigator = new ConcreteActivityNavigator();
    String budgetID;
    BackgroundTaskManger backgroundTaskManger = new RxJavaWrapper();
    RadioButton incomeButton;
    RadioButton expenseButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_add_item);
        setTitle("Add new entry");

        incomeButton= findViewById(R.id.incomeRadio_AddForm);
        expenseButton=findViewById(R.id.expenseRadio_AddForm);

        activityNavigator.attachContext(this);
        setupAddFormSpinner();
        setupAddForm();

        budgetID =getIntent().getStringExtra(ConcreteActivityNavigator.INTENT_EXTRA_BUDGET_ID);

        Repository repository = new Repository();
        repository.setDatabaseManager(this,getResources().getString(R.string.DATABASE_NAME),getResources().getInteger(R.integer.DATBASE_VERSION));

        presenter = new AddItemPresenter(repository);
        presenter.attachView(this);
        presenter.setBackgroundTaskManger(backgroundTaskManger);
    }

    public View.OnClickListener createRadioButtonOnClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(view.getId()==R.id.incomeRadio_AddForm) {
                    incomeButton.setChecked(true);
                    expenseButton.setChecked(false);
                }
                else
                {
                    expenseButton.setChecked(true);
                    incomeButton.setChecked(false);

                }
            }
        };

    }



    public void setupAddForm(){
        EditText field;
        field = findViewById(R.id.amountInput);
        field.setText("0.00");



        field = findViewById(R.id.quantityInput);
        field.setText("1");
        field.setFilters(new InputFilter[]{new EditTextQuantityFilter(1,999999999)});

    }

    public void setupAddFormSpinner() {
        String [] categories=getResources().getStringArray(R.array.categories_spinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,categories);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = findViewById(R.id.categoryEdit_AddItemActivity);
        spinner.setAdapter(spinnerAdapter);
    }

    public void onAddItemClicked(View v)
    {
        //Get references to input fields
        TextView nameEditText = findViewById(R.id.nameInput);
        TextView brandEditText =  findViewById(R.id.brandInput);
        String brandtext = brandEditText.getText().toString().replace("'","''");
        Log.e(TAG,"brandtext -"+brandtext);
        TextView amountEditText =  findViewById(R.id.amountInput);
        TextView quantityEditText = findViewById(R.id.quantityInput);
        Spinner categorySpinner =  findViewById(R.id.categoryEdit_AddItemActivity);
        RadioButton incomeRadioButton =  findViewById(R.id.incomeRadio_AddForm);
        String cashFlow = incomeRadioButton.isChecked() ? "INCOME":"EXPENSE";



        String amountStr = amountEditText.getText().toString();
        double amount = amountStr.isEmpty() ? 0: Double.parseDouble(amountStr);

        String quantityStr = quantityEditText.getText().toString();
        int quantity = quantityStr.isEmpty() ? 0: Integer.parseInt(quantityStr);

        BudgetItem newItem =new BudgetItem(-1,-1,
                                            nameEditText.getText().toString(),
                                            categorySpinner.getSelectedItemPosition()+1,
                                            categorySpinner.getSelectedItem().toString(),
                                            brandtext,
                                            amount,
                                            quantity,cashFlow);

        presenter.viewAddItemClicked(budgetID,newItem);

    }

    @Override
    public void startCurrentBudgetActivity(String budgetID) {
        activityNavigator.startCurrentBudgetActivity(budgetID);
    }

    public void onCancelClicked(View v)
    {
        activityNavigator.startCurrentBudgetActivity(budgetID);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        return false;
    }

    @Override
    protected void onDestroy() {
        activityNavigator.detachContext();
        ((RxJavaWrapper)backgroundTaskManger).disposeObserverable();
        super.onDestroy();
    }

    @Override
    public void toastMessage(String message){

    }

    @Override
    public ActivityNavigator getActivityNavigator() {
        return activityNavigator;
    }
}
