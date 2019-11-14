package com.droiddevsa.budgetplanner.Utilities;

import android.text.InputFilter;
import android.text.Spanned;

public class EditTextQuantityFilter implements InputFilter {

    private int min,max;

    public EditTextQuantityFilter(int min, int max){
        this.min = min;
        this.max = max;
    }



    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try{
            int input = Integer.parseInt(dest.toString() + source.toString());
            if(isInRange(min,max,input)){
                return null;
            }

        }catch (NumberFormatException nfe){

        }return "";
    }

    private boolean isInRange(int min, int max, int input){
        return max > min ? input >=min && input <=max:  input>=max && input <= min ;
    }

}
