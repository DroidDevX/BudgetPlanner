package com.droiddevsa.budgetplanner.MVP.UI.View;


import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.droiddevsa.budgetplanner.MVP.Data.Models.BudgetItem;
import com.droiddevsa.budgetplanner.MVP.Presenters.EditItemPresenter;
import com.droiddevsa.budgetplanner.MVP.UI.ViewInterface.ActivityNavigator;
import com.droiddevsa.budgetplanner.MVP.UI.ViewInterface.ConcreteActivityNavigator;
import com.droiddevsa.budgetplanner.R;
import com.droiddevsa.budgetplanner.MVP.Data.Repository.Repository;
import com.droiddevsa.budgetplanner.MVP.UI.ViewInterface.EditItemView;
import com.droiddevsa.budgetplanner.Utilities.EditTextQuantityFilter;

import java.util.ArrayList;
import java.util.Locale;

import static com.droiddevsa.budgetplanner.MVP.UI.ViewInterface.ConcreteActivityNavigator.INTENT_EXTRA_BUDGET_ID;

public class EditItemActivity extends AppCompatActivity implements EditItemView {

    private final String TAG = "EditItemActivity";

    String budgetID;
    EditItemPresenter presenter;
    ConcreteActivityNavigator activityNavigator = new ConcreteActivityNavigator();
    BudgetItem budgetItem;

    /*--------------------------------------------------------------------------------*/
    //Initialize user interface
    /*--------------------------------------------------------------------------------*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_edit_item);
        activityNavigator.attachContext(this);
        setTitle("Edit");
        Log.d(TAG, "onCreate: ");


        setTitle("Edit budget entries");
        overridePendingTransition(R.layout.anim_fadeinright, R.layout.anim_fadeoutright);


        //Extract intent string
        Intent i = getIntent();

        budgetID = i.getStringExtra(INTENT_EXTRA_BUDGET_ID);


        budgetItem = i.getExtras().getParcelable(ConcreteActivityNavigator.INTENT_EXTRA_BUDGETITEM);

        //Initialize Repository
        Repository repository =  new Repository();
        repository.setDatabaseManager(this,getResources().getString(R.string.DATABASE_NAME),getResources().getInteger(R.integer.DATBASE_VERSION));

        presenter = new EditItemPresenter(repository);
        presenter.attachView(this);
        presenter.initSpinner();

        updateEditForm();

    }
    @Override
    public void populateSpinner(ArrayList<String> categories) {
        String[] categoriesArray = new String[categories.size()];
        categoriesArray = categories.toArray(categoriesArray);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categoriesArray);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner categoryEdit = findViewById(R.id.categoryEdit);
        categoryEdit.setAdapter(spinnerAdapter);
    }

    public void updateEditForm() {
        /*Initialize the edit form with the budget item data. The budget item is located at 'budgetItemIndex' param*/
        Log.d(TAG, "updateEditForm()");
        /*---------------------------------------------------------*/
        //References to ui widgets
        /*---------------------------------------------------------*/
        TextView itemNameEdit = findViewById(R.id.nameEdit);
        Spinner categoryEdit = findViewById(R.id.categoryEdit);
        int categorySpinnerPosition = budgetItem.getCategoryID() - 1;//Spinner item starting index starts at 0 but in Database category ID start at 1.
        TextView brandEdit = findViewById(R.id.brandEdit);
        TextView amountEdit = findViewById(R.id.amountEdit);

        TextView quantityEdit = findViewById(R.id.quantityEdit);
        quantityEdit.setFilters(new InputFilter[]{new EditTextQuantityFilter(1,999999999)});
        RadioButton incomeRadButton = findViewById(R.id.incomeRadio_EditForm);
        RadioButton expenseRadButton = findViewById(R.id.expenseRadio_EditForm);

        /*---------------------------------------------------*/
        //Set Data
        /*---------------------------------------------------------*/
        itemNameEdit.setText(budgetItem.get_itemName());
        categoryEdit.setSelection(categorySpinnerPosition);//**
        brandEdit.setText(String.valueOf(budgetItem.getBrand()));

        String inputStr = String.valueOf(budgetItem.getQuantity());
        if (inputStr.isEmpty())
            inputStr ="1";

        quantityEdit.setText(inputStr);


        //Convert amount to unsigned
        double amount = budgetItem.getUnsignedAmount();

        amountEdit.setText(String.format(Locale.ENGLISH,"%.2f",amount));

        //Setup radio buttons
        if (budgetItem.isIncome()) {
            incomeRadButton.setChecked(true);

        } else {
            expenseRadButton.setChecked(true);
        }


    }

    /*--------------------------------------------------------------------------------*/
     //Click events
    /*--------------------------------------------------------------------------------*/

    public BudgetItem getBudgetFormData() {
        Log.i(TAG,"viewOnSaveCurrentItemStateClicked()");
        TextView itemNameEdit = findViewById(R.id.nameEdit);
        Spinner categoryEdit = findViewById(R.id.categoryEdit);
        TextView brandEdit = findViewById(R.id.brandEdit);
        String brandtext = brandEdit.getText().toString().replace("'","''");
        Log.e(TAG,"brandtext -"+brandtext);
        TextView amountEdit = findViewById(R.id.amountEdit);
        TextView quantityEdit = findViewById(R.id.quantityEdit);
        RadioButton incomeRadButton = findViewById(R.id.incomeRadio_EditForm);


        String cashFlow;
        if(incomeRadButton.isChecked())
            cashFlow="INCOME";
        else
            cashFlow="EXPENSE";

        double amount;
        String amountStr =amountEdit.getText().toString();
        amount = amountStr.isEmpty() ? 0.00: Double.parseDouble(amountStr);

        int quantity;
        String quantityStr = quantityEdit.getText().toString();
        quantity = quantityStr.isEmpty() ? 0: Integer.parseInt(quantityStr);

        budgetItem.setData(itemNameEdit.getText().toString(),
                categoryEdit.getSelectedItemPosition()+1,
                categoryEdit.getSelectedItem().toString(),
                brandtext,
                amount,
                quantity,
                cashFlow);

        return  budgetItem;
    }

    public void onCancelEditClicked(View v) {
        activityNavigator.startCurrentBudgetActivity(budgetID);
    }

    public void onFinishEditClicked(View v) {
        BudgetItem itemFormData =getBudgetFormData();
        presenter.viewOnFinishEditClicked(itemFormData);
    }

    @Override
    public void updateItemComplete(BudgetItem updatedItemData) {
        activityNavigator.startCurrentBudgetActivity(budgetID,updatedItemData);
    }

    @Override
    public void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }


    @Override
    protected void onDestroy() {
        activityNavigator.detachContext();
        super.onDestroy();
    }

    @Override
    public ActivityNavigator getActivityNavigator() {
        return activityNavigator;
    }
}