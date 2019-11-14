package com.droiddevsa.budgetplanner.MVP.UI.View;

import com.droiddevsa.budgetplanner.MVP.Data.Repository.SharedPreferenceManager;

public interface HistogramFragmentPageView {


    void onChartSettingsReady(SharedPreferenceManager.ConfigHistogram config);
}
