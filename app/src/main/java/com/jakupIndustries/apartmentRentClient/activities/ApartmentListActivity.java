package com.jakupIndustries.apartmentRentClient.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
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

public class ApartmentListActivity extends AppCompatActivity {
    private ApartmentRentService apartmentRentService;
    private TextView titleTextView;
    private RecyclerView apartmentsRecyclerView;
    private ApartmentsAdapter apartmentsAdapter;
    private ArrayList<Apartment> apartments;
    private String type="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apartment_list);

        apartments = new ArrayList<>();
        titleTextView = (TextView)findViewById(R.id.textViewTitle);
        apartmentRentService = ApiClient.getClient().create(ApartmentRentService.class);
        apartmentsAdapter = new ApartmentsAdapter(apartments);
        apartmentsRecyclerView = (RecyclerView)findViewById(R.id.apartmentsRecycleView);
        apartmentsRecyclerView.setHasFixedSize(true);
        apartmentsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        apartmentsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        apartmentsRecyclerView.setAdapter(apartmentsAdapter);

        apartmentsAdapter.setOnItemClickListener(new IClickListener<Apartment>() {
            @Override
            public void onClick(View view, Apartment data, int position) {
                Intent intent = new Intent(ApartmentListActivity.this, ApartmentDetailsActivity.class);
                intent.putExtra("apartmentID",data.getId());

                startActivity(intent);
            }
        });
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show();
                onBackPressed();
            } else {
                type = extras.getString("TYPE");
            }
        } else {
            type = (String) savedInstanceState.getSerializable("TYPE");
        }

    }

    private void refreshView(String type) {
        Call<User> call = apartmentRentService.getLoggedUserData(Cookie.cookie);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                try {
                    User user = new User(response.body());
                    apartments.clear();
                    Log.d("TYPE: ", type);
                    if(type.equals("OWNED")){
                        apartments.addAll(user.getOwnedApartments());
                        titleTextView.setText("Owned apartments");
                    }
                    else if (type.equals("RENTED")){
                        apartments.addAll(user.getRentedApartments());
                        titleTextView.setText("Rented apartments");
                    }

                    apartmentsAdapter.notifyDataSetChanged();

                    if (response.code() == 401) {
                        Cookie.cookie = "";
                    }
                } catch (NullPointerException e) {
                    Log.d("ERROR:", e.getMessage());
                    Cookie.cookie = "";
                    Log.d("COOKIE", "onResponse: cookie cleared and saved");

                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshView(type);
    }
}