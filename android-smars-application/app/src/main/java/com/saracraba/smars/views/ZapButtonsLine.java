/*
 * Copyright (c) 2016 Sara Craba and Mario Di Dio
 *
 * Distributed under the GNU GPL v3 with additional terms.
 * For full terms see the file LICENSE.txt
 */
package com.saracraba.smars.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.saracraba.smars.configuration.ZapConfig.ZapButtonNumber;
import com.saracraba.smars.configuration.ZapConfig.ZapCommand;

import com.saracraba.smars.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Implement line view for each zap line
 * A line contains on and off buttons and a tag name
 */
public class ZapButtonsLine extends LinearLayout {
    @BindView(R.id.zap_button_on)
    Button mOnButton;

    @BindView(R.id.zap_button_off)
    Button mOffButton;

    @BindView(R.id.zap_button_tag)
    TextView mTag;

    public ZapButtonsLine(Context context) {
        super(context);

        init(context);
    }

    public ZapButtonsLine(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    private void init(Context context) {
        // inflate view
        setOrientation(LinearLayout.HORIZONTAL);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_zap_button, this, true);

        ButterKnife.bind(view);
    }

    /**
     * Set the tag view and associate the buttons with the corresponding action
     *
     * @param zapButtonsHolder line buttons holder
     * @param buttonAction callback to associate the buttons to the corresponding action
     */
    public void populateView(@NonNull final ZapButtonsHolder zapButtonsHolder,
                             @NonNull final ButtonAction buttonAction) {
        // Set tag
        mTag.setText(zapButtonsHolder.getTag());

        // Set on button
        mOnButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonAction.onButtonPressed(zapButtonsHolder.getZapButtonNumber(),
                        ZapCommand.ON);
            }
        });

        // Set off button
        mOffButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonAction.onButtonPressed(zapButtonsHolder.getZapButtonNumber(),
                        ZapCommand.OFF);
            }
        });
    }

    /**
     * Callback for zap line button action
     */
    public interface ButtonAction {
        void onButtonPressed(ZapButtonNumber buttonNumber, ZapCommand command);
    }

    /**
     * Holder for zap line elements
     * Contains a tag string for the name and the corresponding zap line
     */
    public static class ZapButtonsHolder {
        private String mTag;
        private ZapButtonNumber mZapButtonNumber;

        public ZapButtonsHolder(String Tag, ZapButtonNumber LineNumber) {
            this.mTag = Tag;
            this.mZapButtonNumber = LineNumber;
        }

        public ZapButtonNumber getZapButtonNumber() {
            return mZapButtonNumber;
        }

        public String getTag() {
            return mTag;
        }
    }
}
