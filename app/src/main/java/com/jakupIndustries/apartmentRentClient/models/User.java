package com.jakupIndustries.apartmentRentClient.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import retrofit2.Response;

public class User {
    private int id;
    private String name;
    private String email;
    private ArrayList<Role> roles;
    private ArrayList<Apartment> ownedApartments;
    private ArrayList<Apartment> rentedApartments;

    public User() {
    }

    public User(User response) {
        this.id = response.id;
        this.name = response.name;
        this.email = response.email;
        this.roles = response.roles;
        this.ownedApartments = response.ownedApartments;
        this.rentedApartments = response.rentedApartments;
    }

    public User(int id, String name, String email, ArrayList<Role> roles, ArrayList<Apartment> ownedApartments, ArrayList<Apartment> rentedApartments) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.roles = roles;
        this.ownedApartments = ownedApartments;
        this.rentedApartments = rentedApartments;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<Role> getRoles() {
        return roles;
    }

    public void setRoles(ArrayList<Role> roles) {
        this.roles = roles;
    }

    public ArrayList<Apartment> getOwnedApartments() {
        return ownedApartments;
    }

    public void setOwnedApartments(ArrayList<Apartment> ownedApartments) {
        this.ownedApartments = ownedApartments;
    }

    public ArrayList<Apartment> getRentedApartments() {
        return rentedApartments;
    }

    public void setRentedApartments(ArrayList<Apartment> rentedApartments) {
        this.rentedApartments = rentedApartments;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", roles=" + roles.toString() +
                ", ownedApartments=" + ownedApartments.toString() +
                ", rentedApartments=" + rentedApartments.toString() +
                '}';
    }
}
