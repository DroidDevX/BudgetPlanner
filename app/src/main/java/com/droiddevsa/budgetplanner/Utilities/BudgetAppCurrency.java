package com.droiddevsa.budgetplanner.Utilities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Locale;

public class BudgetAppCurrency implements Parcelable {

    public static Creator<BudgetAppCurrency> CREATOR = new Creator<BudgetAppCurrency>() {
        @Override
        public BudgetAppCurrency createFromParcel(Parcel parcel) {
            return new BudgetAppCurrency(parcel);
        }

        @Override
        public BudgetAppCurrency[] newArray(int size) {
            return new BudgetAppCurrency[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(code);
        parcel.writeString(symbol);
    }

    protected BudgetAppCurrency(Parcel in){
        id = in.readInt();
        name = in.readString();
        code = in.readString();
        symbol = in.readString();
    }

    private int id;
    private String name;
    private String code;
    private String symbol;

    public BudgetAppCurrency(int id, String name,String code, String symbol,String position)
    {
        this.id =id;
        this.name = name;
        this.code = code;
        this.symbol = symbol;
    }

    public String getSymbol()
    {
        return this.symbol;
    }

    public String toString()
    {
        return (this.name)+" ("+ this.code +")";
    }

    public String formatAmount(double amount)
    {
        return  String.format(Locale.getDefault(),"%s %.2f", symbol,amount);
    }


    public int getCurrencyID()
    {
        return id;
    }



}
