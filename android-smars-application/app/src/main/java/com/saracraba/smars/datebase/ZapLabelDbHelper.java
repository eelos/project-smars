/*
 * Copyright (c) 2016 Sara Craba and Mario Di Dio
 *
 * Distributed under the GNU GPL v3 with additional terms.
 * For full terms see the file LICENSE.txt
 */
package com.saracraba.smars.datebase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.saracraba.smars.datebase.ZapLabelContract.SQL_CREATE_ENTRIES;
import static com.saracraba.smars.datebase.ZapLabelContract.SQL_DELETE_ENTRIES;

/**
 * Helper for creating and managing the Zap database
 */
public class ZapLabelDbHelper extends SQLiteOpenHelper {
    // If change the database schema, must increment the database version.
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ZapLabel.db";

    public ZapLabelDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Simply discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
