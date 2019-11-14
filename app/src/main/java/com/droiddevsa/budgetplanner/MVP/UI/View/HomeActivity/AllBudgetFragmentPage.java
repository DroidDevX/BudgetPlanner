package com.droiddevsa.budgetplanner.MVP.UI.View.HomeActivity;

import android.content.Context;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.droiddevsa.budgetplanner.MVP.Data.Models.Budget;
import com.droiddevsa.budgetplanner.MVP.UI.BudgetRecyclerView.BudgetAdapter;
import com.droiddevsa.budgetplanner.R;

import java.util.ArrayList;

public class AllBudgetFragmentPage extends Fragment implements BudgetAdapter.RecyclerViewEventListener{

    private static final String TAG = "AllBudgetFragmentPage";
    private static String BUDGETLIST_DATA="budgetlistData";

    View rootView;
    private EventListener homeActivity;
    private RecyclerView budgetRecyclerView;
    private BudgetAdapter budgetAdapter;


    public interface EventListener{
        void onDeleteBtnClicked(String budgetID);
        void onBudgetSelected(String budgetID);

    }


    public static AllBudgetFragmentPage getInstance(ArrayList<Budget> budgetlist){
        AllBudgetFragmentPage fragment = new AllBudgetFragmentPage();
        Bundle data = new Bundle();
        data.putParcelableArrayList(BUDGETLIST_DATA,budgetlist);
        fragment.setArguments(data);


        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG,"AllBudgetFragmentPage, onCreateView()");
        rootView = inflater.inflate(R.layout.fragment_home_all_budget_page,container,false);




        budgetRecyclerView = rootView.findViewById(R.id.budget_recyclerView);
        budgetAdapter= new BudgetAdapter(this);
        budgetRecyclerView.setAdapter(budgetAdapter);
        budgetRecyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));

        Bundle initData = getArguments();

        if(initData!=null)
            if(initData.getParcelableArrayList(BUDGETLIST_DATA)!=null);
            {
                    ArrayList<Budget> budgetlist= initData.getParcelableArrayList(BUDGETLIST_DATA);

                    budgetAdapter.setBudgetList(budgetlist);
                    budgetAdapter.notifyDataSetChanged();
            }



        return rootView;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onAttach(@NonNull Context homeActivity) {
        super.onAttach(homeActivity);
        this.homeActivity = (EventListener) homeActivity;
    }

    @Override
    public void onRecyclerViewDeleteBtnClicked(String budgetID) {
        homeActivity.onDeleteBtnClicked(budgetID);
    }

    @Override
    public void onRecyclerViewBudgetSelected(String budgetID) {
        homeActivity.onBudgetSelected(budgetID);
    }


    @Override
    public void onResume() {
        super.onResume();
        budgetAdapter.notifyDataSetChanged();

    }



}
