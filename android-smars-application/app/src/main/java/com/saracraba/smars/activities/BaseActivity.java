/*
 * Copyright (c) 2016 Sara Craba and Mario Di Dio
 *
 * Distributed under the GNU GPL v3 with additional terms.
 * For full terms see the file LICENSE.txt
 */
package com.saracraba.smars.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Base Home Automation activity
 */
public class BaseActivity extends AppCompatActivity {
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
    }
}
