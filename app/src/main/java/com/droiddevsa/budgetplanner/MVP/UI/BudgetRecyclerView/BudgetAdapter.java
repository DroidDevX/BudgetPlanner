package com.droiddevsa.budgetplanner.MVP.UI.BudgetRecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.droiddevsa.budgetplanner.MVP.Data.Models.Budget;
import com.droiddevsa.budgetplanner.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class BudgetAdapter extends RecyclerView.Adapter<BudgetAdapter.ViewHolder> {

    ArrayList<Budget> budgetList;
    RecyclerViewEventListener eventListener;

    public interface RecyclerViewEventListener{
        void onRecyclerViewDeleteBtnClicked(String budgetID);
        void onRecyclerViewBudgetSelected(String budgetID);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        View rootView;
        TextView dateCreatedTV;
        TextView totalBalanceTV;
        TextView totalIncomeTV;
        TextView totalExpenseTV;
        AppCompatImageView editBudgetButton;
        AppCompatImageView deleteBudgetButton;

        public ViewHolder(View rootview){
            super(rootview);
            this.rootView = rootview;
            dateCreatedTV = rootview.findViewById(R.id.dateCreatedTV);
            totalBalanceTV = rootview.findViewById(R.id.totalBalanceTV);
            totalIncomeTV = rootview.findViewById(R.id.totalIncomeTV);
            totalExpenseTV = rootview.findViewById(R.id.totalExpenseTV);
            deleteBudgetButton = rootview.findViewById(R.id.deleteBudgetBtn);
            editBudgetButton = rootview.findViewById(R.id.editBudgetBtn);
        }

    }

    public BudgetAdapter(RecyclerViewEventListener eventListener){
        this.eventListener = eventListener;
    }

    public void setBudgetList(ArrayList<Budget> budgetlist){
        this.budgetList = budgetlist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup reyclerview, int viewType) {
        View rootView=LayoutInflater.from(reyclerview.getContext()).inflate(R.layout.recyclerview_budget_description_item,reyclerview,false);
        ViewHolder viewHolder = new ViewHolder(rootView);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Budget budget = budgetList.get(position);
        SimpleDateFormat formater = new SimpleDateFormat("dd-MM-yyyy");
        holder.dateCreatedTV.setText(formater.format(budget.getCreatedDate()));
        holder.totalBalanceTV.setText(String.format(Locale.ENGLISH,"%.2f",budget.getBalance()));
        holder.totalIncomeTV.setText(String.format(Locale.ENGLISH,"%.2f",budget.getTotalIncome()));
        holder.totalExpenseTV.setText(String.format(Locale.ENGLISH,"%.2f",budget.getTotalExpense()));


        holder.deleteBudgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                eventListener.onRecyclerViewDeleteBtnClicked(String.valueOf(budget.getBudgetID()));
            }
        });

        holder.editBudgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              eventListener.onRecyclerViewBudgetSelected(String.valueOf(budget.getBudgetID()));
            }
        });

    }

    @Override
    public int getItemCount() {
        if(budgetList==null)
            return 0;
        else
            return budgetList.size();
    }
}
