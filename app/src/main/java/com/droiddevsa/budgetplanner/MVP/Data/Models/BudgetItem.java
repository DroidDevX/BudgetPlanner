package com.droiddevsa.budgetplanner.MVP.Data.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class BudgetItem implements Parcelable {

    public static final Creator<BudgetItem> CREATOR = new Creator<BudgetItem>() {
        @Override
        public BudgetItem createFromParcel(Parcel parcel) {
            return new BudgetItem(parcel);
        }

        @Override
        public BudgetItem[] newArray(int size) {
            return new BudgetItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(m_itemEntryID);
        parcel.writeInt(m_itemID);
        parcel.writeString(m_itemName);
        parcel.writeInt(m_categoryID);
        parcel.writeString(m_categoryName);
        parcel.writeString(m_brand);
        parcel.writeDouble(m_Amount);
        parcel.writeInt(m_quantity);
        parcel.writeString(m_cashFlow);
    }

    protected BudgetItem(Parcel in){
        m_itemEntryID = in.readInt();
        m_itemID = in.readInt();
        m_itemName = in.readString();
        m_categoryID = in.readInt();
        m_categoryName = in.readString();
        m_brand = in.readString();
        m_Amount = in.readDouble();
        m_quantity = in.readInt();
        m_cashFlow = in.readString();
    }

    private int m_itemEntryID;
    private int m_itemID;
    private String m_itemName;
    private int m_categoryID;
    private String m_categoryName;
    private String m_brand;
    private double m_Amount;
    private int m_quantity;
    private String m_cashFlow;

    private final String TAG ="BudgetItemClassFilter";

    public void setData(String itemName,int categoryID,String categoryName,String brand,double amount,int m_quantity,String m_cashFlow)
    {
        this.m_itemName = itemName;
        this.m_categoryID = categoryID;
        this.m_categoryName = categoryName;
        this.m_brand = brand;
        this.m_Amount = amount;
        this.m_quantity = m_quantity;
        this.m_cashFlow = m_cashFlow;
    }

    public void setItemEntryID(int itemEntryID) {
        this.m_itemEntryID = itemEntryID;
    }

    public void setItemID(int newItemID) {
        this.m_itemID = newItemID;
    }

    public String getCategoryName()
    {
        return m_categoryName;
    }

    public boolean isExpense() {
        return m_cashFlow.equals("EXPENSE");
    }

    public boolean isIncome()
    {
        return m_cashFlow.equals("INCOME");
    }

    public int getItemID() {
        return m_itemID;
    }

    public String get_itemName() {
        return m_itemName;
    }

    public int getCategoryID() {
        return m_categoryID;
    }

    public String getBrand() {
        return m_brand;
    }

    public double getAmount() {
        return m_Amount;
    }

    public int getQuantity() {
        return m_quantity;
    }

    public String getCashFlow()
    {
        return this.m_cashFlow;
    }


    public int getItemEntryID()
    {
        return this.m_itemEntryID;
    }


    public BudgetItem(int itemEntryID,int itemID, String itemName, int categoryID, String categoryName, String brand, double amount, int quantity, String cashFlow) {
        this.m_itemEntryID = itemEntryID;
        this.m_itemID = itemID;
        this.m_itemName = itemName;
        this.m_categoryID = categoryID;
        this.m_categoryName= categoryName;
        this.m_brand = brand;
        this.m_Amount = amount;
        this.m_quantity = quantity;
        this.m_cashFlow = cashFlow;
    }

    public void overideData(BudgetItem newItemData){
        m_itemEntryID = newItemData.getItemEntryID();
        m_itemID = newItemData.getItemID();
        m_itemName = newItemData.get_itemName();
        m_categoryID = newItemData.getCategoryID();
        m_categoryName = newItemData.getCategoryName();
        m_brand = newItemData.getBrand();
        m_Amount = newItemData.getAmount();
        m_quantity = newItemData.m_quantity;
        m_cashFlow = newItemData.m_cashFlow;
    }



    @Override
    public String toString() {
        return "BudgetItem{" +
                "m_itemEntryID= "+ m_itemEntryID +
                ", m_itemID=" + m_itemID +
                ", m_itemName='" + m_itemName + '\'' +
                ", m_categoryID=" + m_categoryID +
                ", m_categoryName='" + m_categoryName + '\'' +
                ", m_brand='" + m_brand + '\'' +
                ", m_amount=" + m_Amount +
                ", m_quantity= " + m_quantity +
                ", m_cashFlow= " + m_cashFlow+
                '}';
    }

    public double getUnsignedAmount()
    {
        if(this.m_Amount <0)
            return (m_Amount*(-1));
        else
            return  m_Amount;
    }


}
