package com.droiddevsa.budgetplanner.Utilities;

import android.view.View;

public abstract class SingleClickListener implements View.OnClickListener {
    private boolean clickEnabled =true;
    @Override
    final public void onClick(View view) {
        if(clickEnabled) {
            clickEnabled = false;
            onSingleClick(view);
        }
    }

    /*Executes only once, then further invocations to this method is ignored
     Call setClickEnabled(true) to enable click again.
    *
    * */

    public abstract void onSingleClick(View view);


    public boolean clickEnabled(){
        return clickEnabled;
    }

    public void setClickEnabled(boolean isEnabled){
        clickEnabled=isEnabled;
    }
}
