package com.progmobklp12.aplikasipresensi.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.progmobklp12.aplikasipresensi.R;
import com.progmobklp12.aplikasipresensi.api.BaseApi;
import com.progmobklp12.aplikasipresensi.api.RetrofitClient;
import com.progmobklp12.aplikasipresensi.model.presensi.PresensiCreateResponse;
import com.progmobklp12.aplikasipresensi.model.presensi.PresensiEditResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditPresensiActivity extends AppCompatActivity {

    private TextInputEditText namaPresensiField;
    private TextInputEditText keteranganPresensiField;
    private TextInputEditText tanggalPresensiOpen;
    private TextInputEditText tanggalPresensiClose;
    private TextInputEditText waktuPresensiOpen;
    private TextInputEditText waktuPresensiClose;

    private Button updatePresensiButton;

    MaterialDatePicker.Builder materialDateBuilderTanggalOpen;
    MaterialDatePicker.Builder materialDateBuilderTanggalClose;
    MaterialDatePicker materialDatePickerTanggalOpen;
    MaterialDatePicker materialDatePickerTanggalClose;

    MaterialTimePicker.Builder materialTimePickerBuliderWaktuOpen;
    MaterialTimePicker.Builder materialTimePickerBuilderWaktuClose;
    MaterialTimePicker materialTimePickerWaktuOpen;
    MaterialTimePicker materialTimePickerWaktuClose;

    private SimpleDateFormat timeFormatter;
    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat editDateFormatter;

    private Date waktuOpen;
    private Date waktuClose;

    @TimeFormat
    private int clockFormat;

    private Date tanggalOpen;
    private Date tanggalClose;

    private String presensiNameKey = "PRESENSINAME";
    private String presensiDescKey = "PRESENSIDESC";
    private String presensiDateOpenKey = "PRESENSIDATEOPEN";
    private String presensiDateCloseKey = "PRESENSIDATECLOSE";
    private String presensiIdKey = "PRESENSIID";

    private int idPresensi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_presensi);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        namaPresensiField = findViewById(R.id.update_presensi_name_text_field);
        keteranganPresensiField = findViewById(R.id.update_presensi_desc_text_field);
        tanggalPresensiOpen = findViewById(R.id.update_presensi_date_open_text_field);
        tanggalPresensiClose = findViewById(R.id.update_presensi_date_closed_text_field);
        waktuPresensiOpen = findViewById(R.id.update_presensi_time_open_text_field);
        waktuPresensiClose = findViewById(R.id.update_presensi_time_close_text_field);
        updatePresensiButton = findViewById(R.id.update_presensi_button);

        clockFormat = TimeFormat.CLOCK_24H;
        Calendar cal = Calendar.getInstance();

        timeFormatter = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        editDateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        Bundle extras = getIntent().getExtras();

        namaPresensiField.setText(extras.getString(presensiNameKey));
        keteranganPresensiField.setText(extras.getString(presensiDescKey));
        Log.d("tanggal", extras.getString(presensiDateCloseKey));

        try {
            tanggalOpen = editDateFormatter.parse(extras.getString(presensiDateOpenKey));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            tanggalClose = editDateFormatter.parse(extras.getString(presensiDateCloseKey));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        tanggalPresensiOpen.setText(dateFormatter.format(tanggalOpen));
        tanggalPresensiClose.setText(dateFormatter.format(tanggalClose));
        waktuPresensiOpen.setText(timeFormatter.format(tanggalOpen));
        waktuPresensiClose.setText(timeFormatter.format(tanggalClose));
        idPresensi = extras.getInt(presensiIdKey);

        materialTimePickerBuliderWaktuOpen = new MaterialTimePicker.Builder();
        materialTimePickerBuliderWaktuOpen.setTimeFormat(clockFormat);
        materialTimePickerBuliderWaktuOpen.setTitleText("Pilih waktu presensi di buka");

        materialTimePickerBuilderWaktuClose = new MaterialTimePicker.Builder();
        materialTimePickerBuilderWaktuClose.setTimeFormat(clockFormat);
        materialTimePickerBuilderWaktuClose.setTitleText("Pilih waktu presensi di tutup");

        waktuPresensiClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialTimePickerWaktuClose = materialTimePickerBuilderWaktuClose.build();
                materialTimePickerWaktuClose.addOnPositiveButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cal.set(Calendar.HOUR_OF_DAY, materialTimePickerWaktuClose.getHour());
                        cal.set(Calendar.MINUTE,materialTimePickerWaktuClose.getMinute());
                        cal.set(Calendar.SECOND,0);
                        waktuClose = cal.getTime();
                        waktuPresensiClose.setText(timeFormatter.format(waktuClose));
                    }
                });
                materialTimePickerWaktuClose.show(getSupportFragmentManager(), "MATERIAL_TIME_PICKER_1");
            }
        });

        waktuPresensiClose.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    materialTimePickerWaktuClose = materialTimePickerBuilderWaktuClose.build();
                    materialTimePickerWaktuClose.addOnPositiveButtonClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cal.set(Calendar.HOUR_OF_DAY, materialTimePickerWaktuClose.getHour());
                            cal.set(Calendar.MINUTE,materialTimePickerWaktuClose.getMinute());
                            cal.set(Calendar.SECOND,0);
                            waktuClose = cal.getTime();
                            waktuPresensiClose.setText(timeFormatter.format(waktuClose));
                        }
                    });
                    materialTimePickerWaktuClose.show(getSupportFragmentManager(), "MATERIAL_TIME_PICKER_1");
                }
            }
        });


        waktuPresensiOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialTimePickerWaktuOpen = materialTimePickerBuliderWaktuOpen.build();
                materialTimePickerWaktuOpen.addOnPositiveButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cal.set(Calendar.HOUR_OF_DAY, materialTimePickerWaktuOpen.getHour());
                        cal.set(Calendar.MINUTE,materialTimePickerWaktuOpen.getMinute());
                        cal.set(Calendar.SECOND,0);
                        waktuOpen = cal.getTime();
                        waktuPresensiOpen.setText(timeFormatter.format(waktuOpen));
                    }
                });
                materialTimePickerWaktuOpen.show(getSupportFragmentManager(), "MATERIAL_TIME_PICKER");
            }
        });

        waktuPresensiOpen.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    materialTimePickerWaktuOpen = materialTimePickerBuliderWaktuOpen.build();
                    materialTimePickerWaktuOpen.addOnPositiveButtonClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cal.set(Calendar.HOUR_OF_DAY, materialTimePickerWaktuOpen.getHour());
                            cal.set(Calendar.MINUTE,materialTimePickerWaktuOpen.getMinute());
                            cal.set(Calendar.SECOND,0);
                            waktuOpen = cal.getTime();
                            waktuPresensiOpen.setText(timeFormatter.format(waktuOpen));
                        }
                    });
                    materialTimePickerWaktuOpen.show(getSupportFragmentManager(), "MATERIAL_TIME_PICKER");
                }
            }
        });

        materialDateBuilderTanggalOpen = MaterialDatePicker.Builder.datePicker();
        materialDateBuilderTanggalOpen.setTitleText("Pilih tanggal presensi di buka");
        materialDatePickerTanggalOpen = materialDateBuilderTanggalOpen.build();

        materialDateBuilderTanggalClose = MaterialDatePicker.Builder.datePicker();
        materialDateBuilderTanggalClose.setTitleText("Pilih tanggal presensi di tutup");
        materialDatePickerTanggalClose = materialDateBuilderTanggalClose.build();

        tanggalPresensiOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDatePickerTanggalOpen.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER_1");
            }
        });

        tanggalPresensiOpen.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    materialDatePickerTanggalOpen.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER_1");
                }
            }
        });

        tanggalPresensiClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDatePickerTanggalClose.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
            }
        });

        tanggalPresensiClose.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    materialDatePickerTanggalClose.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
                }
            }
        });

        materialDatePickerTanggalOpen.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selectedDate) {
                // link: https://stackoverflow.com/questions/14933330/datepicker-how-to-popup-datepicker-when-click-on-edittext
                // user has selected a date
                // format the date and set the text of the input box to be the selected date
                // right now this format is hard-coded, this will change
                ;
                // Get the offset from our timezone and UTC.
                TimeZone timeZoneUTC = TimeZone.getDefault();
                // It will be negative, so that's the -1
                int offsetFromUTC = timeZoneUTC.getOffset(new Date().getTime()) * -1;
                // Create a date format, then a date object with our offset
                Date date = new Date(selectedDate + offsetFromUTC);
                tanggalPresensiOpen.setText(dateFormatter.format(date));
            }
        });

        materialDatePickerTanggalClose.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selectedDate) {
                TimeZone timeZoneUTC = TimeZone.getDefault();
                int offsetFromUTC = timeZoneUTC.getOffset(new Date().getTime()) * -1;
                Date date = new Date(selectedDate + offsetFromUTC);
                tanggalPresensiClose.setText(dateFormatter.format(date));
            }
        });

        updatePresensiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePresensi();
            }
        });
    }
    public void updatePresensi() {
        BaseApi editPresensiApi = RetrofitClient.buildRetrofit().create(BaseApi.class);
        Call<PresensiEditResponse> presensiEditResponseCall = editPresensiApi.editPresensi(idPresensi, namaPresensiField.getText().toString(),
                keteranganPresensiField.getText().toString(), (tanggalPresensiOpen.getText().toString() + " " + waktuPresensiOpen.getText().toString()),
                (tanggalPresensiClose.getText().toString() + " " + waktuPresensiClose.getText().toString()));
        presensiEditResponseCall.enqueue(new Callback<PresensiEditResponse>() {
            @Override
            public void onResponse(Call<PresensiEditResponse> call, Response<PresensiEditResponse> response) {
                if (response.body().getMessage().equals("Data berhasil di Update")) {
                    Toast.makeText(getApplicationContext(), "Presensi berhasil di update!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Presensi gagal di update", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PresensiEditResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Presensi gagal di update", Toast.LENGTH_SHORT).show();
            }
        });
    }
}