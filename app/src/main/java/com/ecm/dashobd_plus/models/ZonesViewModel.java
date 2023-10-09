package com.ecm.dashobd_plus.models;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class ZonesViewModel {
    public static ArrayList<Zone> zones;


    public static ArrayList<Zone> getZones() {
        return zones;
    }

    public static void setZones(ArrayList<Zone> zones) {
        ZonesViewModel.zones = zones;
    }
}
