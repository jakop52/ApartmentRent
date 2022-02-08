package com.jakupIndustries.apartmentRentClient.services;

import com.jakupIndustries.apartmentRentClient.models.Apartment;
import com.jakupIndustries.apartmentRentClient.models.LoginDTO;
import com.jakupIndustries.apartmentRentClient.models.RegisterDTO;
import com.jakupIndustries.apartmentRentClient.models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApartmentRentService {


    @Headers({"Accept: application/json"})
    @POST("auth/signin")
    Call<Void> login(@Body LoginDTO loginDTO);

    @POST("auth/signup")
    Call<Void> register(@Body RegisterDTO registerDTO);

    @GET("user/")
    Call<User> getLoggedUserData(@Header("Cookie") String jSessionID);

    @GET("apartments/{id}")
    Call<Apartment> getApartmentByID(@Path("id") int apartmentID, @Header("Cookie") String jSessionID);


}
