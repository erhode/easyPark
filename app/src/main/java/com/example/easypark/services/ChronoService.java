package com.example.easypark.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.easypark.activities.MapsActivity;

public class ChronoService extends Service {
    private static final String TAG = MapsActivity.class.getSimpleName();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "ticketList: service started, duree:"+intent.getStringExtra("duree"));
        return Service.START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }
}
