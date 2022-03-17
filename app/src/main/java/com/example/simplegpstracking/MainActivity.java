package com.example.simplegpstracking;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView latitudeLabel, latitudeValue, longitudeLabel, longitudeValue, altitudeLabel,
    altitudeValue, accuracyLabel, accuracyValue, speedLabel, speedValue, sensorLabel,
    sensorValue, updatesLabel, updatesValue, addressLabel, addressValue;

    Switch locationUpdates, gps;

    View divider;

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


    }
}