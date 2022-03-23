package com.example.simplegpstracking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int DEFAULT_INTERVEL_SETTING = 30;
    public static final int FASTEST_INTERVAL_SETTING = 5;

    TextView latitudeLabel, latitudeValue, longitudeLabel, longitudeValue, altitudeLabel,
    altitudeValue, accuracyLabel, accuracyValue, speedLabel, speedValue, sensorLabel,
    sensorValue, updatesLabel, updatesValue, addressLabel, addressValue, wayPointsLabel, wayPointsValue;


    Button newWayPointBtn, wayPointsListBtn, showMapBtn;

    Switch locationUpdates, gps;

    View divider;

    boolean updateBtn = false;

    Location currentLocation;

    List<Location> savedLocations;

    LocationCallback locationCallback;

    // class for all the settings related to FusedLocationProviderClient
    // to configure the location's options
    LocationRequest locationRequest;

    // Google API for location tracking services
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        latitudeLabel = findViewById(R.id.tv_labellat);
        latitudeValue = findViewById(R.id.tv_lat);

        longitudeLabel = findViewById(R.id.tv_labellon);
        longitudeValue = findViewById(R.id.tv_lon);

        altitudeLabel = findViewById(R.id.tv_labelaltitude);
        altitudeValue = findViewById(R.id.tv_altitude);

        accuracyLabel = findViewById(R.id.tv_labelaccuracy);
        accuracyValue = findViewById(R.id.tv_accuracy);

        speedLabel = findViewById(R.id.tv_labelspeed);
        speedValue = findViewById(R.id.tv_speed);

        sensorLabel = findViewById(R.id.tv_labelsensor);
        sensorValue = findViewById(R.id.tv_sensor);

        updatesLabel = findViewById(R.id.tv_labelupdates);
        updatesValue = findViewById(R.id.tv_updates);

        locationUpdates = findViewById(R.id.sw_locationsupdates);

        gps = findViewById(R.id.sw_gps);

        addressLabel = findViewById(R.id.tv_labeladdress);
        addressValue = findViewById(R.id.tv_address);

        divider = findViewById(R.id.divider);

        wayPointsLabel = findViewById(R.id.tv_labelWayPoints);
        wayPointsValue = findViewById(R.id.tv_wayPoints);

        newWayPointBtn = findViewById(R.id.newWayPointBtn);
        wayPointsListBtn = findViewById(R.id.wayPointsListBtn);

        showMapBtn = findViewById(R.id.showMapBtn);


        // initializing the LocationRequest class instance and its properties
        locationRequest = LocationRequest.create();

        // intervals in milliseconds
        locationRequest.setInterval(1000 * DEFAULT_INTERVEL_SETTING);
        locationRequest.setFastestInterval(1000 * FASTEST_INTERVAL_SETTING);

        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);


        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);

                Location location = locationResult.getLastLocation();
                updateUIValues(location);
            }
        };

        newWayPointBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // get the current location and add it in the saved locations list

                MySimpleGPSTracking mySimpleGPSTracking = (MySimpleGPSTracking) getApplicationContext();

                savedLocations = mySimpleGPSTracking.getLocationList();

                savedLocations.add(currentLocation);

            }
        });

        wayPointsListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, ShowSavedLocationsList.class);
                startActivity(intent);
            }
        });


        showMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, ShowMap.class);
                startActivity(intent);
            }
        });


        gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(gps.isChecked())
                {
                    // GPS uses the highest accuracy
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    sensorValue.setText("Using GPS");

                }

                else
                {
                    // Using the cell towers and the WiFi
                    locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                    sensorValue.setText("Using Cell Tower + Wifi");
                }



            }
        });

        locationUpdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(locationUpdates.isChecked())
                {
                    startLocationUpdates();
                }

                else
                {
                    stopLocationUpdates();
                }
            }
        });

        updateGPS();

    }

    private void stopLocationUpdates()
    {
        updatesValue.setText("Location is not being tracked");

        latitudeValue.setText("Not available");
        longitudeValue.setText("Not available");
        altitudeValue.setText("Not available");
        speedValue.setText("Not available");
        addressValue.setText("Not available");
        accuracyValue.setText("Not available");
        sensorValue.setText("Not available");

        fusedLocationProviderClient.removeLocationUpdates(locationCallback);

    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates()
    {
        if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            updatesValue.setText("Location is being tracked");
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
            updateGPS();
        }

        else
        {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            }

        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1)
        {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                updateGPS();
            }

            else
            {

                Toast.makeText(this, "This application requires permission ", Toast.LENGTH_SHORT).show();
                finish();

            }


        }
    }

    @SuppressLint("MissingPermission")
    private void updateGPS()
    {

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // asking for the user's permission to get the device's location
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {

            // permission granted by the user
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location)
                {
                    // setting the UI elements with their obtained values
                    updateUIValues(location);

                    currentLocation = location;

                }
            });

        }
        else
        {
            // permission not granted by the user
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            }
        }

    }

    private void updateUIValues(Location location)
    {
        latitudeValue.setText(String.valueOf(location.getLatitude()));
        longitudeValue.setText(String.valueOf(location.getLongitude()));

        if(location.hasAccuracy())
        {
            accuracyValue.setText(String.valueOf(location.getAccuracy()));

        }
        else
        {
            accuracyValue.setText("Not available");
        }

        if(location.hasAltitude())
        {
            altitudeValue.setText(String.valueOf(location.getAltitude()));

        }
        else
        {
            altitudeValue.setText("Not available");
        }

        if(location.hasSpeed())
        {
            speedValue.setText(String.valueOf(location.getSpeed()));

        }
        else
        {
            speedValue .setText("Not available");
        }

        Geocoder geocoder= new Geocoder(MainActivity.this);

        try
        {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),1);
            addressValue.setText(addresses.get(0).getAddressLine(0));
        }

        catch (Exception e)
        {
            addressValue.setText("Not available");
        }


        MySimpleGPSTracking mySimpleGPSTracking = (MySimpleGPSTracking) getApplicationContext();

        savedLocations = mySimpleGPSTracking.getLocationList();

        wayPointsValue.setText(String.valueOf(savedLocations.size()));



    }
}