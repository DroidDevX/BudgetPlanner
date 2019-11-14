package com.droiddevsa.budgetplanner.Utilities;

import java.util.HashMap;

public class ViewClickableFlags {
    private HashMap<String,Boolean> clickableFlags;

    public ViewClickableFlags(){
        clickableFlags = new HashMap<>();
    }

    public void addFlag(String viewname, boolean value){
        clickableFlags.put(viewname,value);
    }

    public void setViewClickable(String viewname, boolean value){
        if(clickableFlags.get(viewname)==null)
            return;

        clickableFlags.put(viewname,value);
    }

    public Boolean getValue(String viewname){
        if(clickableFlags.get(viewname)==null)
            return false;

        else
            return clickableFlags.get(viewname);
    }

    public void setAllViewsClickable(boolean isClickable){
        for(String viewname:clickableFlags.keySet()) {

            if (clickableFlags.get(viewname) != null)
                clickableFlags.put(viewname, isClickable);
        }
    }

}
