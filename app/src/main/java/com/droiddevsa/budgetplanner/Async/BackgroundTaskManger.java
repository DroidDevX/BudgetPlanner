package com.droiddevsa.budgetplanner.Async;

/**
 * Custom interface implemented by classes that need to perform
 * background I/O operations. Such as a call to a database , or a network
 * call to a HTTP web server for example.
 *
 * */
public interface BackgroundTaskManger {


    /**Overide this method to perform your own implementation on how to
     * perform tasks in the background. The 'task' param contains callback methods
     * such as doInBackground() - to perform background related tasks, and onPostExecute()
     * to return the result on the main thread.
     *
     * It is up to you on how, and when to use these callbacks,
     *
     * @param task An interface argument, that contains callback methods ( doInBackground(), onPostExecute()), to perform background tasks, and to
     *             return to UI thread.
     *
     * */
    void executeTask(BackgroundTask task);

}
