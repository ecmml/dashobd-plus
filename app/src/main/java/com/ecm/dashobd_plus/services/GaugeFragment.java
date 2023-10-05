package com.ecm.dashobd_plus.services;

import static com.ecm.dashobd_plus.Constants.DATA_REFRESH_RATE;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ecm.dashobd_plus.MainCarActivity;
import com.ecm.dashobd_plus.ObdData;
import com.ecm.dashobd_plus.R;
import com.github.anastr.speedviewlib.PointerSpeedometer;

import org.w3c.dom.Text;

public class GaugeFragment extends Fragment {


    private final String mParameterName;

    private PointerSpeedometer mGauge;

    private TextView           mParameterView;

    private MainCarActivity mActivity;

    public GaugeFragment(@NonNull String parameterName){
        this.mParameterName = parameterName;
        mActivity = (MainCarActivity) getContext();
     }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        for(int i=0; i<DashObdService.getObdDataList().size(); i++){
            ObdData data = DashObdService.getObdDataList().get(i);

            if(data.getName().equals(mParameterName)){
                return data;
            }
        }

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


}
