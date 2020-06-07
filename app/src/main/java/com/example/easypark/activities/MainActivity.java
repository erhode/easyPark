package com.example.easypark.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easypark.R;
import com.example.easypark.classes.TicketDbHandler;
import com.example.easypark.classes.Ticket;
import com.example.easypark.classes.TicketAdapter;
import com.example.easypark.services.ChronoService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MapsActivity.class.getSimpleName();

    public static TicketDbHandler mDataBaseHandler;

    private Button btn_newTicket;
    private ImageButton btn_maps, btn_social, btn_avis;
    private TextView  txtv_durationTitle;
    //private Spinner spn_duration;
    private Context ctx;
    private boolean mContinueTimer = true;

    public static Ticket mTicket;

    Location mStartLocation, mEndLocation, mCurrentLocation;

    public static String userLogin;

    //manage Timer
    private int mDurationSelected; //in hours
    private int mDurationInHour;
    private int mDurationInMinute;
    private int mDurationInSeconds;

    private int mDurationCalculated;

    private FusedLocationProviderClient mFusedLocationClient;

    private boolean mLocationPermissionGranted;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mDurationInHour =(int)intent.getExtras().get("mDurationInHour");
            mDurationInMinute =(int)intent.getExtras().get("mDurationInMinute");
            mDurationInSeconds =(int)intent.getExtras().get("mDurationInSeconds");

            Log.d("service", "onReceive: hour"+intent.getExtras().get("mDurationInHour"));
            Log.d("service", "onReceive: min"+intent.getExtras().get("mDurationInMinute"));
            Log.d("service", "onReceive: sec"+intent.getExtras().get("mDurationInMinute"));

            if (mTicket!=null) {
                mTicket.setDureeInHour(mDurationInHour);
                mTicket.setDureeInMin(mDurationInMinute);
                mTicket.setDureeSec(mDurationInSeconds);

                String DurationLeft = String.format("%02d:%02d:%02d", mDurationInHour, mDurationInMinute, mDurationInSeconds);
                ((TextView)findViewById(R.id.txtv_durationTitle_value)).setText(DurationLeft);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ctx = this;
        mDataBaseHandler = new TicketDbHandler(ctx);

        btn_maps = findViewById(R.id.btn_maps);
        btn_social = findViewById(R.id.btn_social);
        btn_newTicket = findViewById(R.id.btn_newTicket);
        btn_avis = findViewById(R.id.btn_avis);

        mTicket = null;


        Intent intent = new Intent();
        if (intent.getStringExtra("user") != null && !intent.getStringExtra("user").isEmpty()) {
            userLogin =  intent.getStringExtra("user");
        }

        btn_newTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, AddTicket.class);
                startActivity(intent);
            }
        });

        btn_maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(ctx, MapsActivity.class);
            startActivity(intent);
            }
        });

        btn_avis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userLogin== null || userLogin.isEmpty()) {
                    Toast.makeText(ctx, "Veuillez vous connecter pour consulter les avis", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(ctx, AvisActivity.class);
                    startActivity(intent);
                }
            }
        });

        btn_social.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, LoginActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btn_cancel_ticket).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTicket=null;
                findViewById(R.id.current_ticket).setVisibility(View.INVISIBLE);
                findViewById(R.id.no_current_ticket).setVisibility(View.VISIBLE);
            }
        });
        //spn_duration = findViewById(R.id.spn_Duration);
        //addItemToSpinner(spn_duration);
/*
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
 */

