package com.jakupIndustries.apartmentRentClient.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import android.widget.Toast;

import com.jakupIndustries.apartmentRentClient.ApiClient;
import com.jakupIndustries.apartmentRentClient.Cookie;
import com.jakupIndustries.apartmentRentClient.R;
import com.jakupIndustries.apartmentRentClient.adapters.ApartmentsAdapter;
import com.jakupIndustries.apartmentRentClient.interfaces.IClickListener;
import com.jakupIndustries.apartmentRentClient.models.Apartment;
import com.jakupIndustries.apartmentRentClient.models.User;
import com.jakupIndustries.apartmentRentClient.services.ApartmentRentService;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ApartmentRentService apartmentRentService;
    private TextView textViewHello;
    private RecyclerView apartmentsRecyclerView;
    private ApartmentsAdapter apartmentsAdapter;
    private ArrayList<Apartment> apartmentsArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        //TODO RENTED AND OWNED APARTMENT LISTING

        apartmentsArrayList = new ArrayList<Apartment>();
        textViewHello = (TextView) findViewById(R.id.textViewHello);
        apartmentRentService = ApiClient.getClient().create(ApartmentRentService.class);

        apartmentsAdapter = new ApartmentsAdapter(apartmentsArrayList);
        apartmentsRecyclerView = (RecyclerView)findViewById(R.id.ownedApartmentsRecycleView);
        apartmentsRecyclerView.setHasFixedSize(true);
        apartmentsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        apartmentsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        apartmentsRecyclerView.setAdapter(apartmentsAdapter);

        apartmentsAdapter.setOnItemClickListener(new IClickListener<Apartment>() {
            @Override
            public void onClick(View view, Apartment data, int position) {
                Intent intent = new Intent(MainActivity.this, ApartmentDetailsActivity.class);
                intent.putExtra("apartmentID",data.getId());
                startActivity(intent);
            }
        });





        refreshView();
    }


    private void refreshView() {
        Call<User> call = apartmentRentService.getLoggedUserData(Cookie.cookie);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                try {
                    User loggedUser = new User(response.body());
                    textViewHello.setText("Hello \n"+loggedUser.getName());

                    //apartmentsArrayList = loggedUser.getOwnedApartments();
                    apartmentsArrayList.addAll(loggedUser.getOwnedApartments());
                    apartmentsAdapter.notifyDataSetChanged();

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
}