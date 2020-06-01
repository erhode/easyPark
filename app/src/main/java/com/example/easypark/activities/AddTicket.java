package com.example.easypark.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.easypark.R;
import com.example.easypark.classes.DBHandler;
import com.example.easypark.classes.MyLocationAddress;
import com.example.easypark.classes.Ticket;
import com.example.easypark.classes.Time;

import java.util.Calendar;
import java.util.Date;

public class AddTicket extends AppCompatActivity {

    TextView txtv_dateValue, txtv_TimeStartValue, txtv_TimeEndValue, txtv_dureeValue;
    Button btn_selectDuration, btn_validate, btn_cancel;

    Ticket mTicket;
    Context ctx;

    int mHour, mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ticket);
        ctx = this;

        txtv_dateValue = findViewById(R.id.txtv_dateValue);
        txtv_TimeStartValue = findViewById(R.id.txtv_TimeStartValue);
        btn_selectDuration = findViewById(R.id.btn_selectDuration);
        txtv_TimeEndValue = findViewById(R.id.txtv_TimeEndValue);
        txtv_dureeValue = findViewById(R.id.txtv_dureeValue);

        btn_validate = findViewById(R.id.btn_validate);
        btn_cancel = findViewById(R.id.btn_cancel);

        mTicket = new Ticket();

        txtv_dateValue.setText(mTicket.getDate());
        txtv_TimeStartValue.setText(mTicket.getHeureDebut());

        btn_selectDuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //time that timePicker will show when open
                final Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(ctx, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar currentTime = Calendar.getInstance();
                        int currentHour = currentTime.get(Calendar.HOUR_OF_DAY); //In 24h Format
                        int currentMinute = currentTime.get(Calendar.MINUTE);
                        Log.d("timeeuuh", "currentTime getTime, hour: "+currentHour+", Minute: "+currentMinute);

                        //time selected + currentSecond
                        txtv_TimeEndValue.setText(String.format("%02d:%02d:%02d", hourOfDay, minute, 0));
                        mTicket.setHeureFin(hourOfDay+":"+minute+":00");

                        Log.d("timeeuuh", "onTimeSet: hour:"+hourOfDay+", minute: "+minute);

                        int duration = Time.getDuration((currentHour*60 + currentMinute), (hourOfDay*60 + minute)); //in minutes

                        if (duration < 0) {
                            txtv_TimeEndValue.setTextColor(Color.parseColor("#ff0000"));
                            txtv_dureeValue.setTextColor(Color.parseColor("#ff0000"));

                            btn_validate.setEnabled(false);
                            btn_validate.setBackgroundResource(R.color.disabled);
                        } else {
                            txtv_TimeEndValue.setTextColor(Color.parseColor("#000000"));
                            txtv_dureeValue.setTextColor(Color.parseColor("#000000"));

                            btn_validate.setEnabled(true);
                            btn_validate.setBackgroundResource(R.color.validate);
                        }

                        int hourDuration = duration / 60;
                        int minDuration = duration % 60;
                        int secDuration = 0;

                        mTicket.setDuree(String.format("%02d:%02d:%02d", hourDuration, minDuration, secDuration));
                        txtv_dureeValue.setText(mTicket.getDuree());
                        Log.d("timeeuuh", "duration in hour:"+hourDuration+", in minute: "+minDuration);

                        Log.d("timeeuuh", "duration park: "+duration);
                    }
                }, mHour, mMinute, true);

                timePickerDialog.show();
            }
        });

        btn_validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ticket", mTicket.toString());

                MainActivity.mDataBaseHandler.addTicket(mTicket);

                Log.d("ticketList", "mticket:"+mTicket.toString());

                Intent intent = new Intent(ctx, MainActivity.class);
                intent.putExtra("ticket", mTicket);
                intent.putExtra("startChrono", "true");
                startActivity(intent);
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, MainActivity.class);
                intent.putExtra("startChrono", "false");
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        try {
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            mTicket.setLongitude(location.getLongitude());
            mTicket.setLatitude(location.getLatitude());
            MyLocationAddress adr = new MyLocationAddress(ctx,location.getLongitude(),location.getLatitude() );

        } catch (SecurityException e) {
           Log.d("AddTicket", "auth gps refused: "+e.getMessage());
        }
    }
}
