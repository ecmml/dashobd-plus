package com.ecm.dashobd_plus;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.support.car.Car;
import android.support.car.CarInfoManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ecm.dashobd_plus.carinput.CarInputManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;


public class ZoneSettingsFragment extends CarFragment {



    private FloatingActionButton backBtn;

    private TextInputEditText minField;

    private TextInputEditText maxField;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_zone_settings, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        backBtn = view.findViewById(R.id.back_btn);
        minField = view.findViewById(R.id.min_field);
        maxField = view.findViewById(R.id.max_field);
        FragmentManager fm = getParentFragmentManager();
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fm.beginTransaction()
                        .replace(R.id.fragment_container, new SettingsFragment())
                        .commit();
            }
        });

        minField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CarInputManager.getInstance().startInput(minField);
            }
        });

        maxField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CarInputManager.getInstance().startInput(maxField);
            }
        });
    }
}