/*
        btn_startTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (mDurationSelected > 0) {
                mDurationInHour = mDurationSelected - 1;
                mDurationInMinute = 59;
                mDurationInSeconds = 60;
                mContinueTimer = true;
                btn_startTimer.setEnabled(false);
                btn_stopTimer.setEnabled(true);
                startTimer();

                if (mLocationPermissionGranted) {
                    getDeviceLocation();
                    mStartLocation = mCurrentLocation;
                } else {
                    getLocationPermission();
                }
            }
            }
        });
*/

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
/*
        btn_selectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(ctx, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Log.d("timeeuuh", "onTimeSet: hour:"+hourOfDay+", minute: "+minute);

                        Calendar currentTime = Calendar.getInstance();
                        int currentHour = currentTime.get(Calendar.HOUR_OF_DAY); //In 24h Format
                        int currentMinute = currentTime.get(Calendar.MINUTE);
                        Log.d("timeeuuh", "currentTime getTime, hour: "+currentHour+", Minute: "+currentMinute);

                        int duration = getDuration((currentHour*60 + currentMinute), (hourOfDay*60 + minute));
                        mDurationCalculated = duration;

                        if ( mDurationCalculated < 0) {
                            txtv_durationTitle.setText(R.string.time_left_error);
                            txtv_durationTitle.setTextColor(Color.parseColor("#ff0000"));
                        } else {
                            ((TextView)findViewById(R.id.txtv_durationTitle)).setText(R.string.time_left);
                            txtv_durationTitle.setTextColor(Color.parseColor("#000000"));
                        }

                        mDurationInHour = mDurationCalculated / 60;
                        mDurationInMinute = mDurationCalculated % 60;
                        mDurationInSeconds = 0;

                        Log.d("timeeuuh", "duration in hour:"+mDurationInHour+", in minute: "+mDurationInMinute);
                        txtv_timer.setText(String.format("%02d:%02d:%02d", mDurationInHour, mDurationInMinute, mDurationInSeconds));

                        Log.d("timeeuuh", "duration park: "+duration);
                    }
                }, mHour, mMinute, true);

                timePickerDialog.show();
            }
        });

 */
    }

    //park duration in minutes
    private int getDuration( int startInMinute, int endInMinute) {
        return endInMinute - startInMinute;
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
                        mEndLocation = mCurrentLocation;
                    }

                    String DurationLeft = String.format("%02d:%02d:%02d", mDurationInHour, mDurationInMinute, mDurationInSeconds);
                    ((TextView)findViewById(R.id.txtv_durationTitle_value)).setText(DurationLeft);

                    if (mTicket!=null) {
                        mTicket.setDureeInHour(mDurationInHour);
                        mTicket.setDureeInMin(mDurationInMinute);
                        mTicket.setDureeSec(mDurationInSeconds);
                    }
                    startTimer();
                }
            }
        };
        handler.postDelayed(runnable, 10); //todo 10ms just for test
    }

    private void addItemToSpinner(Spinner spn) {
        List<String> list = new ArrayList<>();

        list.add("");
        list.add("1 heure");
        list.add("2 heures");
        list.add("3 heures");
        list.add("4 heures");
        list.add("5 heures");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);

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
            Log.d(TAG, "getLocationPermission: I don't have the permission");
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                    //updateLocationUI();
                    //getDeviceLocation();
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
                    mCurrentLocation = location;
                } else {
                    Log.d("location", "no latitude + no longitude ");
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter filt = new IntentFilter("Duration");
        this.registerReceiver(br, filt);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_historique);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        ArrayList<Ticket> ticketList = mDataBaseHandler.getAllContacts();
        RecyclerView.Adapter adapter = new TicketAdapter(ticketList);
        recyclerView.setAdapter(adapter);

        for (Ticket ticket:ticketList) {
            Log.d(TAG, "ticketList: "+ticket.toString());
        }

        String msg = getIntent().getStringExtra("startChrono");
        mTicket = (Ticket)getIntent().getSerializableExtra("ticket"); //récupération du ticket nouvellement créé

        if (mTicket!= null || (msg != null && msg.equals("true"))) {
            Log.d(TAG, "ticketList: start service "+msg);
            displayTicket(mTicket);
            findViewById(R.id.current_ticket).setVisibility(View.VISIBLE);
            findViewById(R.id.no_current_ticket).setVisibility(View.INVISIBLE);
            mDurationInHour = mTicket.getDureeInHour();
            mDurationInMinute = mTicket.getDureeInMin();
            //startTimer();
            startChronoService();
        } else {
            findViewById(R.id.current_ticket).setVisibility(View.INVISIBLE);
            findViewById(R.id.no_current_ticket).setVisibility(View.VISIBLE);
            Log.d(TAG, "ticketList: no start service"+msg);
        }
    }

    private void displayTicket(Ticket ticket) {
        ((TextView)findViewById(R.id.txtv_currentTicket_Date_value)).setText(ticket.getDate());
        ((TextView)findViewById(R.id.txtv_currentTicket_Location_value)).setText("pas encore");
        ((TextView)findViewById(R.id.txtv_start_time_value)).setText(ticket.getHeureDebut());
        ((TextView)findViewById(R.id.txtv_end_time_value)).setText(ticket.getHeureFin());
        ((TextView)findViewById(R.id.txtv_durationTitle_value)).setText(ticket.getDuree());
    }

    private void startChronoService() {
        Intent i= new Intent(ctx, ChronoService.class);
        i.putExtra("mDurationInHour", mTicket.getDureeInHour());
        i.putExtra("mDurationInMinute", mTicket.getDureeInMin());
        i.putExtra("mDurationInSeconds", mTicket.getDureeSec());
        startService(i);
        Log.d(TAG, "startService: demarrage du service");
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(br);
    }

}
