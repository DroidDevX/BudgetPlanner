package com.droiddevsa.budgetplanner.MVP.UI.HomeViewPager;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.droiddevsa.budgetplanner.AccumulatedBalancePage;
import com.droiddevsa.budgetplanner.MVP.Data.Models.Budget;
import com.droiddevsa.budgetplanner.MVP.UI.View.HomeActivity.AllBudgetFragmentPage;
import com.droiddevsa.budgetplanner.MVP.UI.View.HomeActivity.BudgetHistogramFragmentPage;

import java.util.ArrayList;

public class HomeViewPagerAdapter extends FragmentStatePagerAdapter {


    private static final String TAG = "HomeViewPagerAdapter";

    ArrayList<Fragment> pagelist;
    ArrayList<Budget> budgetlist;

    public HomeViewPagerAdapter(@NonNull FragmentManager fm){
        super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        initPages();
    }

    private void initPages(){
        if(pagelist!=null)
            pagelist.clear();
        pagelist = new ArrayList<>();
        pagelist.add(AllBudgetFragmentPage.getInstance(budgetlist));
        pagelist.add(BudgetHistogramFragmentPage.getInstance(budgetlist));
        pagelist.add(AccumulatedBalancePage.getInstance(budgetlist));
    }

    public void setBudgetlist(ArrayList<Budget> budgetlist){
        Log.d(TAG,"setBudgetList(): ");
        this.budgetlist = budgetlist;
        initPages();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Log.d(TAG, "getItem: ");
        return pagelist.get(position);
    }


    @Override
    public int getCount() {
        return 3;

    }



}
