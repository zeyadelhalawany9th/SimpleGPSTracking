package com.example.simplegpstracking;

import android.app.Application;
import android.location.Location;

import java.util.ArrayList;
import java.util.List;

public class MySimpleGPSTracking extends Application {

    private static MySimpleGPSTracking mySimpleGPSTracking;

    @Override
    public void onCreate() {
        super.onCreate();

        mySimpleGPSTracking = this;
        locationList = new ArrayList<>();
    }

    private List<Location> locationList;


    public List<Location> getLocationList() {
        return locationList;
    }

    public void setLocationList(List<Location> locationList) {
        this.locationList = locationList;
    }


    public MySimpleGPSTracking getInstance()
    {
        return mySimpleGPSTracking;
    }


}
