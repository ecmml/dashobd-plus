// IDashObdRemoteServiceCallBack.aidl
package com.ecm.dashobd_plus;

// Declare any non-default types here with import statements

oneway IDashObdRemoteServiceCallBack {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void newObdData(int value);
}