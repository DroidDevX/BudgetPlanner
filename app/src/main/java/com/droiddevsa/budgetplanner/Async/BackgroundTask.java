package com.droiddevsa.budgetplanner.Async;

import android.os.Bundle;

/**
 * This is a dependency of the BackgroundTaskManager.
 * Its job is to specify what needs to performed in the background via doInBackground()
 * and how the result should be returned in onPostExecute()
 * */
public interface BackgroundTask {
    Bundle doInBackground();
    void onPostExecute(Bundle result);
}
