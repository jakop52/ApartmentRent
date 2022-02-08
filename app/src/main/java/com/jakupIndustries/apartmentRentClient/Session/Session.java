package com.jakupIndustries.apartmentRentClient.Session;

public class Session {
    private String jSessionID;

    public Session(){}

    public Session(String jSessionID){
        this.jSessionID = jSessionID;
    }

    public String getjSessionID() {
        return jSessionID;
    }

    public void setjSessionID(String jSessionID) {
        this.jSessionID = jSessionID;
    }
}
