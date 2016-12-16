/*
 * Copyright (c) 2016 Sara Craba and Mario Di Dio
 *
 * Distributed under the GNU GPL v3 with additional terms.
 * For full terms see the file LICENSE.txt
 */
package com.saracraba.smars.network;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

/**
 * Send a command to Arduino
 */
class CallableSendCommand implements ArduinoCallable {
    private static final String TAG = CallableSendCommand.class.getName();

    private String mCommandUrl;
    private String mResponse;
    private String mMessage = "";
    private HttpURLConnection mConnection = null;
    private int mRandomNumber;
    private String mThreadTag;

    CallableSendCommand(String commandUrl) {
        mCommandUrl = commandUrl;

        // For debug use only
        mRandomNumber = new Random().nextInt(500);
        mThreadTag = "Thread " + mRandomNumber + " - ";
    }

    /**
     * Main working thread that send command to Arduino with a POST request and wait the response
     *
     * @return integer from {@link NetworkStates} that represent the server response
     */
    public Integer call() {
        try {
            // Send a POST request to the server with a command
            URL commandUrl = new URL(mCommandUrl);
            mConnection = (HttpURLConnection) commandUrl.openConnection();

            mConnection.setDoOutput(true);
            mConnection.setRequestMethod("POST");

            Log.i(TAG, mThreadTag + "Connect to server.");

            // Interpret the response message
            BufferedReader bufferedReader =
                    new BufferedReader(new InputStreamReader(mConnection.getInputStream()));
            while ((mResponse = bufferedReader.readLine()) != null) {
                mMessage += mResponse;
            }
            bufferedReader.close();

            Log.i(TAG, mThreadTag + "Server response: " + mMessage);
            return NetworkStates.SUCCESS;

        } catch (IOException e) {
            Log.i(TAG, mThreadTag + "Server response: " + e.getMessage());
            e.printStackTrace();
            return NetworkStates.ERROR;
        } finally {
            mConnection.disconnect();
            Log.i(TAG, mThreadTag + "Disconnect from server");
        }
    }
}
