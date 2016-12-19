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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.saracraba.smars.R;
import com.saracraba.smars.util.Persistence;

import static com.saracraba.smars.configuration.ArduinoConfig.getFormattedServerAddress;

/**
 * Setting page for server allow to change server address where Arduino is connected
 */
public class ServerSetupFragment extends BaseFragment {
    private EditText mAddress;
    private EditText mPort;
    private Button mSaveButton;
    private TextView mCurrentAddress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setup_server, container, false);

        mAddress = (EditText) view.findViewById(R.id.setup_address);
        mPort = (EditText) view.findViewById(R.id.setup_port);
        mSaveButton = (Button) view.findViewById(R.id.setup_server_save_button);
        mCurrentAddress = (TextView) view.findViewById(R.id.setup_server_current_address);

        String serverUrl = getResources().getString(R.string.setup_server_current) +
                Persistence.getServerAddress();
        mCurrentAddress.setText(serverUrl);

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveButtonClick();
            }
        });

        return view;
    }

    /**
     * Save server address in Shared Preferences
     */
    private void onSaveButtonClick() {
        String portString = mPort.getText().toString();
        Integer portInt = portString.isEmpty() ? null : Integer.parseInt(portString);

        // Get formatted server url string
        String address = getFormattedServerAddress(mAddress.getText().toString(), portInt);

        // Save server address in Shared Preferences
        Persistence.saveServerAddress(address);

        // Update "Current address: ..."
        String serverUrl = getResources().getString(R.string.setup_server_current) + address;
        mCurrentAddress.setText(serverUrl);

        // Notify user
        Toast.makeText(getActivity(), getResources().getString(R.string.setup_server_saved),
                Toast.LENGTH_SHORT).show();
    }
}
