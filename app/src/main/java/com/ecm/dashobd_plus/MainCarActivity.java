package com.ecm.dashobd_plus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.apps.auto.sdk.CarActivity;

public class MainCarActivity extends CarActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_car);
    }
}