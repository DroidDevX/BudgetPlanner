package com.droiddevsa.budgetplanner.MVP;

import com.droiddevsa.budgetplanner.MVP.UI.ViewInterface.ActivityNavigator;

public interface MVPContract {

    interface BaseView{

        ActivityNavigator getActivityNavigator();

    }

    interface BasePresenter{
    }

    interface BaseRepository{

    }

}
