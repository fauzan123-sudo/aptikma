package com.example.aptikma;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.aptikma.helper.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MauIzin extends AppCompatActivity implements AdapterView.OnItemSelectedListener, TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener {


    SessionManager sessionManager;
    String getId, getName;
    String dari1,sampai1,jenis1,keterangan1;
    private String izin = Constans.BaseUrl + "Perizinan.php";

    Spinner spinner;

    int which ;
    EditText keterangan;
    RadioGroup radioGroup;
    RadioButton rb1,rb2;
    TextView dari, sampai,hanyatulisan1,hanyatulisan2, kalender1, kalender2; //
    Button kirim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mau_izin);
        spinner = findViewById(R.id.spinner1);

        keterangan = findViewById(R.id.editKeterangan);
        keterangan1 = keterangan.getText().toString();
        AndroidNetworking.initialize(getApplicationContext());
        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(sessionManager.ID);
        getName = user.get(sessionManager.NAME);
        kirim = findViewById(R.id.kirim);
        hanyatulisan1 = findViewById(R.id.hanyaTulisan1);
        hanyatulisan2 = findViewById(R.id.hanyaTulisan2);
        kalender1 = findViewById(R.id.txt_date1);
        kalender2 = findViewById(R.id.txt_date2);
        rb1 = findViewById(R.id.rb1);
        rb2 = findViewById(R.id.rb2);
        dari = findViewById(R.id.coba);
        sampai = findViewById(R.id.sampai);
        dari1 = dari.getText().toString();
        sampai1 = sampai.getText().toString();
        radioGroup = findViewById(R.id.radioGroup2);
        kalender1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment2();
                datePicker.show(getSupportFragmentManager(), "date picker");
                which = 2;
                Toast.makeText(MauIzin.this, "dari", Toast.LENGTH_SHORT).show();
            }
        });

        kalender2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment2();
                datePicker.show(getSupportFragmentManager(), "date picker");
                which =3;

            }
        });

        hanyatulisan1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MauIzin.this, "Silahkan pilih jam atau tanggal", Toast.LENGTH_SHORT).show();
            }
        });
        hanyatulisan2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MauIzin.this, "Silahkan pilih jam atau tanggal", Toast.LENGTH_SHORT).show();
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rb1://tanggal
                        hanyatulisan1.setVisibility(View.INVISIBLE);
                        hanyatulisan2.setVisibility(View.INVISIBLE);
                        dari.setVisibility(View.INVISIBLE);
                        sampai.setVisibility(View.INVISIBLE);
                        kalender1.setVisibility(View.VISIBLE);
                        kalender2.setVisibility(View.VISIBLE);
                        break;
                    case R.id.rb2://jam
                        hanyatulisan1.setVisibility(View.INVISIBLE);
                        hanyatulisan2.setVisibility(View.INVISIBLE);
                        dari.setVisibility(View.VISIBLE);
                        sampai.setVisibility(View.VISIBLE);
                        kalender1.setVisibility(View.INVISIBLE);
                        kalender2.setVisibility(View.INVISIBLE);
                        break;
                }
            }
        });


        kirim.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

//                Toast.makeText(MauIzin.this, "", Toast.LENGTH_SHORT).show();
                AndroidNetworking.post(izin)
                        .addBodyParameter("id_pegawai", getId)
                        .addBodyParameter("nama", getName)
                        .addBodyParameter("dari", dari1)
                        .addBodyParameter("sampai", sampai1)
                        .addBodyParameter("keterangan", keterangan1)
                        .setTag("test")
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {


                            }
                            @Override
                            public void onError(ANError error) {
                                // handle error
                            }
                        });
            }
        });
        dari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                which = 0;
                DialogFragment timePicker = new DatePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });

        sampai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                which =1;
                DialogFragment timePicker = new DatePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.numbers, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(MauIzin.this);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        TextView dari = findViewById(R.id.coba);
        dari.setText("pilih jenis izin");

    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        if (which == 0) {
            TextView textView = findViewById(R.id.coba);
            textView.setText("Jam: " + hourOfDay + ":" + minute);
        }else if (which==1){
            TextView tx = findViewById(R.id.sampai);
            tx.setText("Jam: " + hourOfDay + ":" + minute);
        }



    }



    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        if (which==2){
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
            TextView tv1 =  findViewById(R.id.txt_date1);
            tv1.setText(currentDateString);
        }else if(which==3){
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
            TextView tv1 =  findViewById(R.id.txt_date2);
            tv1.setText(currentDateString);

        }

    }



}