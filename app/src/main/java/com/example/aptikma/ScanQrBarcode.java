package com.example.aptikma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ScanQrBarcode extends AppCompatActivity {

    public static TextView result;
    private static final String TAG = ScanQrBarcode.class.getSimpleName();
    int success;
    SessionManager sessionManager;
    String getId, getName;
    private String secan = Constans.BaseUrl + "Seken.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    String tag_json_obj = "json_obj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr_barcode);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom);
        bottomNavigationView.setSelectedItemId(R.id.scan);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.gaji:
                        startActivity(new Intent(getApplicationContext(), Gaji.class ));
                        overridePendingTransition(0,0);
                        break;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class ));
                        overridePendingTransition(0,0);
                        break;
                    case R.id.absen:
                        startActivity(new Intent(getApplicationContext(), Izin.class ));
                        overridePendingTransition(0,0);
                        break;
                    case R.id.scan:
                        break;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), Profile.class ));
                        overridePendingTransition(0,0);
                        break;
                }
                return false;
            }
        });

        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(sessionManager.ID);

        Button Scan = findViewById(R.id.scan);
        TextView menampilkanID = findViewById(R.id.menmapilkanID);
        menampilkanID.setVisibility(View.GONE);
        menampilkanID.setText(getId);
        Button konfirm = findViewById(R.id.konfirm);

        konfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cekInputnya();
            }


        });
        Scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Scan.class));
            }
        });
        result = findViewById(R.id.tv_scanresult_title);
        if (result!=null){
            cekScan();

        }


    }

    private void cekScan() {

            final String hasil_scan = result.getText().toString();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, secan, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jObj = new JSONObject(response);
                        success = jObj.getInt("success");

                        if (success == 1) {
                            String berhasil = jObj.getString("message");
                            Toast.makeText(ScanQrBarcode.this, "berhasil" + berhasil, Toast.LENGTH_LONG).show();
                            Intent i = new Intent(ScanQrBarcode.this, MainActivity.class);
                            startActivity(i);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ScanQrBarcode.this, "isi 0", Toast.LENGTH_LONG).show();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(ScanQrBarcode.this, "Error2 " +error.toString(), Toast.LENGTH_LONG).show();
                        }
                    })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("id_pegawai", getId);
                    params.put("string_qr_code", hasil_scan);
                    Log.d("kesalahan", "disini");
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
    }
    private void cekInputnya() {

        final String hasil_scan = result.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, secan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Check for error node in json
                    if (success == 1) {

                        String message = jObj.getString("message");

                        Log.e("Successfully Login!", jObj.toString());

                        Toast.makeText(ScanQrBarcode.this, "Berhasil Absen", Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), jObj.getString(message), Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(ScanQrBarcode.this, "QR Code salah", Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(),jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();



            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_pegawai", getId);
                params.put("string_qr_code", hasil_scan);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(stringRequest, tag_json_obj);

    }


}