// IDashObdRemoteService.aidl
package com.ecm.dashobd_plus;

// Declare any non-default types here with import statements

interface IDashObdRemoteService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);


    /**
         * Often you want to allow a service to call back to its clients.
         * This shows how to do so, by registering a callback interface with
         * the service.
         */
        void registerCallback(IDashObdRemoteServiceCallback cb);

        /**
         * Remove a previously registered callback interface.
         */
        void unregisterCallback(IDashObdRemoteServiceCallback cb);


}