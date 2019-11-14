package com.droiddevsa.budgetplanner.MVP.Data.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.droiddevsa.budgetplanner.Utilities.BudgetAppCurrency;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Budget implements Parcelable {
    private final String TAG ="BudgetClassFilter";

    public static final Creator<Budget> CREATOR = new Creator<Budget>(){
        @Override
        public Budget createFromParcel(Parcel parcel) {
            return new Budget(parcel);
        }

        @Override
        public Budget[] newArray(int size) {
            return new Budget[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(budgetID);
        parcel.writeLong(createdDate.getTime());
        parcel.writeDouble(totalIncome);
        parcel.writeDouble(totalExpense);
        parcel.writeDouble(balance);
        parcel.writeParcelable(defaultCurrency,flags);

    }

    protected Budget(Parcel in){
        budgetID = in.readInt();
        createdDate = new Date(in.readLong());
        totalIncome = in.readDouble();
        totalExpense = in.readDouble();
        balance = in.readDouble();
        defaultCurrency = in.readParcelable(BudgetAppCurrency.class.getClassLoader());
        //Arraylist of BudgetItems not parsed yet
    }

    //Members
    private int budgetID;
    private Date createdDate;
    private double totalIncome;
    private double totalExpense;
    private double balance;
    private BudgetAppCurrency defaultCurrency;


    //List of budget items
    private ArrayList<BudgetItem> budgetItems;


    public Budget(int budgetID, Date createdDate)
    {

        this.budgetID = budgetID;
        this.createdDate = createdDate;
        this.budgetItems = new ArrayList<>();
        this.totalIncome =0;
        this.totalExpense=0;
        this.balance = 0;
    }

    public int getBudgetID() {
        return budgetID;
    }


    public String getFormattedDateStr(String format){
        SimpleDateFormat dateFormatter = new SimpleDateFormat(format, Locale.ENGLISH);
        return dateFormatter.format(createdDate);
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public ArrayList<BudgetItem> getBudgetItemList()
    {
        return budgetItems;
    }

    public double getTotalIncome() {
        return  this.totalIncome;
    }

    public double getTotalExpense() {
        return  this.totalExpense;
    }

    public double getBalance()
    {
        return this.balance;
    }


    public void setDefaultCurrency(BudgetAppCurrency defaultCurrency) {
        this.defaultCurrency = defaultCurrency;
    }

    @Override
    public String toString() {
        String finalStr = "Budget{" +
                "budgetID=" + budgetID +
                ", createdDate=" + createdDate.toString() +
                ",  m_income=" + totalIncome +
                ",  m_expense" + totalExpense +
                ", balance=" + balance+
                '}'+"\nBudget Items\n---------------------\n{";

        //Add budget items here
        StringBuilder strBuilder= new StringBuilder();
        if(budgetItems==null)
            strBuilder.append("{}");
        else
          for(int i = 0; i < budgetItems.size(); i++){
                strBuilder.append(budgetItems.get(i).toString()).append(", ");
            }
        finalStr += strBuilder.append("}").toString();
        return finalStr;
    }

    public void setBudgetItems(ArrayList<BudgetItem> budgetItems){
        this.budgetItems = budgetItems;
        calculateBalance();
    }

    private void calculateBalance(){
        totalIncome=0;
        totalExpense=0;
        balance=0;

        if(budgetItems==null)
            return;

        for(BudgetItem item:budgetItems){
            if(item.getCashFlow().equals("INCOME"))
                totalIncome+= (item.getQuantity()*item.getAmount());
            else if(item.getCashFlow().equals("EXPENSE"))
                totalExpense+= (item.getQuantity()*item.getAmount());
        }
        balance = totalIncome-totalExpense;
    }


}
