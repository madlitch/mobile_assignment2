package com.massimo.location;

public class Location {
    int ID;
    String address;
    float latitude;
    float longitude;


    Location(int ID, String address, float latitude, float longitude) {
        this.ID = ID;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    Location(String address, float latitude, float longitude) {
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
