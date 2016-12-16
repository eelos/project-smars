/*
 * Copyright (c) 2016 Sara Craba and Mario Di Dio
 *
 * Distributed under the GNU GPL v3 with additional terms.
 * For full terms see the file LICENSE.txt
 */
package com.saracraba.smars.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.saracraba.smars.R;
import com.saracraba.smars.configuration.ZapConfig;
import com.saracraba.smars.datebase.ZapLabelDatabase;
import com.saracraba.smars.views.ZapSetupLine;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Setup Zap View allow to change labels associated with the outlet in the main zap view
 */
public class ZapSetupFragment extends BaseFragment {
    @BindView(R.id.zap_outlet_1)
    ZapSetupLine mOutlet_1;

    @BindView(R.id.zap_outlet_2)
    ZapSetupLine mOutlet_2;

    @BindView(R.id.zap_outlet_3)
    ZapSetupLine mOutlet_3;

    @BindView(R.id.zap_outlet_4)
    ZapSetupLine mOutlet_4;

    @BindView(R.id.zap_outlet_5)
    ZapSetupLine mOutlet_5;

    @OnClick(R.id.setup_zap_save_button)
    /**
     * Save label value in Shared Preference
     */
    void onSaveButtonClick() {
        // Label 1
        String label = mOutlet_1.getText();
        if (!label.isEmpty()) {
            ZapLabelDatabase.saveLabel(ZapConfig.ZapButtonNumber.ONE, label);
        }

        //label 2
        label = mOutlet_2.getText();
        if (!label.isEmpty()) {
            ZapLabelDatabase.saveLabel(ZapConfig.ZapButtonNumber.TWO, label);
        }

        //label 3
        label = mOutlet_3.getText();
        if (!label.isEmpty()) {
            ZapLabelDatabase.saveLabel(ZapConfig.ZapButtonNumber.THREE, label);
        }

        //label 4
        label = mOutlet_4.getText();
        if (!label.isEmpty()) {
            ZapLabelDatabase.saveLabel(ZapConfig.ZapButtonNumber.FOUR, label);
        }

        //label 5
        label = mOutlet_5.getText();
        if (!label.isEmpty()) {
            ZapLabelDatabase.saveLabel(ZapConfig.ZapButtonNumber.FIVE, label);
        }

        // Notify user
        Toast.makeText(getActivity(), getResources().getString(R.string.setup_zap_saved),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setup_zap, container, false);

        ButterKnife.bind(this, view);

        // Set hint in the labels with saved values
        mOutlet_1.populateView(
                getResources().getString(R.string.setup_zap_outlet_1),
                ZapLabelDatabase.getLabel(ZapConfig.ZapButtonNumber.ONE));

        mOutlet_2.populateView(getResources().getString(R.string.setup_zap_outlet_2),
                ZapLabelDatabase.getLabel(ZapConfig.ZapButtonNumber.TWO));

        mOutlet_3.populateView(getResources().getString(R.string.setup_zap_outlet_3),
                ZapLabelDatabase.getLabel(ZapConfig.ZapButtonNumber.THREE));

        mOutlet_4.populateView(getResources().getString(R.string.setup_zap_outlet_4),
                ZapLabelDatabase.getLabel(ZapConfig.ZapButtonNumber.FOUR));

        mOutlet_5.populateView(getResources().getString(R.string.setup_zap_outlet_5),
                ZapLabelDatabase.getLabel(ZapConfig.ZapButtonNumber.FIVE));

        return view;
    }
}
