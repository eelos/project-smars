/*
 * Copyright (c) 2016 Sara Craba and Mario Di Dio
 *
 * Distributed under the GNU GPL v3 with additional terms.
 * For full terms see the file LICENSE.txt
 */
package com.saracraba.smars.activities;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.saracraba.smars.HomeAutomation;
import com.saracraba.smars.R;
import com.saracraba.smars.fragments.AboutFragment;
import com.saracraba.smars.fragments.BaseFragment;
import com.saracraba.smars.fragments.ServerSetupFragment;
import com.saracraba.smars.fragments.ZapFragment;
import com.saracraba.smars.fragments.ZapSetupFragment;

/**
 * Main Activity, contains menu drawer and main fragment container
 */
public class MainActivity extends BaseActivity {
    private final static String TAG_FRAGMENT = "TAG_FRAGMENT";

    private String[] mSettingTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    private DrawerItem mDrawerItem = DrawerItem.HOME;

    /**
     * Menu list
     */
    public enum DrawerItem {
        // Must be equal to settings_array in strings
        HOME,
        SERVER,
        ZAP,
        ABOUT;

        public DrawerItem getDrawerItem(int position) {
            for (DrawerItem drawerItem : values()) {
                if (drawerItem.ordinal() == position) {
                    return drawerItem;
                }
            }

            // default return home
            return HOME;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HomeAutomation.init(this);

        mSettingTitles = getResources().getStringArray(R.array.settings_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Drawer settings
        final View header = getLayoutInflater().inflate(R.layout.drawer_header, null);
        mDrawerList.addHeaderView(header, null, false);
        mDrawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_list_item, mSettingTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // Attach zap fragment as first fragment
        BaseFragment fragment = new ZapFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_main_fragment, fragment, TAG_FRAGMENT)
                .addToBackStack(null)
                .commit();
    }

    /**
     * Drawer listener
     */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            BaseFragment fragment;

            switch (mDrawerItem.getDrawerItem(position - 1)) {
                case SERVER:
                    fragment = new ServerSetupFragment();
                    break;
                case ZAP:
                    fragment = new ZapSetupFragment();
                    break;
                case ABOUT:
                    fragment = new AboutFragment();
                    break;
                default:
                    fragment = new ZapFragment();
            }

            // Show the fragment relative to the selected menu
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.activity_main_fragment, fragment, TAG_FRAGMENT)
                    .addToBackStack(null)
                    .commit();

            // Highlight the selected item, and close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerLayout.closeDrawer(mDrawerList);
        }
    }

    @Override
    public void onBackPressed() {
        BaseFragment fragment = (BaseFragment) getSupportFragmentManager()
                .findFragmentByTag(TAG_FRAGMENT);

        if (fragment instanceof ZapFragment) {
            // Close application
            finish();
        } else {
            // Show  home page
            fragment = new ZapFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.activity_main_fragment, fragment, TAG_FRAGMENT)
                    .addToBackStack(null)
                    .commit();
        }

    }
}
