package com.ecm.dashobd_plus.services;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

public class DashObdService extends Service {


    private final static String TAG = DashObdService.class.getSimpleName();

    //These values have to match those in DashOBD (ObdLibService)
    private static final int MSG_REGISTER_CLIENT = 1;
    private static final int MSG_UNREGISTER_CLIENT = 2;

    private static final int MSG_GET_USER_STATUS = 3;
    private static final int MSG_GET_DATA = 4;


    private static final String DASHOBD_APP_PACKAGE = "com.ecm.dashobd";
    private static final String OBDLIBSERVICE_CLASS = "com.ecm.dashobd.services.ObdLibService";


    private boolean mIsBound = false;

    Messenger mMessenger;
    IncomingHandler incomingHandler;
    Messenger mServiceMessenger;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if(mMessenger == null){
            incomingHandler = new IncomingHandler();
            mMessenger = new Messenger(incomingHandler);
        }
        return mMessenger.getBinder();
    }




    private class CommunicatorThread extends Thread {

        @Override
        public void run() {

        }

    }



    private class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {

            }
        }
    }


    /**
     * <p>A function to bind to the ObdLibService in DashOBD app.</p>
     */
    private void doBindService(){

        Intent obdLibServiceIntent = new Intent();

        obdLibServiceIntent.setComponent(
                new ComponentName(DASHOBD_APP_PACKAGE, OBDLIBSERVICE_CLASS));

        boolean result = bindService(obdLibServiceIntent, mConnection, 0);

        if(result){
            mIsBound = true;
            
        }else{
            mIsBound = false;
        }

    }


    /**
     * <p>A function to unbind from the ObdLibService in DashOBD app.</p>
     */
    private void doUnbindService(){

    }


    /**
     * Class for interacting with the main interface of the service.
     */
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {

            mServiceMessenger = new Messenger(service);


            // We want to monitor the service for as long as we are
            // connected to it.
            try {
                Message msg = Message.obtain(null,
                        MSG_REGISTER_CLIENT);
                msg.replyTo = mMessenger;
                mServiceMessenger.send(msg);

            } catch (RemoteException e) {


            }
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            mServiceMessenger = null;
            mIsBound = false;


        }
    };


}
