/*
 * Copyright (c) 2016 Sara Craba and Mario Di Dio
 *
 * Distributed under the GNU GPL v3 with additional terms.
 * For full terms see the file LICENSE.txt
 */
package com.saracraba.smars.network;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Execute ArduinoCallable elements to manage Arduino commands
 */
class ArduinoExecutor {
    private ExecutorService mExecutor;
    private Future mFuture;
    private ArduinoCallable mCallable;
    private Arduino.Callback mListener;

    ArduinoExecutor(@NonNull ArduinoCallable callable, Arduino.Callback listener) {
        mCallable = callable;
        mListener = listener;
    }

    /**
     * Execute callable in a single thread and run another AsyncTask to collect the result
     * to not freeze the main thread
     */
    void execute() {
        // Creates an Executor that uses a single worker thread operating off an unbounded queue
        mExecutor = Executors.newSingleThreadExecutor();

        // Submits a value-returning task for execution
        mFuture = mExecutor.submit(mCallable);

        // Wait till Future is done in a separate thread than return result to listener
        GetFuture getFuture = new GetFuture();
        getFuture.execute();
    }

    private class GetFuture extends AsyncTask<Void, Void, Integer> {
        @Override
        protected Integer doInBackground(Void... params) {
            // Wait till future is finished
            while (!mFuture.isDone()) ;

            // Get future result
            Integer result;
            try {
                result =(Integer) mFuture.get(5000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
                result = NetworkStates.THREAD_COMPUTATION_EXCEPTION;
            } catch (ExecutionException e) {
                e.printStackTrace();
                result = NetworkStates.THREAD_INTERRUPTED_EXCEPTION;
            } catch (TimeoutException e) {
                e.printStackTrace();
                result = NetworkStates.THREAD_TIMEOUT;
            }

            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);

            // Notify listener
            mListener.onResult(result);
        }
    }
}
