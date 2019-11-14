package com.droiddevsa.budgetplanner.Async;


import android.os.Bundle;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class RxJavaWrapper implements BackgroundTaskManger {

    private static final String TAG = "RxJavaAsyncWrapper";
    private  static boolean taskIsRunning;
    Disposable disposable;

    public RxJavaWrapper(){
        taskIsRunning=false;
    }

    @Override
    public void executeTask(final BackgroundTask task) {
        if (taskIsRunning) {
            Log.e(TAG, "runBackgroundTask:--Task is already running abort operation ");
            return;
        }
        else {
            Log.d(TAG, "runBackgroundTask:--No existing tasks are running. Executing runBackgroundTask() now");
            taskIsRunning = true;
        }


        Observable<Bundle> observable = Observable.just(task).map(new Function<BackgroundTask, Bundle>() {
            @Override
            public Bundle apply(BackgroundTask task) throws Exception {
                Log.e(TAG, "apply: Thread name"+Thread.currentThread().getName() );
                Bundle result =task.doInBackground();
                if(result==null)
                    result = new Bundle();
                return result;

            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

        observable.subscribe(new Observer<Bundle>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe: ");
                disposable = d;
            }

            @Override
            public void onNext(Bundle result) {
                Log.d(TAG, "onNext: , Thread Name: "+Thread.currentThread().getName());
                task.onPostExecute(result);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: ",e );
                taskIsRunning = false;
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: ");
                taskIsRunning = false;
            }
        });

    }

    public void disposeObserverable(){
        Log.d(TAG, "disposeObserverable: ");
        if(disposable==null)
            return;

        if (!disposable.isDisposed())
            disposable.dispose();
    }

}
