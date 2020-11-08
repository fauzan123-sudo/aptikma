package com.example.aptikma;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.aptikma.helper.AppController;
import com.example.aptikma.helper.SessionManager;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    private TextInputEditText Email, password;
    private Button btn_login;
    private ProgressBar loading;
    private static String URL_LOGIN = Constans.BaseUrl + "login.php";
    SessionManager sessionManager;
    public final static String TAG_ID = "id";
    public final static String TAG_USERNAME = "email";
    SharedPreferences sharedpreferences;
    private static final String TAG_SUCCESS = "success";
    Boolean session = false;
    int success;
    TextView idne,ne;
    String id, email;
    public static final String my_shared_preferences = "my_shared_preferences";
    public static final String session_status = "session_status";
    ConnectivityManager conMgr;
    String tag_json_obj = "json_obj_req";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sessionManager = new SessionManager(this);
        idne = findViewById(R.id.idn);
        ne = findViewById(R.id.ne);
        // Session manager
        loading     = findViewById(R.id.loading);
        Email       = findViewById(R.id.email);
        password    = findViewById(R.id.password);
        btn_login   = findViewById(R.id.btn_login);


        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection",
                        Toast.LENGTH_LONG).show();
            }
        }

        // Cek session login jika TRUE maka langsung buka Beranda
        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session           = sharedpreferences.getBoolean(session_status, false);
        id                = sharedpreferences.getString(TAG_ID, null);
        email             = sharedpreferences.getString(TAG_USERNAME, null);

        if (session) {
            Intent intent = new Intent(Login.this, MainActivity.class);
            intent.putExtra(TAG_ID, id);
            intent.putExtra(TAG_USERNAME, email);
            finish();
            startActivity(intent);
        }

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mEmail = Email.getText().toString().trim();
                String mPass = password.getText().toString().trim();

                if (!mEmail.isEmpty() || !mPass.isEmpty()) {
                    if (conMgr.getActiveNetworkInfo() != null
                            && conMgr.getActiveNetworkInfo().isAvailable()
                            && conMgr.getActiveNetworkInfo().isConnected()) {
                        Login(mEmail, mPass);
                    } else {
                        Email.setError("Please insert email");
                        password.setError("Please insert password");
                    }
                }
            }
        });



    }

    private void Login(final String email, final String password) {

        loading.setVisibility(View.VISIBLE);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jObj = new JSONObject(response);
                            success = jObj.getInt(TAG_SUCCESS);

                            if (success == 1) {

                                String name  = jObj.getString("nama").trim();
                                String email = jObj.getString("email").trim();
                                String id    = jObj.getString("id").trim();
                                String image = jObj.getString("image").trim();

                                sessionManager.createSession(name, email, id);



                                // menyimpan login ke session
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putBoolean(session_status, true);
                                editor.putString(TAG_ID, id);

                                editor.commit();

                                Intent intent = new Intent(Login.this, MainActivity.class);


                                startActivity(intent);
                                finish();

                                loading.setVisibility(View.GONE);


                            }else{
                                Toast.makeText(Login.this, "periksa kembali username atau password anda", Toast.LENGTH_SHORT).show();
                                loading.setVisibility(View.GONE);
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                            loading.setVisibility(View.GONE);
                            btn_login.setVisibility(View.VISIBLE);
                            Toast.makeText(Login.this, "Error " +e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.setVisibility(View.GONE);
                        btn_login.setVisibility(View.VISIBLE);
                        Toast.makeText(Login.this, "Error " +error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", email);
                params.put("password", password);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest, tag_json_obj);

    }
}