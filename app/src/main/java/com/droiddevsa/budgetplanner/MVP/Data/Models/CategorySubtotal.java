package com.droiddevsa.budgetplanner.MVP.Data.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class CategorySubtotal implements Parcelable {
    public static Creator<CategorySubtotal> CREATOR = new Creator<CategorySubtotal>() {
        @Override
        public CategorySubtotal createFromParcel(Parcel parcel) {
            return new CategorySubtotal(parcel);
        }

        @Override
        public CategorySubtotal[] newArray(int size) {
            return new CategorySubtotal[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(categoryName);
        parcel.writeDouble(subtotal);
    }

    protected CategorySubtotal(Parcel in){
        categoryName = in.readString();
        subtotal = in.readDouble();
    }

    String categoryName;
    double subtotal;

    public CategorySubtotal(String categoryName,double subtotal){
        this.categoryName= categoryName;
        this.subtotal = subtotal;
    }

    @Override
    public String toString() {
        return "CategorySubtotal{" +
                "categoryName='" + categoryName + '\'' +
                ", subtotal=" + subtotal +
                '}';
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
}
