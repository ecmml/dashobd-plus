// IDashObdRemoteServiceCallBack.aidl
package com.ecm.dashobd;


// Declare any non-default types here with import statements
parcelable ObdData;

oneway interface IDashObdRemoteServiceCallBack {
        /**
         * Demonstrates some basic types that you can use as parameters
         * and return values in AIDL.
         */
        void newObdData(in ObdData data);

        void allMonitoredParameters(in ObdData[] allParameters);
 }
