/*
 * Copyright (c) 2016 Sara Craba and Mario Di Dio
 *
 * Distributed under the GNU GPL v3 with additional terms.
 * For full terms see the file LICENSE.txt
 */
package com.saracraba.smars.configuration;

/**
 * Zap remote configuration.
 * The Zap remote have 5 buttons line, each line has 2 commands (on/off)
 */
public class ZapConfig {
    private static final String TAG = ZapConfig.class.getName();

    /**
     * Zap buttons
     */
    public enum ZapButtonNumber {
        ONE(1),    // line #1
        TWO(2),    // line #2
        THREE(3),  // line #3
        FOUR(4),   // line #4
        FIVE(5);   // line #5

        private int value;

        ZapButtonNumber(int val) {
            value = val;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * Zap commands
     */
    public enum ZapCommand {
        OFF(0),     // command off
        ON(1);      // command on

        private int value;

        ZapCommand(int val) {
            value = val;
        }

        public int getValue() {
            return value;
        }
    }
}
