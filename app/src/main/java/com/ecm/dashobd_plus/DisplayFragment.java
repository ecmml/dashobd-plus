package com.ecm.dashobd_plus;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ecm.dashobd_plus.models.Zone;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class DisplayFragment extends Fragment {


    ArrayList<Zone> mZones;

    FloatingActionButton settingsBtn;

    public DisplayFragment(ArrayList<Zone> zones) {
        mZones = zones;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_display, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        settingsBtn = view.findViewById(R.id.settings_btn);

        FragmentManager fm = getParentFragmentManager();
        for(int i=0; i<mZones.size(); i++) {
            fm.beginTransaction()
                    .add(R.id.zone_container, new GaugeFragment(mZones.get(i)))
                    .commit();
        }

        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fm.beginTransaction()
                        .replace(R.id.fragment_container, new SettingsFragment())
                        .commit();
            }
        });
    }
}