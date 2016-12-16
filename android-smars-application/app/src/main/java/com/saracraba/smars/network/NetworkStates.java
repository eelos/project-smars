/*
 * Copyright (c) 2016 Sara Craba and Mario Di Dio
 *
 * Distributed under the GNU GPL v3 with additional terms.
 * For full terms see the file LICENSE.txt
 */
package com.saracraba.smars.network;

public interface NetworkStates {
    /**
     * Command sent returned success state
     */
    int SUCCESS = 200;

    /**
     * Command sent returned error state
     */
    int ERROR = 404;

    /**
     * Thread computation threw an exception
     */
    int THREAD_COMPUTATION_EXCEPTION = 500;

    /**
     * Current thread was interrupted while waiting
     */
    int THREAD_INTERRUPTED_EXCEPTION = 501;

    /**
     * Current thread timed out
     */
    int THREAD_TIMEOUT= 502;
}
