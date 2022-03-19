package com.example.simplegpstracking;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class ShowSavedLocationsList extends AppCompatActivity {

    ListView savedLocationsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_saved_locations_list);

        savedLocationsList = findViewById(R.id.showWayPointsList);

        List<Location> savedLocations;

        MySimpleGPSTracking mySimpleGPSTracking = (MySimpleGPSTracking) getApplicationContext();
        savedLocations = mySimpleGPSTracking.getLocationList();

        List<String> savedLocationss = new ArrayList<>();
        List<Address> addresses = new ArrayList<>();

        for(int i = 0; i < savedLocations.size(); i++)
        {
            Geocoder geocoder= new Geocoder(ShowSavedLocationsList.this);

            try
            {
                addresses = geocoder.getFromLocation(savedLocations.get(i).getLatitude(), savedLocations.get(i).getLongitude(),1);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            savedLocationss.add(addresses.get(0).getAddressLine(0));
        }


        savedLocationsList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, savedLocationss));
    }
}