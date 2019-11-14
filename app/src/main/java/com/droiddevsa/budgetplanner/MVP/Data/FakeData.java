package com.droiddevsa.budgetplanner.MVP.Data;

import com.droiddevsa.budgetplanner.MVP.Data.Models.Budget;
import com.droiddevsa.budgetplanner.MVP.Data.Models.BudgetItem;
import com.droiddevsa.budgetplanner.MVP.Data.Models.CategorySubtotal;
import com.droiddevsa.budgetplanner.Utilities.BudgetAppCurrency;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

public class FakeData {



    private static  Date generateDate(){
        int randomYear = randomIntBetween(2019,2019);
        int randomMonth = randomIntBetween(1,8);
        int randomDay = randomIntBetween(1,29);

        GregorianCalendar gc = new GregorianCalendar(randomYear,randomMonth,randomDay);
        return gc.getTime();
    }

    private static int  randomIntBetween(int startVal,int endVal){
        return startVal+ (int)Math.round(Math.random()*(endVal-startVal));
    }

    public static ArrayList<BudgetItem> createBudgetItems(){
        int listSize =10;
        ArrayList<BudgetItem> list = new ArrayList<>();
        for(int i=1;i<= listSize;i++)
            list.add(new BudgetItem(0,i,"Test name",0,
                                    "Test catname","Test brand",10*randomIntBetween(50,200),1,"INCOME"
                    ));
        for(int i=listSize;i<= listSize*2;i++)
            list.add(new BudgetItem(0,i,"Test name",0,
                    "Test catname","Test brand",10*randomIntBetween(1,100),1,"EXPENSE"
            ));

        return  list;
    }

    public static ArrayList<Budget> createBudgetList(){
        int budgetListSize =15;
        ArrayList<Budget> list = new ArrayList<>();

        BudgetAppCurrency currency = new BudgetAppCurrency(1,"US dollar","","","_");

        Date date;
        Budget budget;

        date = new GregorianCalendar(2019,1,28).getTime();
        budget =new Budget(1,date);
        budget.setDefaultCurrency(currency);
        budget.setBudgetItems(createBudgetItems());
        list.add(budget);

        date = new GregorianCalendar(2019,2,26).getTime();
        budget =new Budget(1,date);
        budget.setDefaultCurrency(currency);
        budget.setBudgetItems(createBudgetItems());
        list.add(budget);

        date = new GregorianCalendar(2019,3,29).getTime();
        budget =new Budget(1,date);
        budget.setDefaultCurrency(currency);
        budget.setBudgetItems(createBudgetItems());
        list.add(budget);

        date = new GregorianCalendar(2019,4,27).getTime();
        budget =new Budget(1,date);
        budget.setDefaultCurrency(currency);
        budget.setBudgetItems(createBudgetItems());
        list.add(budget);

        date = new GregorianCalendar(2019,5,30).getTime();
        budget =new Budget(1,date);
        budget.setDefaultCurrency(currency);
        budget.setBudgetItems(createBudgetItems());
        list.add(budget);


        date = new GregorianCalendar(2019,5,28).getTime();
        budget =new Budget(1,date);
        budget.setDefaultCurrency(currency);
        budget.setBudgetItems(createBudgetItems());
        list.add(budget);


        date = new GregorianCalendar(2019,6,28).getTime();
        budget =new Budget(1,date);
        budget.setDefaultCurrency(currency);
        budget.setBudgetItems(createBudgetItems());
        list.add(budget);

        date = new GregorianCalendar(2019,7,28).getTime();
        budget =new Budget(1,date);
        budget.setDefaultCurrency(currency);
        budget.setBudgetItems(createBudgetItems());
        list.add(budget);

        date = new GregorianCalendar(2019,8,28).getTime();
        budget =new Budget(1,date);
        budget.setDefaultCurrency(currency);
        budget.setBudgetItems(createBudgetItems());
        list.add(budget);

        date = new GregorianCalendar(2019,9,28).getTime();
        budget =new Budget(1,date);
        budget.setDefaultCurrency(currency);
        budget.setBudgetItems(createBudgetItems());
        list.add(budget);

        date = new GregorianCalendar(2019,10,28).getTime();
        budget =new Budget(1,date);
        budget.setDefaultCurrency(currency);
        budget.setBudgetItems(createBudgetItems());
        list.add(budget);

        return  list;
    }

    public static  ArrayList<CategorySubtotal> createIncomeSubtotals()
    {
        int listsize = 5;
        ArrayList<CategorySubtotal> incomes = new ArrayList<>();
        for (int i = 0; i < listsize; i++)
            incomes.add(new CategorySubtotal("Test Income Category", randomIntBetween(50,100)));
        return incomes;
    }


    public static  ArrayList<CategorySubtotal> createExpenseSubtotals()
    {
        int listsize = 5;
        ArrayList<CategorySubtotal> expense = new ArrayList<>();
        for (int i = 0; i < listsize; i++)
            expense.add(new CategorySubtotal("Test Expense Category", randomIntBetween(40,60)));
        return expense;
    }

}
