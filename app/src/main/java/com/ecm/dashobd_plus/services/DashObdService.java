package com.ecm.dashobd_plus.services;

import static com.ecm.dashobd_plus.Constants.DATA_REFRESH_RATE;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

import com.ecm.dashobd_plus.BuildConfig;
import com.ecm.dashobd_plus.IDashObdRemoteService;
import com.ecm.dashobd_plus.ObdData;
import com.ecm.dashobd_plus.R;

import java.text.RuleBasedCollator;
import java.util.ArrayList;

public class DashObdService extends Service {


    private final static String TAG = DashObdService.class.getSimpleName();

    //These values have to match those in DashOBD (ObdLibService)
    private static final int MSG_REGISTER_CLIENT = 1;
    private static final int MSG_UNREGISTER_CLIENT = 2;

    private static final int MSG_GET_USER_STATUS = 3;

    private static final int MSG_REQUEST_DATA = 4;


    private static final String DASHOBD_APP_PACKAGE = "com.ecm.dashobd";
    private static final String OBDLIBSERVICE_CLASS = "com.ecm.dashobd.services.ObdLibService";


    private boolean mIsBound = false;

    Messenger mMessenger;
    IncomingHandler incomingHandler;
    Messenger mServiceMessenger;


    static ArrayList<ObdData> obdDataList = new ArrayList<>();

    public static ArrayList<ObdData> getObdDataList(){
        return obdDataList;
    }




    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if(mMessenger == null){
            incomingHandler = new IncomingHandler();
            mMessenger = new Messenger(incomingHandler);
        }
        return mMessenger.getBinder();
    }

    @Override
    public void onCreate(){
        if(BuildConfig.DEBUG)Log.v(TAG, "Starting DashOBDService");
        CommunicatorThread communicatorThread = new CommunicatorThread();
        communicatorThread.start();
    }




    private class CommunicatorThread extends Thread {

        @Override
        public void run() {
            Looper.prepare();
            obdDataList = new ArrayList<>();
            if(mMessenger == null){
                incomingHandler = new IncomingHandler();
                mMessenger = new Messenger(incomingHandler);
            }

            doBindService();
            mainHandler = new Handler();
            mainHandler.post(bindToService);



            Looper.loop();
        }

    }


    Handler mainHandler;





    Runnable bindToService = new Runnable() {
        @Override
        public void run() {
            doBindService();


            if(!mIsBound) {
                mainHandler.removeCallbacks(bindToService);
                mainHandler.postDelayed(bindToService, 3000);
            }else{
                mainHandler.post(requestData);
            }
        }
    };
    Runnable requestData = new Runnable() {
        @Override
        public void run() {

            sendDataRequest();
            mainHandler.removeCallbacks(requestData);
            mainHandler.postDelayed(requestData, DATA_REFRESH_RATE); //make it 10 milliseconds;
        }
    };


    private void sendDataRequest(){
        if(mServiceMessenger == null)return;

        Message msg = Message.obtain(null, MSG_REQUEST_DATA);
        msg.replyTo = mMessenger;
        try {
            if(BuildConfig.DEBUG)Log.i(TAG, "::sendDataRequest=> Sending Data Request");
            mServiceMessenger.send(msg);
        } catch (RemoteException e) {
            if(BuildConfig.DEBUG)Log.e(TAG, "::sendDataRequest=> Could not send data request",e);
        }
    }



    private class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case MSG_REGISTER_CLIENT:
                    break;
                case MSG_REQUEST_DATA:

                    if(msg.arg1 == 0){
                        //TO-DO: handle is not a top supporter
                    }else{
                        Log.v(TAG, "received data packet: " + msg.obj);
                        obdDataList = (ArrayList<ObdData>) msg.obj;
                    }


            }
        }
    }


    /**
     * <p>A function to bind to the ObdLibService in DashOBD app.</p>
     */
    private void doBindService(){

        if(BuildConfig.DEBUG)Log.i(TAG, "::doBindService => Trying to bind to DashOBD");
        Intent obdLibServiceIntent = new Intent();

        obdLibServiceIntent.setComponent(
                new ComponentName(DASHOBD_APP_PACKAGE, OBDLIBSERVICE_CLASS));

        boolean result = bindService(obdLibServiceIntent, mConnection, Context.BIND_AUTO_CREATE);

        if(result){
            if(BuildConfig.DEBUG)Log.v(TAG, "Binding was successful");
            mIsBound = true;
            
        }else{
            if(BuildConfig.DEBUG)Log.v(TAG, "Binding failed.");
            mIsBound = false;
        }

    }


    /**
     * <p>A function to unbind from the ObdLibService in DashOBD app.</p>
     */
    private void doUnbindService(){

        if(mServiceMessenger != null){
            Message message = Message.obtain(null, MSG_UNREGISTER_CLIENT);
            message.replyTo = mMessenger;
            try {
                mServiceMessenger.send(message);
            } catch (RemoteException e) {
                if(BuildConfig.DEBUG)Log.e(TAG, "::doUnbindService() => could not send unregister message", e);
            }
        }

        unbindService(mConnection);
        mIsBound = false;

    }

    IDashObdRemoteService dashObdRemoteService;

    /**
     * Class for interacting with the main interface of the service.
     */
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {

           /* mServiceMessenger = new Messenger(service);

            Log.v(TAG, "::mConnection => " + service);


            // We want to monitor the service for as long as we are
            // connected to it.
            try {
                Message msg = Message.obtain(null,
                        MSG_REGISTER_CLIENT);
                msg.replyTo = mMessenger;
                mServiceMessenger.send(msg);

            } catch (RemoteException e) {

                mServiceMessenger = null;
                mIsBound = false;
            }
*/
            Log.v(TAG, "::mConnection => " + service);
            dashObdRemoteService = IDashObdRemoteService.Stub.asInterface(service);



        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            mServiceMessenger = null;
            mIsBound = false;


        }
    };





    @Override
    public void onDestroy(){
        super.onDestroy();
        doUnbindService();
    }


}
