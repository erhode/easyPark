package com.example.easypark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MapsActivity.class.getSimpleName();

    Button btn_parkmetre, btn_avis, btn_startTimer, btn_stopTimer;
    ImageButton btn_home, btn_maps;
    TextView txtv_timer;
    Spinner spn_duration;
    Context ctx;
    boolean mContinueTimer = true;

    private int mDurationSelected; //hours
    private int mDurationInHour;
    private int mDurationInMinute;
    private int mDurationInSeconds;

    private FusedLocationProviderClient mFusedLocationClient;

    private boolean mLocationPermissionGranted;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ctx = this;
        btn_maps = findViewById(R.id.btn_maps);
        btn_startTimer = findViewById(R.id.btn_startTimer);
        btn_stopTimer = findViewById(R.id.btn_stopTimer);
        txtv_timer = findViewById(R.id.txtv_timer);

        btn_maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, MapsActivity.class);
                startActivity(intent);
            }
        });

        spn_duration = findViewById(R.id.spn_Duration);
        addItemToSpinner(spn_duration);

        spn_duration.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemSelected: "+ parent.getItemAtPosition(position).toString());
                Log.d(TAG, "onItemSelected: position"+ position);

                mDurationSelected = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_startTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (mDurationSelected > 0){
                    mDurationInHour = mDurationSelected - 1;
                    mDurationInMinute = 59;
                    mDurationInSeconds = 60;
                    mContinueTimer = true;
                    btn_startTimer.setEnabled(false);
                    btn_stopTimer.setEnabled(true);
                    startTimer();
                }
            }
        });

        btn_stopTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContinueTimer = false;
                txtv_timer.setText("00:00:00");
                btn_startTimer.setEnabled(true);
                btn_stopTimer.setEnabled(false);
            }
        });

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    private void startTimer() {
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if(mContinueTimer) {
                    if (mDurationInSeconds > 0) {
                        mDurationInSeconds--;
                    } else if (mDurationInMinute > 0) {
                        mDurationInMinute--;
                        mDurationInSeconds = 60;
                    } else if (mDurationInHour > 0) {
                        mDurationInHour--;
                        mDurationInMinute = 60;
                        mDurationInSeconds = 60;
                    } else {
                        Log.d(TAG, "run: timer finished!!");
                        mContinueTimer = false;
                    }

                    txtv_timer.setText(String.format("%02d:%02d:%02d", mDurationInHour, mDurationInMinute, mDurationInSeconds));
                    startTimer();
                }
            }
        };
        handler.postDelayed(runnable, 10);
    }

    private void addItemToSpinner(Spinner spn) {
        List<String> list = new ArrayList<>();

        list.add("");
        list.add("1 heure");
        list.add("2 heures");
        list.add("3 heures");
        list.add("4 heures");
        list.add("5 heures");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn.setAdapter(dataAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();

        getLocationPermission();
    }

    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            Log.d(TAG, "getLocationPermission: je n'ai pas la permission");
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: callback-----------");
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                    //updateLocationUI();
                    getDeviceLocation();
                }
            }
        }
    }

    private void getDeviceLocation() {
        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>(){

            @Override
            public void onSuccess(Location location) {
                //Last known location, can be null
                if (location != null) {
                    Log.d("location", "latitude: "+location.getLatitude());
                    Log.d("location", "longitude: "+location.getLongitude());
                }
            }
        });
    }

}
