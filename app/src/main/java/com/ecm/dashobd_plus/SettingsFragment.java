package com.ecm.dashobd_plus;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ecm.dashobd_plus.models.ZonesViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class SettingsFragment extends CarFragment {

    FloatingActionButton homeBtn;

    public SettingsFragment() {

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        homeBtn = view.findViewById(R.id.home_btn);

        FragmentManager fm = getParentFragmentManager();

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fm.beginTransaction()
                        .replace(R.id.fragment_container, new DisplayFragment(ZonesViewModel.getZones()))
                        .commit();
            }
        });
    }
}