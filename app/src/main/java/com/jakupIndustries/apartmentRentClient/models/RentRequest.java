package com.jakupIndustries.apartmentRentClient.models;

public class RentRequest {
    private int id;
    private Apartment apartment;
    private User user;

    public RentRequest(int id, Apartment apartment, User user) {
        this.id = id;
        this.apartment = apartment;
        this.user = user;
    }

    public RentRequest() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Apartment getApartment() {
        return apartment;
    }

    public void setApartment(Apartment apartment) {
        this.apartment = apartment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
