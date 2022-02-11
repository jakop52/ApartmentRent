package com.jakupIndustries.apartmentRentClient.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
    AppCompatButton buttonRentRequests, buttonDeleteApartment, buttonRequestRent;
    TextView apartmentNameTextView, apartmentAddressTextView, apartmentAreaTextView, apartmentFloorTextView, apartmentRoomsTextView, apartmentOwnerTextView, apartmentRentierTextView;
    int apartmentID = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apartment_details);

        buttonRentRequests = (AppCompatButton) findViewById(R.id.buttonRentRequests);
        buttonDeleteApartment = (AppCompatButton) findViewById(R.id.buttonDeleteApartment);
        buttonRequestRent = (AppCompatButton) findViewById(R.id.buttonRequestRent);

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


                    Log.d("USER ID:", Integer.toString(Cookie.loggedUser.getId()));
                    Log.d("from apartment USER ID:", Integer.toString(apartment.getOwner().getId()));
                    if(apartment.getOwner().getId()==Cookie.loggedUser.getId()){ //TODO TEST
                        if(apartment.getRentier().getId()==-1){
                            buttonDeleteApartment.setVisibility(View.VISIBLE);
                            buttonRentRequests.setVisibility(View.VISIBLE);
                        }
                        else {
                            buttonDeleteApartment.setVisibility(View.GONE);
                            buttonRentRequests.setVisibility(View.GONE);
                        }
                        buttonRequestRent.setVisibility(View.GONE);
                    }else if(Cookie.loggedUser.getId()!=apartment.getOwner().getId()){
                        if(apartment.getRentier().getId()==-1){
                            buttonRequestRent.setVisibility(View.VISIBLE);
                        }else{
                            buttonRequestRent.setVisibility(View.GONE);
                        }
                        buttonDeleteApartment.setVisibility(View.GONE);
                        buttonRentRequests.setVisibility(View.GONE);
                    }else{
                        buttonDeleteApartment.setVisibility(View.GONE);
                        buttonRentRequests.setVisibility(View.GONE);
                        buttonRequestRent.setVisibility(View.GONE);
                    }

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

    public void onRentRequestsButtonClick(View view) {
        Intent intent = new Intent(ApartmentDetailsActivity.this, RentRequestsActivity.class);
        intent.putExtra("apartmentID",apartmentID);
        startActivity(intent);
    }

    public void onDeleteApartmentClick(View view) {//TODO apatment deleting only if owner
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        Call<Void> call = apartmentRentService.deleteRentRequestsByApartmentID(apartmentID,Cookie.cookie);
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
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(ApartmentDetailsActivity.this);
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        refreshView();
    }

    public void onRequestRentClick(View view) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        Call<Void> call = apartmentRentService.putRentRequestByApartmentID(apartmentID,Cookie.cookie);
                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if(response.code()==200){
                                    Toast.makeText(getApplicationContext(),"Request sent",Toast.LENGTH_SHORT).show();
                                }else Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {

                            }
                        });
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(ApartmentDetailsActivity.this);
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }
}