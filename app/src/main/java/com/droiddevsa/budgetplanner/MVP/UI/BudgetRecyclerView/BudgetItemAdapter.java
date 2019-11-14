package com.droiddevsa.budgetplanner.MVP.UI.BudgetRecyclerView;

import android.graphics.Color;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.droiddevsa.budgetplanner.MVP.Data.Models.BudgetItem;
import com.droiddevsa.budgetplanner.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class BudgetItemAdapter extends RecyclerView.Adapter<BudgetItemAdapter.ViewHolder> {
    private static final String TAG = "BudgetItemAdapter";

    private static int fillerSize=15;

    //Dataset
    ArrayList<BudgetItem> budgetItemList;

    //Control variables
    private SparseBooleanArray itemIsSelected;
    private int selectedItemColor = Color.BLUE;
    private int selectedItemTextColor = Color.parseColor("#FAFAFA");//White default;
    private int unselectedItemColor = Color.parseColor("#FAFAFA");//White default
    private int unselectedItemTextColor = Color.GRAY;


    //Item column Identification
    SparseBooleanArray itemColumnIsVisible;
    final public static int  COLUMN_ITEM_NAME =0;
    final public static int COLUMN_CATEGORY_NAME=1;
    final public static int COLUMN_BRAND =2;
    final public static int COLUMN_AMOUNT=3;
    final public static int COLUMN_QUANTITY=4;
    final public static int COLUMN_CASHFLOW=5;


    class ViewHolder extends  RecyclerView.ViewHolder
    {
        View rootView;
        TextView itemNameTV;
        TextView categoryNameTV;
        TextView brandTV;
        TextView amountTV;
        TextView quantityTV;
        TextView cashFlowTV;

        public ViewHolder (View rootView){
            super(rootView);

            this.rootView = rootView;
            itemNameTV= rootView.findViewById(R.id.itemNameTV);
            categoryNameTV = rootView.findViewById(R.id.categoryNameTV);
            brandTV = rootView.findViewById(R.id.brandTV);
            amountTV = rootView.findViewById(R.id.amountTV);
            quantityTV = rootView.findViewById(R.id.quantityTV);
            cashFlowTV = rootView.findViewById(R.id.cashFlowTV);

        }

    }

    public BudgetItemAdapter()
    {


        this.budgetItemList = new ArrayList<>();
        this.itemColumnIsVisible = new SparseBooleanArray();
        this.itemIsSelected = new SparseBooleanArray();

        //There is 6 columns in a recyclerview item
        for(int colPos=0;colPos< 6;colPos++)
            itemColumnIsVisible.put(colPos,true);

        for(int itemPos = 0; itemPos< budgetItemList.size(); itemPos++) {
            itemIsSelected.put(itemPos, false);
        }


    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recylcerview_budget_item,parent,false);


        return new ViewHolder(rootView);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        displayItemVisibleColumns(holder);

        if(budgetItemList==null ||position>=budgetItemList.size()) { //Extra Rows out of scope
            bindNoDataToViews(holder);
            return;
        }
        else //Populated recycler view item from budgetItemList[0] ->(n-1)
            bindDataToViews(holder,position);



        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"onClick()");


                //If item clicked was not selected before, otherwise deselect it
                if(!itemIsSelected.get(position))
                    itemIsSelected.put(position,true);
                else
                    itemIsSelected.put(position,false);

                //Unselect all items that were not clicked
                for(int itemPos = 0; itemPos < budgetItemList.size(); itemPos++) {
                    if(itemPos!=position)
                        itemIsSelected.put(itemPos, false);
                }

                notifyDataSetChanged();

            }
        });


        if(itemIsSelected.get(position)) {
            highlightSelectedItem(holder,position,true);
        }
        else {
            highlightSelectedItem(holder,position,false);
        }


    }

    public void bindDataToViews(ViewHolder viewHolder, int position){
        BudgetItem item = budgetItemList.get(position);
        viewHolder.itemNameTV.setText(item.get_itemName());
        viewHolder.categoryNameTV.setText(item.getCategoryName());
        viewHolder.brandTV.setText(item.getBrand());
        viewHolder.amountTV.setText(String.format(Locale.ENGLISH,"%.2f",item.getAmount()));
        viewHolder.quantityTV.setText(String.format(Locale.ENGLISH,"%d",item.getQuantity()));
        viewHolder.cashFlowTV.setText(item.getCashFlow());
    }

    public void bindNoDataToViews(ViewHolder viewHolder){
        viewHolder.itemNameTV.setText("");
        viewHolder.categoryNameTV.setText("");
        viewHolder.brandTV.setText("");
        viewHolder.amountTV.setText("");
        viewHolder.quantityTV.setText("");
        viewHolder.cashFlowTV.setText("");
    }

    public void highlightSelectedItem(ViewHolder viewHolder,int position,boolean highlightItem){
        Log.d(TAG,"highlightSelectedItem");
        LinearLayout itemViewGroup = (LinearLayout) viewHolder.rootView;
        int itemBackgroundColor;
        int itemChildTextColor;

        if(highlightItem) {
            itemBackgroundColor = selectedItemColor;
            itemChildTextColor  = selectedItemTextColor;
        }
        else{
            itemBackgroundColor = unselectedItemColor;
            itemChildTextColor  = unselectedItemTextColor;
        }

        viewHolder.rootView.setBackgroundColor(itemBackgroundColor);
        for(int childItemPos=0;childItemPos<itemViewGroup.getChildCount();childItemPos++)
                ((TextView)itemViewGroup.getChildAt(childItemPos)).setTextColor(itemChildTextColor);


    }


    private void displayItemVisibleColumns(ViewHolder viewHolder){
        for(int columnPos=0;columnPos < itemColumnIsVisible.size();columnPos++){

            boolean childIsVisible = itemColumnIsVisible.get(columnPos);
            int visibility= childIsVisible ? View.VISIBLE:View.GONE;
            TextView columnView=null;
            switch (columnPos){

                case COLUMN_ITEM_NAME:columnView=viewHolder.itemNameTV;break;

                case COLUMN_CATEGORY_NAME:columnView=viewHolder.categoryNameTV;break;

                case COLUMN_BRAND:columnView=viewHolder.brandTV;break;

                case COLUMN_AMOUNT:columnView=viewHolder.amountTV;break;

                case COLUMN_QUANTITY:columnView=viewHolder.quantityTV;break;

                case COLUMN_CASHFLOW:columnView=viewHolder.cashFlowTV;break;
            }
            if(columnView!=null)
                columnView.setVisibility(visibility);
        }

    }

    public  void swapBudgetItems(int dragPosition, int targetPosition){
        if(dragPosition >= getBudgetListSize()||targetPosition>=getBudgetListSize())
            return;

        BudgetItem dragedItem = budgetItemList.get(dragPosition);
        BudgetItem targetItem = budgetItemList.get(targetPosition);

        //Swap item entry_ID
        int temp = dragedItem.getItemEntryID();
        dragedItem.setItemEntryID(targetItem.getItemEntryID());
        targetItem.setItemEntryID(temp);

        //Swap recyclerview position
        Collections.swap(budgetItemList,dragPosition,targetPosition);

        notifyItemMoved(dragPosition,targetPosition);
    }


    @Override
    public int getItemCount()
    {
        if(budgetItemList==null)
            return fillerSize;
        else
            return budgetItemList.size()+fillerSize;
    }

    public void updateBudgetItemList(ArrayList<BudgetItem> budgetList){
        this.budgetItemList =budgetList;
    }

    public void setVisibleColumns(SparseBooleanArray columnsToShow){
        this.itemColumnIsVisible = columnsToShow;
    }

    public BudgetItem getSelectedItem(){
        if(budgetItemList==null)
            return null;

        int itemPos=0;
        for(BudgetItem item: budgetItemList){
            if(itemIsSelected.get(itemPos))
                return budgetItemList.get(itemPos);

            itemPos++;
        }
        return  null;//No selected items found
    }

    public int getBudgetListSize(){
        if(budgetItemList==null)
            return 0;
        else
            return budgetItemList.size();
    }

    public ArrayList<BudgetItem> getBudgetItemList(){
        return this.budgetItemList;
    }


    public void updateEditItem(BudgetItem editedItem){
        Log.d(TAG,"updateEditItem()");
        BudgetItem item=null;
        for (int itemPos = 0; itemPos< budgetItemList.size(); itemPos++)
        {
            if(editedItem.getItemEntryID()== budgetItemList.get(itemPos).getItemEntryID())
            {
                item = budgetItemList.get(itemPos);
                item.overideData(editedItem);
            }
        }
    }

    public void resetSelectedItem(){
        int itemPos=0;
        if(budgetItemList!=null)
            while(itemPos < budgetItemList.size()) {
                itemIsSelected.put(itemPos, false);
                itemPos++;
        }
    }

}
