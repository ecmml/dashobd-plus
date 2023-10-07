// IDashObdRemoteServiceCallBack.aidl
package com.ecm.dashobd_plus;

// Declare any non-default types here with import statements

oneway interface IDashObdRemoteServiceCallBack {
    void newObdData(int test);
}