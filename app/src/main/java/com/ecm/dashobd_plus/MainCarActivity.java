package com.ecm.dashobd_plus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.google.android.apps.auto.sdk.CarActivity;
import com.google.android.apps.auto.sdk.MenuAdapter;
import com.google.android.apps.auto.sdk.MenuAdapterCallback;
import com.google.android.apps.auto.sdk.MenuController;
import com.google.android.apps.auto.sdk.MenuItem;
import com.google.android.apps.auto.sdk.m;

public class MainCarActivity extends CarActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_car);


        FragmentManager fm = getSupportFragmentManager();
        Fragment currentFragment;
        HomeFragment homeFragment = new HomeFragment();

        fm.beginTransaction()
                .add(R.id.fragment_container, homeFragment, HomeFragment.class.getSimpleName())
                .hide(homeFragment).commit();


        fm.beginTransaction().show(homeFragment).commit();
        










    }
}