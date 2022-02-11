package com.jakupIndustries.apartmentRentClient.services;

import com.jakupIndustries.apartmentRentClient.models.Apartment;
import com.jakupIndustries.apartmentRentClient.models.LoginDTO;
import com.jakupIndustries.apartmentRentClient.models.RegisterDTO;
import com.jakupIndustries.apartmentRentClient.models.RentRequest;
import com.jakupIndustries.apartmentRentClient.models.User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApartmentRentService {



    @POST("auth/signin")
    Call<Void> login(@Body LoginDTO loginDTO);

    @POST("auth/signup")
    Call<Void> register(@Body RegisterDTO registerDTO);

    @GET("user/")
    Call<User> getLoggedUserData(@Header("Cookie") String jSessionID);

    @GET("apartments/{id}")
    Call<Apartment> getApartmentByID(@Path("id") int apartmentID, @Header("Cookie") String jSessionID);
    @DELETE("/apartments/{apartmentID}")
    Call<Void> deleteRentRequestsByApartmentID(@Path(("apartmentID")) int apartmentID, @Header("Cookie") String jSessionID);
    @GET("apartments/notRented")
    Call<ArrayList<Apartment>> getNotRentedApartments(@Header("Cookie") String jSessionID);

    @GET("rents/{apartmentID}")
    Call<ArrayList<RentRequest>> getRentRequestsByApartmentID(@Path(("apartmentID")) int apartmentID, @Header("Cookie") String jSessionID);
    @PUT("rents/{apartmentID}")
    Call<Void> putRentRequestByApartmentID(@Path(("apartmentID")) int apartmentID, @Header("Cookie") String jSessionID);
    @GET("rents/")
    Call<ArrayList<RentRequest>> getRentRequestsByAuth(@Header("Cookie") String jSessionID);
    @PATCH("rents/accept/{id}")
    Call<Void> acceptRentRequestByApartmentID(@Path("id") int requestID, @Header("Cookie") String jSessionID);


}
