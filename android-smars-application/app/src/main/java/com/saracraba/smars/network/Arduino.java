/*
 * Copyright (c) 2016 Sara Craba and Mario Di Dio
 *
 * Distributed under the GNU GPL v3 with additional terms.
 * For full terms see the file LICENSE.txt
 */
package com.saracraba.smars.network;

import com.saracraba.smars.configuration.ArduinoConfig;

import com.saracraba.smars.configuration.ZapConfig.ZapButtonNumber;
import com.saracraba.smars.configuration.ZapConfig.ZapCommand;

/**
 * Utility class to interact with arduino
 *
 * Use Executor Framework : https://examples.javacodegeeks.com/core-java/util/concurrent/runnablefuture/java-runnablefuture-example/
 */
public class Arduino {
    private static final String TAG = Arduino.class.getName();

    private ArduinoExecutor mArduinoExecutor;

    /**
     * Send a ZAP command to arduino
     *
     * @param buttonNumber zap line number to control
     * @param command zap command to send
     * @param listener callback for command result
     */
    public void sendZapCommand(ZapButtonNumber buttonNumber,
                               ZapCommand command,
                               Callback listener) {

        // Get complete command url
        String zapCommandUrl = ArduinoConfig.getZapStringCommand(buttonNumber, command);

        // Execute zap command
        mArduinoExecutor = new ArduinoExecutor(
                new CallableSendCommand(zapCommandUrl),
                listener);
        mArduinoExecutor.execute();
    }

    public interface Callback {
        void onResult(int result);
    }
}
