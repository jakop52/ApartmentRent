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

public class SearchActivity extends AppCompatActivity {
    private ApartmentRentService apartmentRentService;
    private TextView titleTextView;
    private RecyclerView apartmentsRecyclerView;
    private ApartmentsAdapter apartmentsAdapter;
    private ArrayList<Apartment> apartments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

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
                Intent intent = new Intent(SearchActivity.this, ApartmentDetailsActivity.class);
                intent.putExtra("apartmentID",data.getId());

                startActivity(intent);
            }
        });

    }

    private void refreshView() {
        Call<ArrayList<Apartment>> call = apartmentRentService.getNotRentedApartments(Cookie.cookie);
        call.enqueue(new Callback<ArrayList<Apartment>>() {
            @Override
            public void onResponse(Call<ArrayList<Apartment>> call, Response<ArrayList<Apartment>> response) {
                apartments.clear();

                apartments.addAll(response.body());
                apartmentsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ArrayList<Apartment>> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshView();
    }
}