package com.progmobklp12.aplikasipresensi.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.progmobklp12.aplikasipresensi.BuildConfig;
import com.progmobklp12.aplikasipresensi.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        String versionName = BuildConfig.VERSION_NAME;
        TextView about = findViewById(R.id.version_number);
        about.setText(String.format(getResources().getString(R.string.version_string), versionName));
    }
}