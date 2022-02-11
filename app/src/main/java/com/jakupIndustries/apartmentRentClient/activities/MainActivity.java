package com.jakupIndustries.apartmentRentClient.activities;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.jakupIndustries.apartmentRentClient.ApiClient;
import com.jakupIndustries.apartmentRentClient.Cookie;
import com.jakupIndustries.apartmentRentClient.R;
import com.jakupIndustries.apartmentRentClient.models.User;
import com.jakupIndustries.apartmentRentClient.services.ApartmentRentService;

public class MainActivity extends AppCompatActivity {
    private ApartmentRentService apartmentRentService;
    private TextView textViewHello, ownedAmountTextView, rentedAmountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        textViewHello = (TextView) findViewById(R.id.textViewHello);
        ownedAmountTextView = (TextView) findViewById(R.id.textViewOwnedAmount);
        rentedAmountTextView =(TextView)findViewById(R.id.textViewRentedAmount);
        apartmentRentService = ApiClient.getClient().create(ApartmentRentService.class);
        refreshView();
    }


    private void refreshView() {
        Call<User> call = apartmentRentService.getLoggedUserData(Cookie.cookie);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                try {
                    Cookie.loggedUser = new User(response.body());
                    textViewHello.setText("Hello \n"+Cookie.loggedUser.getName());

                    ownedAmountTextView.setText(String.valueOf(Cookie.loggedUser.getOwnedApartments().size()));
                    rentedAmountTextView.setText(String.valueOf(Cookie.loggedUser.getRentedApartments().size()));



                    if (response.code() == 401) {
                        Cookie.cookie = "";
                        saveCookie();
                    }
                } catch (NullPointerException e) {
                    Log.d("ERROR:", e.getMessage());
                    Cookie.cookie = "";
                    saveCookie();
                    Log.d("COOKIE", "onResponse: cookie cleared and saved");

                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        //saveCookie();
        finishAffinity();
    }

    @Override
    protected void onPause() {
        saveCookie();
        super.onPause();

    }

    public void saveCookie() {


        SharedPreferences sharedPreferences = this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Cookie", Cookie.cookie);
        editor.apply();
        Log.d("COOKIE:", "COOKIE SAVED");
    }

    public void onOwnedClick(View view) {
        Intent intent = new Intent(MainActivity.this, ApartmentListActivity.class);
        intent.putExtra("TYPE","OWNED");
        startActivity(intent);
    }

    public void onRentedClick(View view) {
        Intent intent = new Intent(MainActivity.this, ApartmentListActivity.class);
        intent.putExtra("TYPE","RENTED");
        startActivity(intent);
    }

    public void onSearchClick(View view) {
        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
        startActivity(intent);
    }
}