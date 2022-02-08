package com.jakupIndustries.apartmentRentClient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.jakupIndustries.apartmentRentClient.activities.LoginActivity;
import com.jakupIndustries.apartmentRentClient.activities.MainActivity;

public class LauncherActivity extends AppCompatActivity {
    boolean coldStart = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        boolean isDarkThemeOn = (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK)  == Configuration.UI_MODE_NIGHT_YES;
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = getWindow().getDecorView();
            if (!isDarkThemeOn) {
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                // We want to change tint color to white again.
                // You can also record the flags in advance so that you can turn UI back completely if
                // you have set other flags before, such as translucent or full screen.
                decor.setSystemUiVisibility(0);
            }
        }

        Log.d("LIVE:","onCreate");
    }

    public void doThings(){
        SharedPreferences sharedPreferences = this.getSharedPreferences(getPackageName(),Context.MODE_PRIVATE);
        Cookie.cookie = sharedPreferences.getString("Cookie","");
        if (!coldStart){

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                   // IF USER LOGGED > go to MainActivity else goto LoginActivity
                   if(Cookie.cookie==""){
                      Intent intent = new Intent(LauncherActivity.this, LoginActivity.class);
                      startActivity(intent);

                   }else{
                       Intent intent = new Intent(LauncherActivity.this, MainActivity.class);
                       startActivity(intent);
                   }
                }
            }, 2000);
            coldStart=true;
        }
        else {
            Intent intent = new Intent(LauncherActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        Log.d("LIVE:","onResume");
        super.onResume();
        doThings();
    }

    @Override
    protected void onStart() {
        Log.d("LIVE:","onStart");
        super.onStart();
        //doThings();
    }
}