package com.ecm.dashobd_plus;



import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.apps.auto.sdk.CarActivity;
import com.google.android.apps.auto.sdk.CarActivityService;


public class CarService extends CarActivityService {


    private static String TAG = "CarActivityService";


    public Class<? extends CarActivity> getCarActivity() {




       /* if(isMyServiceRunning(CanLibService.class)){
            Log.v(TAG, "CanLibService is already running");
        }else{
            Log.v(TAG, "CanLibService starting...");
            Intent intent = new Intent(this, CanLibService.class);
            startService(intent);

        }*/




        return MainCarActivity.class;
    }




    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
