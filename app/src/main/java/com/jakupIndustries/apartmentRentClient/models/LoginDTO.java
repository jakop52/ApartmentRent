package com.jakupIndustries.apartmentRentClient.models;

public class LoginDTO {
    private String usernameOrEmail;
    private String password;

    public LoginDTO(String usernameOrEmail, String password) {
        this.usernameOrEmail = usernameOrEmail;
        this.password = password;
    }
}
