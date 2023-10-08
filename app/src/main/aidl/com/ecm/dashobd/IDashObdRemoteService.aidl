// IDashObdRemoteService.aidl
package com.ecm.dashobd;

// Declare any non-default types here with import statements
import com.ecm.dashobd.IDashObdRemoteServiceCallBack;

interface IDashObdRemoteService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    //void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
     //       double aDouble, String aString);

    void registerCallBack(IDashObdRemoteServiceCallBack cb);
    void unregisterCallBack(IDashObdRemoteServiceCallBack cb);


    /**
         * Often you want to allow a service to call back to its clients.
         * This shows how to do so, by registering a callback interface with
         * the service.
         */
        //void registerCallback(IDashObdRemoteServiceCallback cb);

        /**
         * Remove a previously registered callback interface.
         */
        //void unregisterCallback(IDashObdRemoteServiceCallback cb);


}