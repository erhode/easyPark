package com.example.easypark.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.easypark.R;
import com.example.easypark.classes.Avis;
import com.example.easypark.classes.Time;

public class AddAvisActivity extends AppCompatActivity {

    Button btn_validate,btn_cancel;

    EditText edtx_avis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_avis);

        btn_validate = findViewById(R.id.btn_validate);
        btn_cancel = findViewById(R.id.btn_cancel);
        edtx_avis = findViewById(R.id.edtx_avis);

        btn_validate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String content = edtx_avis.getText().toString();
                if (content.isEmpty()) {
                    Toast.makeText(AddAvisActivity.this, "veuillez laisser votre avis", Toast.LENGTH_SHORT).show();
                } else {
                    Avis avis = new Avis(content, MainActivity.userLogin, Time.getTodayDate());
                    AvisActivity.mAVisDataBaseHandler.addAvis(avis);
                    Toast.makeText(AddAvisActivity.this, "Merci pour votre avis", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddAvisActivity.this, AvisActivity.class);
                    startActivity(intent);
                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
