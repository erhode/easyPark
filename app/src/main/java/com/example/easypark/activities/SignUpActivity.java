
package com.example.easypark.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.easypark.R;
import com.example.easypark.classes.Tools;
import com.example.easypark.request.SingletonRequestQueue;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    Button btn_validate, btn_cancel;
    EditText edtx_username, edtx_email, edtx_pwd, edtx_repwd;
    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        btn_validate = findViewById(R.id.btn_validate);
        btn_cancel = findViewById(R.id.btn_cancel);
        edtx_username = findViewById(R.id.edtx_username);
        edtx_email = findViewById(R.id.edtx_email);
        edtx_pwd = findViewById(R.id.edtx_pwd);
        edtx_repwd = findViewById(R.id.edtx_repwd);
        ctx = this;

        btn_validate.setOnClickListener(new View.OnClickListener() {
            String username = edtx_username.getText().toString();
            String email = edtx_email.getText().toString();
            String pwd = edtx_pwd.getText().toString();
            String repwd = edtx_repwd.getText().toString();
            
            @Override
            public void onClick(View v) {
                if (username.isEmpty() || email.isEmpty() || pwd.isEmpty() || repwd.isEmpty()) {
                    Toast.makeText(ctx, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                } else  if (!pwd.equals(repwd)) {
                    Toast.makeText(ctx, "les mots de passe ne correspondent pas", Toast.LENGTH_SHORT).show();
                } else  if (!Tools.isEmailValid(email)) {
                    Toast.makeText(ctx, "Veuillez insérer un email valide", Toast.LENGTH_SHORT).show();
                } else {
                    String loginUrl = "https://iga-easy-park.herokuapp.com/register";
                    // Request a string response from the provided URL.
                    Map<String, String> data = new HashMap<>();
                    data.put("username", username);
                    data.put("password", pwd);
                    data.put("repassword", repwd);

                    // Request a string response from the provided URL.
                    JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, loginUrl, new JSONObject(data), new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(ctx, "Utilisateur crée", Toast.LENGTH_SHORT).show();
                            MainActivity.userLogin = username;
                            Intent intent = new Intent(ctx, MainActivity.class);
                            startActivity(intent);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            btn_validate.setEnabled(true);
                            Toast.makeText(ctx, "Une erreur est survenue veuillez réessayer plus tard", Toast.LENGTH_SHORT).show();
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
