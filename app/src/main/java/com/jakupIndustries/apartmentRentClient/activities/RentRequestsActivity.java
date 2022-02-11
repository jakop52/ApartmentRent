package com.jakupIndustries.apartmentRentClient.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jakupIndustries.apartmentRentClient.ApiClient;
import com.jakupIndustries.apartmentRentClient.Cookie;
import com.jakupIndustries.apartmentRentClient.R;
import com.jakupIndustries.apartmentRentClient.adapters.ApartmentsAdapter;
import com.jakupIndustries.apartmentRentClient.adapters.RentRequestsAdapter;
import com.jakupIndustries.apartmentRentClient.interfaces.IClickListener;
import com.jakupIndustries.apartmentRentClient.models.Apartment;
import com.jakupIndustries.apartmentRentClient.models.RentRequest;
import com.jakupIndustries.apartmentRentClient.models.User;
import com.jakupIndustries.apartmentRentClient.services.ApartmentRentService;

import java.util.ArrayList;

public class RentRequestsActivity extends AppCompatActivity {
    private int apartmentID = -1;
    private TextView apartmentNameTextView;
    private ApartmentRentService apartmentRentService;
    private RecyclerView rentRequestRecyclerView;
    private RentRequestsAdapter rentRequestsAdapter;
    private ArrayList<RentRequest> rentRequests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_requests);

        apartmentNameTextView = (TextView) findViewById(R.id.textViewApartmentName);
        rentRequests = new ArrayList<>();
        apartmentRentService = ApiClient.getClient().create(ApartmentRentService.class);
        rentRequestsAdapter = new RentRequestsAdapter(rentRequests);
        rentRequestRecyclerView = (RecyclerView) findViewById(R.id.rentRequestRecycleView);
        rentRequestRecyclerView.setHasFixedSize(true);
        rentRequestRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rentRequestRecyclerView.setItemAnimator(new DefaultItemAnimator());
        rentRequestRecyclerView.setAdapter(rentRequestsAdapter);
        rentRequestsAdapter.setOnItemClickListener(new IClickListener<RentRequest>() {
            @Override
            public void onClick(View view, RentRequest data, int position) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                accept(data.getId());
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(RentRequestsActivity.this);
                builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show();
                onBackPressed();
            } else {
                apartmentID = extras.getInt("apartmentID");
            }
        } else {
            apartmentID = (Integer) savedInstanceState.getSerializable("apartmentID");
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshView();
    }

    private void refreshView() {
        if (apartmentID != -1) {
            Call<ArrayList<RentRequest>> call = apartmentRentService.getRentRequestsByApartmentID(apartmentID, Cookie.cookie);
            call.enqueue(new Callback<ArrayList<RentRequest>>() {
                @Override
                public void onResponse(Call<ArrayList<RentRequest>> call, Response<ArrayList<RentRequest>> response) {
                    rentRequests.addAll(response.body());
                    rentRequestsAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<ArrayList<RentRequest>> call, Throwable t) {

                }
            });
        }
    }

    private void accept(int requestID){
        Call<Void> call = apartmentRentService.acceptRentRequestByApartmentID(requestID,Cookie.cookie);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code()==200){
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }
}