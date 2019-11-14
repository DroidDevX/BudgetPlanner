package com.droiddevsa.budgetplanner.MVP.UI.View.HomeActivity;

import android.text.TextUtils;

import java.util.ArrayList;

/*
* This is a wrapper model class that stores a list of subtotals
* and their corresponding category names.
*
* This data is linked to a particulat Budget with m_budgetID as the ID
* */
public class SubtotalByCategory
{
    String[][] m_data;

    public SubtotalByCategory(String [][]data)
    {
        this.m_data = data;
    }

    private  int numberOfCategories()
    {
        return m_data.length;
    }

    private String getCategoryName(int index)
    {
        return m_data[index][0];
    }

    private String getSubtotal(int index)
    {
        return m_data[index][1];
    }

    public ArrayList<String> getListOfCategories()
    {
        ArrayList<String> categoryNames = new ArrayList<String>();
        for(int i=0;i< this.numberOfCategories();i++)
            categoryNames.add(this.getCategoryName(i));
        return categoryNames;
    }

    public  ArrayList<String> getListOfSubtotals()
    {
        ArrayList<String> subtotals = new ArrayList<>();
        for(int i=0;i< this.numberOfCategories();i++)
            subtotals.add(this.getSubtotal(i));

        return  subtotals;
    }

    public String toString()
    {
        String categories = "List of Categories: "+ TextUtils.join(",",getListOfCategories());
        String subtotals  = "Corresponding subtotals: "+ TextUtils.join(",",getListOfSubtotals());
        return (categories+"\n"+subtotals);
    }



}
