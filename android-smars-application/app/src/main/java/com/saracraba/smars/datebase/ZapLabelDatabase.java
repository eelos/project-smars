/*
 * Copyright (c) 2016 Sara Craba and Mario Di Dio
 *
 * Distributed under the GNU GPL v3 with additional terms.
 * For full terms see the file LICENSE.txt
 */
package com.saracraba.smars.datebase;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.saracraba.smars.configuration.ZapConfig.ZapButtonNumber;
import com.saracraba.smars.datebase.ZapLabelContract.LabelEntry;

import static com.saracraba.smars.HomeAutomation.getZapDatabase;

/**
 * A not thread save utility for writing and retrieving data from the Zap database.
 * Database has max 5 rows, should not be a problem to run in the main thread
 */
public class ZapLabelDatabase {

    /**
     * Save the label related to the button number, update the value if the button number is
     * already associated to another label
     *
     * @param buttonNumber button number to associate the label
     * @param label        label to save
     */
    public static void saveLabel(@NonNull ZapButtonNumber buttonNumber, @NonNull String label) {
        String savedLabel = getLabel(buttonNumber);

        // Save the new label
        if (savedLabel.isEmpty()) {
            // Gets the database in write mode
            SQLiteDatabase db = getZapDatabase().getWritableDatabase();

            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(LabelEntry.COLUMN_NAME_LINE, buttonNumber.getValue());
            values.put(LabelEntry.COLUMN_NAME_LABEL, label);

            // Insert the new row, returning the primary key value of the new row
            long newRowId = db.insert(LabelEntry.TABLE_NAME, null, values);

            db.close();

            return;
        }

        // Update existing label
        SQLiteDatabase db = getZapDatabase().getReadableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(LabelEntry.COLUMN_NAME_LABEL, label);

        // Which row to update, based on the title
        String selection = LabelEntry.COLUMN_NAME_LINE + " LIKE ?";
        String[] selectionArgs = {Integer.toString(buttonNumber.getValue())};

        int count = db.update(
                LabelEntry.TABLE_NAME,                  // The table to query
                values,                                 // The new value
                selection,                              // The columns for the WHERE clause
                selectionArgs);                         // The values for the WHERE clause

        db.close();
    }

    /**
     * Return the label associated to the button number
     *
     * @param buttonNumber button number
     * @return the string representing the label associated to the button number or nul if not
     * label has been associated
     */
    public static String getLabel(@NonNull ZapButtonNumber buttonNumber) {
        // Gets the database in read mode
        SQLiteDatabase db = getZapDatabase().getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                LabelEntry._ID,
                LabelEntry.COLUMN_NAME_LINE,
                LabelEntry.COLUMN_NAME_LABEL
        };

        // Filter results WHERE "line" = 'buttonNumber.value'
        String selection = LabelEntry.COLUMN_NAME_LINE + " = ?";
        String[] selectionArgs = {Integer.toString(buttonNumber.getValue())};

        Cursor cursor = db.query(
                LabelEntry.TABLE_NAME,                    // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                      // don't sort the order
        );

        cursor.moveToFirst();

        String label;
        try {
            label = cursor.getString(cursor.getColumnIndexOrThrow(LabelEntry.COLUMN_NAME_LABEL));
        } catch (CursorIndexOutOfBoundsException e) {
            // no label saved
            label = "";
        }

        cursor.close();
        db.close();

        return label;
    }
}
