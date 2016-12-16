/*
 * Copyright (c) 2016 Sara Craba and Mario Di Dio
 *
 * Distributed under the GNU GPL v3 with additional terms.
 * For full terms see the file LICENSE.txt
 */
package com.saracraba.smars;

import android.content.Context;
import android.support.annotation.NonNull;

import com.saracraba.smars.datebase.ZapLabelDbHelper;
import com.saracraba.smars.util.Persistence;

public class HomeAutomation {
    private static HomeAutomation sHomeAutomation;
    private ZapLabelDbHelper mZapDatabase;

    private HomeAutomation(Context context) {
        // Initialize persistence
        Persistence.init(context);

        // Initialize database
        mZapDatabase = new ZapLabelDbHelper(context);

    }

    public static void init(@NonNull Context context) {
        sHomeAutomation = new HomeAutomation(context);
    }

    public static HomeAutomation getInstance() {
        return sHomeAutomation;
    }

    public static ZapLabelDbHelper getZapDatabase() {
        return sHomeAutomation.mZapDatabase;
    }
}
