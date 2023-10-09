package com.ecm.dashobd_plus;

import static com.ecm.dashobd_plus.Constants.DATA_REFRESH_RATE;
import static com.ecm.dashobd_plus.services.DashObdService.MSG_RECEIVED_DATA;
import static com.ecm.dashobd_plus.services.DashObdService.MSG_REGISTER_CLIENT;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ecm.dashobd_plus.MainCarActivity;
import com.ecm.dashobd.ObdData;
import com.ecm.dashobd_plus.R;
import com.ecm.dashobd_plus.models.Zone;
import com.ecm.dashobd_plus.services.DashObdService;
import com.github.anastr.speedviewlib.PointerSpeedometer;



public class GaugeFragment extends Fragment {





    private PointerSpeedometer mGauge;

    private boolean mIsBound;

    private Messenger mServiceMessenger;

    private Messenger mMessenger;

    private TextView           mParameterView;

    private final static String TAG = GaugeFragment.class.getSimpleName();

    private MainCarActivity mActivity;

    private Zone mZone;

    public GaugeFragment(@NonNull Zone zone){
        this.mZone = zone;
        mActivity = (MainCarActivity) getContext();
     }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IncomingHandler incomingHandler = new IncomingHandler();
        mMessenger = new Messenger(incomingHandler);
        doBindService();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gauge, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mGauge = view.findViewById(R.id.gauge);
        mParameterView = view.findViewById(R.id.parameter_name);

        mParameterView.setText(mZone.getName());

        DataThread dataThread = new DataThread();
        dataThread.start();



    }

    private void updateUi(ObdData obdData){
        if(obdData == null){
            //TO-DO: handle this case
            return;
        }

        mGauge.setMinMaxSpeed(obdData.getMinimum(), obdData.getMaximum());
        mGauge.setSpeedAt((Float.valueOf(obdData.getValue())));
        mGauge.setUnit(obdData.getResultUnit());
        mParameterView.setText(obdData.getName());

    }

    private ObdData getData(){


        return null;
    }


    Handler mainHandler;
    private class DataThread extends Thread {

        @Override
        public void run(){
            Looper.prepare();
            mainHandler = new Handler();
            mainHandler.post(dataRunnable);
            Looper.loop();
        }
    }

    Runnable dataRunnable = new Runnable() {
        @Override
        public void run() {
            ObdData obdData = getData();

            //run on Ui Thread
            new Handler(Looper.getMainLooper()).postAtFrontOfQueue(()
                    ->  updateUi(obdData));

            mainHandler.removeCallbacks(dataRunnable);
            mainHandler.postDelayed(dataRunnable, DATA_REFRESH_RATE); //Update data refresh rate

        }
    };
    @Override
    public void onDestroy(){
        super.onDestroy();
        //TO-DO: interrupt thread
    }

    private void doBindService(){
        Intent intent = new Intent(getContext(), DashObdService.class);
        boolean result = getContext().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        if(result){
            if(BuildConfig.DEBUG)Log.v(TAG, "Binding with DashObdService was successful");
            mIsBound = true;

        }else{
            if(BuildConfig.DEBUG)Log.v(TAG, "Binding with DashObdService failed.");
            mIsBound = false;
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mServiceMessenger = new Messenger(service);

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
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private class IncomingHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case MSG_RECEIVED_DATA:
                    Log.v(TAG, "::IncomingHandler => data received" + ((ObdData) msg.obj).getResultUnit());
                    if(false){//check if data is the one this fragment is displaying
                        updateUi((ObdData) msg.obj);
                    }
            }
        }
    }


}
