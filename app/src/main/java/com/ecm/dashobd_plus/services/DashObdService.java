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
import com.ecm.dashobd.IDashObdRemoteService;
import com.ecm.dashobd.IDashObdRemoteServiceCallBack;
import com.ecm.dashobd.ObdData;

import java.util.ArrayList;

public class DashObdService extends Service {


    private final static String TAG = DashObdService.class.getSimpleName();

    //These values have to match those in DashOBD (ObdLibService)
    public static final int MSG_REGISTER_CLIENT = 1;
    private static final int MSG_UNREGISTER_CLIENT = 2;

    private static final int MSG_GET_USER_STATUS = 3;

    private static final int MSG_REQUEST_DATA = 4;

    public static final int MSG_RECEIVED_DATA = 5;


    private static final String DASHOBD_APP_PACKAGE = "com.ecm.dashobd";
    private static final String OBDLIBSERVICE_CLASS = "com.ecm.dashobd.services.ObdLibService";


    private boolean mIsBound = false;

    Messenger mMessenger;
    IncomingHandler incomingHandler;
    Messenger mServiceMessenger;


    ArrayList<Messenger> mClients;





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
        mClients = new ArrayList<>();
    }




    private class CommunicatorThread extends Thread {

        @Override
        public void run() {
            Looper.prepare();

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
                    mClients.add((Messenger) (msg.replyTo));
                    break;
                case MSG_REQUEST_DATA:

                case MSG_RECEIVED_DATA:



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
            try {
                dashObdRemoteService.registerCallBack(serviceCallBack);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }

        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            mServiceMessenger = null;
            mIsBound = false;


        }
    };

    IDashObdRemoteServiceCallBack serviceCallBack = new IDashObdRemoteServiceCallBack.Stub() {

        @Override
        public void newObdData(ObdData data) throws RemoteException {
            Log.v(TAG, "new obd data: " + data.getName() + " " + data.getResultUnit());
            Message message = Message.obtain(null, MSG_RECEIVED_DATA);
            message.obj = data;
            broadcast(message);


        }

        @Override
        public void allMonitoredParameters(ObdData[] allParameters) throws RemoteException {
            Log.v(TAG, "Received parameters:=> " + allParameters);
        }


    };

    private void broadcast(Message msg){
        Log.v(TAG, "Broadcasting... to " + mClients.size()  + " Clients" );

        for (int i = 0; i < mClients.size(); i++) {
            try {
                Message broadcastMsg = Message.obtain();
                broadcastMsg.copyFrom(msg);
                if (mClients.size() > i && mClients.get(i) != null) {
                    mClients.get(i).send(broadcastMsg);
                }
            } catch (RemoteException e) {
                // Failed to send message to client. Print trace and try next client.
                e.printStackTrace();
            }
        }
    }





    @Override
    public void onDestroy(){
        super.onDestroy();
        doUnbindService();
    }


}
