/*
 * Copyright (c) 2016 Sara Craba and Mario Di Dio
 *
 * Distributed under the GNU GPL v3 with additional terms.
 * For full terms see the file LICENSE.txt
 */
package com.saracraba.smars.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.saracraba.smars.R;
import com.saracraba.smars.configuration.ZapConfig.ZapButtonNumber;
import com.saracraba.smars.configuration.ZapConfig.ZapCommand;
import com.saracraba.smars.datebase.ZapLabelDatabase;
import com.saracraba.smars.network.Arduino;
import com.saracraba.smars.network.NetworkStates;
import com.saracraba.smars.views.ZapButtonsLine.ZapButtonsHolder;
import com.saracraba.smars.views.ZapButtonsLine.ButtonAction;
import com.saracraba.smars.views.ZapButtonsLine;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.saracraba.smars.network.NetworkStates.ERROR;
import static com.saracraba.smars.network.NetworkStates.THREAD_TIMEOUT;

/**
 * Main interface to simulate zap remote
 */
public class ZapFragment extends BaseFragment {
    private static final String TAG = ZapFragment.class.getName();

    @BindView(R.id.message_text_view)
    TextView mMessageTextView;

    @BindView(R.id.zap_line_1)
    ZapButtonsLine mLine_1;

    @BindView(R.id.zap_line_2)
    ZapButtonsLine mLine_2;

    @BindView(R.id.zap_line_3)
    ZapButtonsLine mLine_3;

    @BindView(R.id.zap_line_4)
    ZapButtonsLine mLine_4;

    @BindView(R.id.zap_line_5)
    ZapButtonsLine mLine_5;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zap, container, false);

        ButterKnife.bind(this, view);

        initView(
                mLine_1,
                ZapLabelDatabase.getLabel(ZapButtonNumber.ONE),
                ZapButtonNumber.ONE);

        initView(
                mLine_2,
                ZapLabelDatabase.getLabel(ZapButtonNumber.TWO),
                ZapButtonNumber.TWO);

        initView(
                mLine_3,
                ZapLabelDatabase.getLabel(ZapButtonNumber.THREE),
                ZapButtonNumber.THREE);

        initView(
                mLine_4,
                ZapLabelDatabase.getLabel(ZapButtonNumber.FOUR),
                ZapButtonNumber.FOUR);

        initView(
                mLine_5,
                ZapLabelDatabase.getLabel(ZapButtonNumber.FIVE),
                ZapButtonNumber.FIVE);

        return view;
    }

    private void initView(ZapButtonsLine buttonLine, String tag, ZapButtonNumber lineNumber) {
        ZapButtonsHolder zapButtonsHolder = new ZapButtonsHolder(tag, lineNumber);
        ButtonAction buttonAction = new ButtonAction() {
            @Override
            public void onButtonPressed(ZapButtonNumber buttonNumber, ZapCommand command) {
                onZapButtonPressed(buttonNumber, command);
            }
        };

        buttonLine.populateView(zapButtonsHolder, buttonAction);
    }

    /**
     * Select command in the given button number
     *
     * @param buttonNumber button to 'press'
     * @param command      command on/off
     */
    private void onZapButtonPressed(ZapButtonNumber buttonNumber, ZapCommand command) {
        Log.i(TAG, "Pressed button " + buttonNumber.getValue() + " with command " +
                command.getValue());

        mMessageTextView.setText(getResources().getString(R.string.zap_connecting_message));
        mMessageTextView.setTextColor(getResources().getColor(R.color.colorPrimaryText));

        Arduino arduino = new Arduino();

        arduino.sendZapCommand(buttonNumber, command, new Arduino.Callback() {
            @Override
            public void onResult(int result) {
                if (result != NetworkStates.SUCCESS) {
                    mMessageTextView.setText(parseErrorMessage(result));
                    mMessageTextView.setTextColor(
                            getResources().getColor(R.color.colorAccentColorRed));
                } else {
                    mMessageTextView.setText("");
                }
            }
        });
    }

    /**
     * Parse the error state in a human readable string
     *
     * @param networkState error state
     * @return string definition of error state
     */
    private String parseErrorMessage(final int networkState) {
        StringBuilder error = new StringBuilder("Error - ");

        switch (networkState) {
            case ERROR:
                error.append("Server not connected");
                break;
            case THREAD_TIMEOUT:
                error.append("Server time out");
                break;
            default:
                error.append("Internal application error");
        }

        return error.toString();
    }
}
