package com.droiddevsa.budgetplanner.MVP.UI.SubtotalViewPager;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.droiddevsa.budgetplanner.MVP.Data.Models.CategorySubtotal;
import com.droiddevsa.budgetplanner.MVP.UI.View.SubtotalActivity.ExpenseFragment;
import com.droiddevsa.budgetplanner.MVP.UI.View.SubtotalActivity.IncomeFragment;

import java.util.ArrayList;

public class SubtotalViewPagerAdapter extends FragmentPagerAdapter {
    private static final String TAG = "SubtotalViewPagerAdapte";
    ArrayList<CategorySubtotal> incomeSubtotals;
    ArrayList<CategorySubtotal> expenseSubtotals;

    public SubtotalViewPagerAdapter(FragmentManager fragmentManager){
        super(fragmentManager,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

    }

    public void setData(ArrayList<CategorySubtotal> incomeSubtotals,ArrayList<CategorySubtotal>expenseSubtotals){
        Log.d(TAG,"setData()");
        this.incomeSubtotals = incomeSubtotals;
        this.expenseSubtotals = expenseSubtotals;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Log.d(TAG,"getItem()");
        if(position==0)
            return IncomeFragment.getInstance(incomeSubtotals);
        else
            return ExpenseFragment.getInstance(expenseSubtotals);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if(position==0)
            return "Income";
        else
            return "Expense";
    }

    @Override
    public int getCount() {
        return 2;
    }
}
