/*
 * Copyright (c) 2016 Sara Craba and Mario Di Dio
 *
 * Distributed under the GNU GPL v3 with additional terms.
 * For full terms see the file LICENSE.txt
 */
package com.saracraba.smars.configuration;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.saracraba.smars.configuration.ZapConfig.ZapButtonNumber;
import com.saracraba.smars.configuration.ZapConfig.ZapCommand;
import com.saracraba.smars.util.Persistence;

/**
 * Server Arduino Yún configuration
 * https://www.arduino.cc/en/Guide/ArduinoYun
 */
public class ArduinoConfig {
    private static final String TAG = ArduinoConfig.class.getName();

    /**
     * Application protocol
     */
    private static final String APP_PROTOCOL = "http://";

    /**
     *------------------------------------------------------------------
     * Arduino internal server connection
     *------------------------------------------------------------------
     */

    /**
     * Arduino Yun endpoint for the not pre-configured commands.
     * Anything added to the URL after this end point is passed from the webserver to the sketch
     * on the 32U4. You can define your APIs inside the sketch
     */
    private static final String ENDPOINT = "arduino";

    /**
     * Custom action types - see sketch features in Arduino
     */
    private enum ActionType {
        ZAP("zap");            // handle zap remote command

        private String value;

        ActionType(String val) {
            value = val;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * ------------------------------------------------------------------
     * Utilities
     * ------------------------------------------------------------------
     */

    private static final String PAGE_SEPARATOR = "/";
    private static final String PORT_SEPARATOR = ":";

    /**
     * Return URL to simulate zap remote.
     * String format:
     * "http://myServer:myPort/arduino/zap/buttonNumber/command"
     *
     * @param buttonNumber button line to press
     * @param command      command for the button line (on or off)
     * @return a String representation of the URL to 'press' the remote button
     */
    public static String getZapStringCommand(ZapButtonNumber buttonNumber, ZapCommand command) {
        String zapCommand =
                //Server address
                Persistence.getServerAddress() +
                        // action
                        ActionType.ZAP.getValue() + PAGE_SEPARATOR +
                        // button
                        buttonNumber.getValue() + PAGE_SEPARATOR +
                        // command
                        command.getValue();

        Log.i(TAG, "ZapStringCommand: " + zapCommand);
        return zapCommand;
    }

    /**
     * Return complete address to access Arduino. Port can be omitted.
     * String format:
     * "http://address:port/arduino/"
     *
     * @param address server address where arduino is connected
     * @param port    server port where Arduino is connected
     * @return a String representation of the server URL where Arduino is connected
     */
    public static String getFormattedServerAddress(@NonNull String address,
                                                   @Nullable Integer port) {
        // http://address
        StringBuilder serverURL = new StringBuilder(APP_PROTOCOL + address);

        if (port != null) {
            // http://address:port
            serverURL.append(PORT_SEPARATOR + Integer.toString(port));
        }

        // http://address:port/arduino/
        serverURL.append(PAGE_SEPARATOR + ENDPOINT + PAGE_SEPARATOR);

        return serverURL.toString();
    }
}
