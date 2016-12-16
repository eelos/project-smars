/*
 * Copyright (c) 2016 Sara Craba and Mario Di Dio
 *
 * Distributed under the GNU GPL v3 with additional terms.
 * For full terms see the file LICENSE.txt
 */
package com.saracraba.smars.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class Persistence {
    private static final String TAG = Persistence.class.getSimpleName();

    private static final String KEY_SERVER_ADDRESS = "key_server_address";
    private static final String DEFAULT_SERVER_ADDRESS = "http://arduino.local/arduino";

    private static SharedPreferences mPreferences;

    public synchronized static void init(Context context) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void saveServerAddress(String serverAddress) {
        getDefaultPreferences().edit().putString(KEY_SERVER_ADDRESS, serverAddress).apply();
    }

    public static String getServerAddress() {
        return getDefaultPreferences().getString(KEY_SERVER_ADDRESS, DEFAULT_SERVER_ADDRESS);
    }

    private static SharedPreferences getDefaultPreferences() {
        if (mPreferences == null) {
            Log.e(TAG, "IllegalStateException: Need to call Init with context");
            throw new IllegalStateException();
        }
        return mPreferences;
    }
}
