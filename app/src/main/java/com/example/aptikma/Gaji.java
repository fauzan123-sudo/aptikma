package com.example.aptikma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.aptikma.helper.AppController;
import com.example.aptikma.helper.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Gaji extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private static final String TAG = MainActivity.class.getSimpleName();
    private ExampleAdapter mExampleAdapter;
    private ArrayList<ExampleItem> mExampleList;
    private RequestQueue mRequestQueue;
    private RecyclerView.Adapter adapter;
    SessionManager sessionManager;
    String getId;
    String tag_json_obj = "json_obj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gaji);
        mRecyclerView = findViewById(R.id.recycler_view);
        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(sessionManager.ID);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mExampleList = new ArrayList<>();
        mRequestQueue = Volley.newRequestQueue(this);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom);
        bottomNavigationView.setSelectedItemId(R.id.gaji);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                parseJSON();
                handler.postDelayed(this, 1000);
            }
        }, 1000);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.gaji:
                        break;
                    case R.id.home:
                        //Beranda
                        startActivity(new Intent(getApplicationContext(), MainActivity.class ));
                        overridePendingTransition(0,0);
                        break;
                    case R.id.absen:
                        //Presensi harian
                        startActivity(new Intent(getApplicationContext(), Izin.class ));
                        overridePendingTransition(0,0);
                        break;
                    case R.id.scan:
                        //Scan
                        startActivity(new Intent(getApplicationContext(), ScanQrBarcode.class ));
                        overridePendingTransition(0,0);
                        break;
                    case R.id.profile:
                        //Profile
                        startActivity(new Intent(getApplicationContext(), Profile.class ));
                        overridePendingTransition(0,0);
                        break;
                }
                return false;
            }
        });



    }
    private void parseJSON() {

        String url = Constans.BaseUrl +"gaji.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("hasil");
                            mExampleList.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject hit     = jsonArray.getJSONObject(i);
                                String creatorName = hit.getString("gaji_pokok");
                                String asuransi    = hit.getString("asuransi");
                                String potongan    = hit.getString("potongan");
                                mExampleList.add(new ExampleItem(creatorName, asuransi, potongan));

                            }
                            mExampleAdapter = new ExampleAdapter(Gaji.this, mExampleList);
                            mRecyclerView.setAdapter(mExampleAdapter);
                            mExampleAdapter.notifyDataSetChanged();



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<>();
                params.put("id_pegawai", getId);
                return params;
            }};
        AppController.getInstance().addToRequestQueue(stringRequest, tag_json_obj);
    }
}