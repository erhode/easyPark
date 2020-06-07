package com.example.easypark.services;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.easypark.R;
import com.example.easypark.activities.MapsActivity;

public class ChronoService extends Service {
    private static final String TAG = MapsActivity.class.getSimpleName();
    private boolean mContinueTimer = true;
    private int mDurationInHour;
    private int mDurationInMinute;
    private int mDurationInSeconds;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mDurationInHour = intent.getIntExtra("mDurationInHour", 0);
        mDurationInMinute = intent.getIntExtra("mDurationInMinute", 0);
        mDurationInSeconds = intent.getIntExtra("mDurationInSeconds", 0);

        Log.d(TAG, "ticketList: service started, duree: hour:"+mDurationInHour+", min:"+mDurationInMinute+", sec:"+mDurationInSeconds);
        //startTimer();

        ChronoThread chronoThread = new ChronoThread();
        mContinueTimer = true;
        chronoThread.run();
        return Service.START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "service onBind");
        return null;
    }

    class ChronoThread implements Runnable {
        @Override
        public void run() {
            Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (mContinueTimer) {
                        if (mDurationInSeconds > 0) {
                            mDurationInSeconds--;
                        } else if (mDurationInMinute > 0) {
                            mDurationInMinute--;
                            mDurationInSeconds = 60;
                        } else if (mDurationInHour > 0) {
                            mDurationInHour--;
                            mDurationInMinute = 59;
                            mDurationInSeconds = 60;
                        } else {
                            Log.d(TAG, "run: timer finished!!");
                            mContinueTimer = false;
                        }

                        String DurationLeft = String.format("%02d:%02d:%02d", mDurationInHour, mDurationInMinute, mDurationInSeconds);

                        sendMessageToActivity(mDurationInHour, mDurationInMinute, mDurationInSeconds);
                        run();
                    }
                }
            };
            handler.postDelayed(runnable, 10); //todo 10ms just for test
        }
    }

    private void startTimer() {
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (mContinueTimer) {
                    if (mDurationInSeconds > 0) {
                        mDurationInSeconds--;
                    } else if (mDurationInMinute > 0) {
                        mDurationInMinute--;
                        mDurationInSeconds = 60;
                    } else if (mDurationInHour > 0) {
                        mDurationInHour--;
                        mDurationInMinute = 59;
                        mDurationInSeconds = 60;
                    } else {
                        Log.d(TAG, "run: timer finished!!");
                        mContinueTimer = false;
                    }

                    String DurationLeft = String.format("%02d:%02d:%02d", mDurationInHour, mDurationInMinute, mDurationInSeconds);

                    sendMessageToActivity(mDurationInHour, mDurationInMinute, mDurationInSeconds);
                    startTimer();
                }
            }
        };
        handler.postDelayed(runnable, 10); //todo 10ms just for test
    }

    private void sendMessageToActivity(int mDurationInHour, int mDurationInMinute, int mDurationInSeconds) {
        Intent intent = new Intent();
        // You can also include some extra data.
        intent.putExtra("mDurationInHour", mDurationInHour);
        intent.putExtra("mDurationInMinute", mDurationInMinute);
        intent.putExtra("mDurationInSeconds", mDurationInSeconds);
        intent.setAction("Duration");
        sendBroadcast(intent);
    }

}
