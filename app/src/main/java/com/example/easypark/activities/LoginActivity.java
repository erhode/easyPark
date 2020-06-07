package com.example.easypark.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.easypark.R;
import com.example.easypark.request.SingletonRequestQueue;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    Button btn_validate, btn_cancel;
    EditText edtx_username, edtx_pwd;
    Context ctx;
    TextView txtv_newUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_validate = findViewById(R.id.btn_validate);
        btn_cancel = findViewById(R.id.btn_cancel);
        edtx_username = findViewById(R.id.edtx_username);
        edtx_pwd = findViewById(R.id.edtx_pwd);
        txtv_newUser = findViewById(R.id.txtv_newUser);
        ctx = this;

        //Clean intent
        getIntent().removeExtra("startChrono");
        getIntent().removeExtra("ticket");

        edtx_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkFields();
            }
        });


        edtx_pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkFields();
            }
        });

       btn_validate.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               btn_validate.setEnabled(false);
               String loginUrl = "https://iga-easy-park.herokuapp.com/login";

               Map<String, String> data = new HashMap<>();
               data.put("username", edtx_username.getText().toString());
               data.put("password", edtx_pwd.getText().toString());

               // Request a string response from the provided URL.
               JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, loginUrl, new JSONObject(data), new Response.Listener<JSONObject>() {
                   @Override
                   public void onResponse(JSONObject response) {
                       Toast.makeText(ctx, "Bienvenue: "+edtx_username.getText().toString(), Toast.LENGTH_SHORT).show();

                       MainActivity.userLogin = edtx_username.getText().toString();
                       Intent intent = new Intent(ctx, MainActivity.class);
                       intent.putExtra("user", edtx_username.getText().toString());
                       startActivity(intent);
                   }
               }, new Response.ErrorListener() {
                   @Override
                   public void onErrorResponse(VolleyError error) {
                       btn_validate.setEnabled(true);
                       ((TextView)findViewById(R.id.activity_title)).setText("That didn't work!"+error);
                       Log.d("volleyerror", "onErrorResponse: "+error);
                   }
               }){
                   @Override
                   public Map<String, String> getHeaders() {
                       Map<String, String>  headers = new HashMap<>();

                       return headers;
                   }
                   @Override
                   protected Map<String, String> getParams()
                   {
                       Map<String, String>  params = new HashMap<>();

                       return params;
                   }
               };
                // Add the request to the RequestQueue.
               SingletonRequestQueue.getInstance(ctx).getRequestQueue().add(jsonRequest);
           }
       });

       btn_cancel.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               finish();
           }
       });

        txtv_newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, SignUpActivity.class);
                startActivity(intent);
            }
        });

    }

    private void checkFields() {
        String username = edtx_username.getText().toString();
        String password = edtx_username.getText().toString();

        if (username.matches("") || password.matches("")) {
            btn_validate.setEnabled(false);
            btn_validate.setBackgroundResource(R.color.disabled);
        } else {
            btn_validate.setEnabled(true);
            btn_validate.setBackgroundResource(R.color.validate);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}
