package com.jakupIndustries.apartmentRentClient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.jakupIndustries.apartmentRentClient.activities.LoginActivity;

public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // IF USER LOGGED > go to MainActivity else goto LoginActivity
                Intent intent = new Intent(LauncherActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        }, 3000);
    }
}