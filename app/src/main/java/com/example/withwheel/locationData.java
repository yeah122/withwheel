package com.example.withwheel;

public class locationData {
    private String lat;
    private String lng;
    private String place_name;
    private String place_address;

    public String getLat(){
        return lat;
    }

    public String getLng(){
        return lng;
    }

    public String getName(){
        return place_name;
    }

    public String getAddress(){
        return place_address;
    }

    public void setLat(String lat){
        this.lat = lat;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public void setName(String place_name) {
        this.place_name = place_name;
    }

    public void setAddress(String place_address) {
        this.place_address = place_address;
    }
}