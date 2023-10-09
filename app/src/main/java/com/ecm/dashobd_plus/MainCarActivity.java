package com.ecm.dashobd_plus;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;

import com.ecm.dashobd_plus.models.Zone;
import com.ecm.dashobd_plus.models.ZonesViewModel;
import com.ecm.dashobd_plus.services.DashObdService;
import com.google.android.apps.auto.sdk.CarActivity;

import java.util.ArrayList;

public class MainCarActivity extends CarActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_car);





        FragmentManager fm = getSupportFragmentManager();
        Fragment currentFragment;
        ArrayList<Zone> zones = new ArrayList<>();
        Zone zone = new Zone();
        zone.setName("Speed");
        zones.add(zone);
        zone = new Zone();
        zone.setName("RPM");
        zones.add(zone);
        zone = new Zone();
        zone.setName("AFR");
        zones.add(zone);

        ZonesViewModel.setZones(zones);
        DisplayFragment displayFragment = new DisplayFragment(ZonesViewModel.getZones());

        fm.beginTransaction()
                .add(R.id.fragment_container, displayFragment, DisplayFragment.class.getSimpleName())
                .hide(displayFragment).commit();


        fm.beginTransaction().show(displayFragment).commit();







        initDashObdService();



    }


    private void initDashObdService(){

        Intent intent = new Intent(this, DashObdService.class);
        startService(intent);
    }
}