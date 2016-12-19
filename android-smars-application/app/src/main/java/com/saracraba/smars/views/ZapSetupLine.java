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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.saracraba.smars.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Implement line view for each setup zap line
 */
public class ZapSetupLine extends LinearLayout {
    @BindView(R.id.zap_setup_outlet_number)
    TextView mOutletNumber;

    @BindView(R.id.setup_zap_label)
    EditText mLabel;

    public ZapSetupLine(Context context) {
        super(context);

        init(context);
    }

    public ZapSetupLine(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    private void init(Context context) {
        // inflate view
        setOrientation(LinearLayout.HORIZONTAL);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_zap_setup_line, this, true);

        ButterKnife.bind(view);
    }

    /**
     * Set the outlet number and the associate label hint
     *
     * @param outletNumber outlet number
     * @param label        label associate with the outlet number
     */
    public void populateView(@NonNull final String outletNumber,
                             @NonNull final String label) {
        // Set outlet number
        mOutletNumber.setText(outletNumber);

        // Set label hint
        mLabel.setHint(label);
    }

    /**
     * @return the string text in the label
     */
    public String getText() {
        return mLabel.getText().toString();
    }
}
