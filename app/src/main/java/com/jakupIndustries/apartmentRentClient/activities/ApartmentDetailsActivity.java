package com.jakupIndustries.apartmentRentClient.activities;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.jakupIndustries.apartmentRentClient.ApiClient;
import com.jakupIndustries.apartmentRentClient.Cookie;
import com.jakupIndustries.apartmentRentClient.R;
import com.jakupIndustries.apartmentRentClient.models.Apartment;
import com.jakupIndustries.apartmentRentClient.models.User;
import com.jakupIndustries.apartmentRentClient.services.ApartmentRentService;

import org.w3c.dom.Text;

public class ApartmentDetailsActivity extends AppCompatActivity {
    ApartmentRentService apartmentRentService;
    TextView apartmentNameTextView, apartmentAddressTextView, apartmentAreaTextView, apartmentFloorTextView, apartmentRoomsTextView, apartmentOwnerTextView, apartmentRentierTextView;
    int apartmentID = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apartment_details);

        apartmentRentService = ApiClient.getClient().create(ApartmentRentService.class);
        apartmentNameTextView = (TextView) findViewById(R.id.apartmentNameTextView);
        apartmentAddressTextView = (TextView) findViewById(R.id.apartmentAddressTextView);
        apartmentAreaTextView = (TextView) findViewById(R.id.apartmentAreaTextView);
        apartmentFloorTextView = (TextView) findViewById(R.id.apartmentFloorsTextView);
        apartmentRoomsTextView = (TextView) findViewById(R.id.apartmentRoomsTextView);
        apartmentOwnerTextView = (TextView) findViewById(R.id.apartmentOwnerNameTextView);
        apartmentRentierTextView = (TextView) findViewById(R.id.apartmentRentierNameTextView);


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

        Log.d("ApartmentDetailsActivity", "onCreate: Apartment id = " + apartmentID);
        refreshView();
    }

    private void refreshView() {
        if (apartmentID != -1) {

            Call<Apartment> call = apartmentRentService.getApartmentByID(apartmentID, Cookie.cookie);
            call.enqueue(new Callback<Apartment>() {
                @Override
                public void onResponse(Call<Apartment> call, Response<Apartment> response) {
                    Apartment apartment = new Apartment(response.body());
                    apartmentNameTextView.setText(apartment.getName());
                    apartmentAddressTextView.setText("Address: " + apartment.getAddress());
                    apartmentAreaTextView.setText("Area: " + apartment.getArea());
                    apartmentFloorTextView.setText("Floor: " + apartment.getFloor());
                    apartmentRoomsTextView.setText("Rooms: " + apartment.getRooms());
                    apartmentOwnerTextView.setText("Owner: " + apartment.getOwner().getName());
                    apartmentRentierTextView.setText("Rentier: " + apartment.getRentier().getName());

                    System.out.println("TEST: " + apartment.getName());
                }

                @Override
                public void onFailure(Call<Apartment> call, Throwable t) {
                    Log.d("TAG", "onFailure: " + t);
                    System.out.println("FAILUR " + t);
                }
            });
        }
    }
//TODO list of rent request only if no owner
    //TODO rent requests acceptation by owner only
    //TODO apatment deleting only if owner
}