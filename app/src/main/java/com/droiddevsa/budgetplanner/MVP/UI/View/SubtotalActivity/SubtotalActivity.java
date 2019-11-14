package com.droiddevsa.budgetplanner.MVP.UI.View.SubtotalActivity;


import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.droiddevsa.budgetplanner.MVP.Data.Models.CategorySubtotal;
import com.droiddevsa.budgetplanner.MVP.Data.Repository.Repository;
import com.droiddevsa.budgetplanner.MVP.UI.SubtotalViewPager.SubtotalViewPagerAdapter;
import com.droiddevsa.budgetplanner.MVP.UI.ViewInterface.ActivityNavigator;
import com.droiddevsa.budgetplanner.MVP.UI.ViewInterface.SubtotalPresenter;
import com.droiddevsa.budgetplanner.MVP.UI.ViewInterface.SubtotalView;
import com.droiddevsa.budgetplanner.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import static com.droiddevsa.budgetplanner.MVP.UI.ViewInterface.ConcreteActivityNavigator.INTENT_EXTRA_BUDGET_ID;


public class SubtotalActivity extends AppCompatActivity
        implements SubtotalView {



    //Other
    private String TAG ="SubtotalActivity";

    //Database information and Globals

    String BUDGET_ID;

    SubtotalViewPagerAdapter viewPagerAdapter;
    ViewPager viewPager;
    SubtotalPresenter presenter;
    ArrayList<CategorySubtotal> incomeSubtotals;
    ArrayList<CategorySubtotal> expenseSubtotals;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_overview);

        Log.d(TAG,"onCreate()");

        BUDGET_ID = getIntent().getStringExtra(INTENT_EXTRA_BUDGET_ID);
        viewPager = findViewById(R.id.viewpager);
        setupViewPagerTablayout(viewPager);

        viewPagerAdapter = new SubtotalViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);


        //Initialization of Presenter
        Repository repository =  new Repository();
        repository.setDatabaseManager(this,getResources().getString(R.string.DATABASE_NAME),getResources().getInteger(R.integer.DATBASE_VERSION));
        presenter = new SubtotalPresenter(repository);
        presenter.attachView(this);
        presenter.viewOnCreate(BUDGET_ID);
    }
    public void setupViewPagerTablayout(ViewPager viewPager){
        TabLayout tablayout= findViewById(R.id.subtotal_tablayout);
        tablayout.setupWithViewPager(viewPager);


    }

    /**
     *  Invoked by the presenter during which viewOnCreate() is invoked in this activity's onCreate() method
     */
    @Override
    public void onIncomeSubtotalsReady(ArrayList<CategorySubtotal> subtotals) {
        Log.d(TAG,"onIncomeSubtotalsReady(), income subtotals: ");
        if(subtotals!=null)
            for(CategorySubtotal subtotal: subtotals)
                Log.d(TAG,subtotal.toString());


        this.incomeSubtotals = subtotals;
    }

    /**
     *  Invoked by the presenter during which viewOnCreate() is invoked in this activity's onCreate() method
     */
    @Override
    public void onExpenseSubtotalsReady(ArrayList<CategorySubtotal> subtotals) {
        Log.d(TAG,"onExpenseSubtotalsReady");
        if(subtotals!=null)
            for(CategorySubtotal subtotal: subtotals)
                Log.d(TAG,subtotal.toString());

        this.expenseSubtotals = subtotals;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onResume()");
        viewPagerAdapter.setData(incomeSubtotals,expenseSubtotals);
        viewPagerAdapter.notifyDataSetChanged();
        Log.d(TAG,"onResume()-end");
    }

    /*  public void setupTimelineSwitches()
    {
        Switch balance = findViewById(R.id.toggleBalance);
        Switch income = findViewById(R.id.toggleIncome);
        Switch expense = findViewById(R.id.toggleExpense);

        balance.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        setupTimeLineChart();
                    }
                }

        );
        income.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        setupTimeLineChart();
                    }
                }

        );
        expense.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        setupTimeLineChart();
                    }
                }

        );

    }
*/

    /*
    public void setupTimeLineChart()
    {
        LineChart lineChart = findViewById(R.id.lineChart_timeline);

        //Event handling
       lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(false);


        //Create arraylist to hold entries for each line
        ArrayList<Budget> budgetList = budgetAppDb.getAllBudgets();
        ArrayList<Entry> balanceList = new ArrayList<>();
        ArrayList<Entry> incomeList = new ArrayList<>();
        ArrayList<Entry> expenseList= new ArrayList<>();

        //Switches
        Switch balanceSwitch = findViewById(R.id.toggleBalance);
        Switch incomeSwitch = findViewById(R.id.toggleIncome);
        Switch expenseSwitch = findViewById(R.id.toggleExpense);

        //Circle colors for lines
        ArrayList<Integer> balanceCircleColors = new ArrayList<>();
        ArrayList<Integer> incomeCircleColors = new ArrayList<>();
        ArrayList<Integer> expenseCircleColors = new ArrayList<>();
        int posInTimeLine = currentBudget.getBudgetID()-1;

        //Initialize entries for each line
        for(int i=0;i< budgetList.size();i++)
        {
            //Circle colors for lines
            if(i==posInTimeLine)
            {
                balanceCircleColors.add(Color.BLACK);
                incomeCircleColors.add(Color.BLACK);
                expenseCircleColors.add(Color.BLACK);
            }
            else
            {
                balanceCircleColors.add(Color.CYAN);
                incomeCircleColors.add(Color.GREEN);
                expenseCircleColors.add(Color.RED);
            }

            Budget budget = budgetList.get(i);
            if(balanceSwitch.isChecked())
                balanceList.add(new Entry(i,(float)budget.getBalance()));
            if (incomeSwitch.isChecked())
                incomeList.add(new Entry(i,(float)budget.getTotalIncome()));
            if(expenseSwitch.isChecked())
                expenseList.add(new Entry(i,(float)budget.getTotalExpense()*(-1)));//*
        }

        //Setup Data sets for lines
        LineDataSet balanceDataSet = new LineDataSet(balanceList,"Net-Balance");
        LineDataSet incomeDataSet = new LineDataSet(incomeList,"Income");
        LineDataSet expenseDataSet = new LineDataSet(expenseList,"Expense");

        //Set Hole color
        balanceDataSet.setCircleColors(balanceCircleColors);
        incomeDataSet.setCircleColors(incomeCircleColors);
        expenseDataSet.setCircleColors(expenseCircleColors);

        //Indicate where the user is currently
        int position = currentBudget.getBudgetID()-1;


        //Formating
        balanceDataSet.setColor(Color.CYAN);
        incomeDataSet.setColor(Color.GREEN);
        expenseDataSet.setColor(Color.RED);
        balanceDataSet.setLineWidth(3f);
        incomeDataSet.setLineWidth(3f);
        expenseDataSet.setLineWidth(3f);
        balanceDataSet.setValueTextSize(10f);
        incomeDataSet.setValueTextSize(10f);
        expenseDataSet.setValueTextSize(10f);
        balanceDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        incomeDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        expenseDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);


        //Line Chart formatting
        LineData data = new LineData(balanceDataSet,incomeDataSet,expenseDataSet);
        lineChart.setData(data);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getAxisLeft().setDrawLabels(false);//
        lineChart.getAxisRight().setDrawGridLines(false);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getXAxis().setDrawLabels(false);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.setDrawGridBackground(false);
        lineChart.fitScreen();
        lineChart.getDescription().setEnabled(false);
        lineChart.getData().setHighlightEnabled(false);//Remove yellow line

        int dp = 32;
        float scale = this.getResources().getDisplayMetrics().density;
        int px = (int)(dp*scale);
        lineChart.setVisibleXRangeMinimum(10);
        lineChart.setVisibleXRangeMaximum(120);
    }
*/
    public void switch_Timeline_Visibility(View v)
    {
        Log.i(TAG,"switchToggleTimeLineVissible()-start");
    }
    @Override
    protected void onStart() {
        super.onStart();

        Log.i(TAG,"onStart()");


    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public ActivityNavigator getActivityNavigator() {
        return null;
    }
}
