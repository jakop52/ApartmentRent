package com.jakupIndustries.apartmentRentClient.models;

import com.google.gson.annotations.SerializedName;

public class Apartment {

    private int id;
    private String name;
    private String address;
    private int area;
    private int floor;
    private int rooms;
    private User owner;
    private User rentier;

    public Apartment() {
    }

    public Apartment(int id, String name, String address, int area, int floor, int rooms, User owner, User rentier) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.area = area;
        this.floor = floor;
        this.rooms = rooms;
        this.owner = owner;
        this.rentier = rentier;
    }

    public Apartment(Apartment response) {
        this.id = response.id;
        this.name = response.name;
        this.address = response.address;
        this.area = response.area;
        this.floor = response.floor;
        this.rooms = response.rooms;
        this.owner = response.owner;

        if (response.rentier==null){
            this.rentier= new User(-1,"","",null,null,null);
        }else this.rentier = response.rentier;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public int getRooms() {
        return rooms;
    }

    public void setRooms(int rooms) {
        this.rooms = rooms;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public User getRentier() {
        return rentier;
    }

    public void setRentier(User rentier) {
        this.rentier = rentier;
    }


}
