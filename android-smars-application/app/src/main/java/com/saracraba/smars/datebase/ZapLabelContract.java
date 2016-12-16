/*
 * Copyright (c) 2016 Sara Craba and Mario Di Dio
 *
 * Distributed under the GNU GPL v3 with additional terms.
 * For full terms see the file LICENSE.txt
 */
package com.saracraba.smars.datebase;

import android.provider.BaseColumns;

/**
 * Define the Zap database for storing the labels associated with the buttons
 */
class ZapLabelContract {
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";

    private ZapLabelContract() {
    }

    /**
     * Inner class that defines the table contents
     */
    static class LabelEntry implements BaseColumns {
        static final String TABLE_NAME = "zap_table";
        static final String COLUMN_NAME_LINE = "line";
        static final String COLUMN_NAME_LABEL = "label";
    }

    /**
     * Command to create table:
     * CREATE TABLE zap_table (_id INTEGER PRIMARY KEY, line INTEGER,label TEXT )
     */
    static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + LabelEntry.TABLE_NAME + " (" +
                    LabelEntry._ID + " INTEGER PRIMARY KEY," +
                    LabelEntry.COLUMN_NAME_LINE + INTEGER_TYPE + COMMA_SEP +
                    LabelEntry.COLUMN_NAME_LABEL + TEXT_TYPE + " )";

    /**
     * Command to delete table:
     * DROP TABLE IF EXISTS zap_table
     */
    static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + LabelEntry.TABLE_NAME;
}
