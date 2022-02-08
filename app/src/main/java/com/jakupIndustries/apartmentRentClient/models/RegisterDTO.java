package com.jakupIndustries.apartmentRentClient.models;

public class RegisterDTO {
    private String name;
    private String email;
    private String password;

    public RegisterDTO(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
