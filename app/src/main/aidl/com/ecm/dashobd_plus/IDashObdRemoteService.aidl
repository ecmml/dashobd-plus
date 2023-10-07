// IDashObdRemoteService.aidl
package com.ecm.dashobd_plus;

// Declare any non-default types here with import statements
import com.ecm.dashobd_plus.IDashObdRemoteServiceCallBack;

interface IDashObdRemoteService {
     void registerCallBack(IDashObdRemoteServiceCallBack cb);
     void unregisterCallBack(IDashObdRemoteServiceCallBack cb);

}