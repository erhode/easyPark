package com.example.easypark.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.easypark.R;
import com.example.easypark.classes.Avis;
import com.example.easypark.classes.AvisAdapter;
import com.example.easypark.classes.AvisDbHandler;
import com.example.easypark.classes.Ticket;
import com.example.easypark.classes.TicketAdapter;
import com.example.easypark.classes.TicketDbHandler;

import java.util.ArrayList;

public class AvisActivity extends AppCompatActivity {
    private static final String TAG = "AvisActivity";
    public static AvisDbHandler mAVisDataBaseHandler;
    Context ctx;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avis);

        ctx = this;
        mAVisDataBaseHandler = new AvisDbHandler(ctx);

        findViewById(R.id.btn_addAvis).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, AddAvisActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btn_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AvisActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        RecyclerView recyclerView = findViewById(R.id.recycler_avis);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        ArrayList<Avis> avisList =  MainActivity.mDataBaseHandler.getAllAvis();
        RecyclerView.Adapter adapter = new AvisAdapter(avisList);
        recyclerView.setAdapter(adapter);

        for (Avis avis:avisList) {
            Log.d(TAG, "avis: "+avis.toString());
        }


    }
}
