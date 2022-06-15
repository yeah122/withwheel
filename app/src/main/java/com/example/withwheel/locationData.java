package com.example.withwheel;

import java.io.Serializable;

public class locationData implements Serializable {
    private String lat;
    private String lng;
    private String place_name;
    private String place_address;
    private String place_call;
    private String homepage;
    private String guide;
    private String entrance;
    private String elevator;
    private String toilet;
    private String parking;
    private String introdution;
    private String outside;
    private String inside;
    private String history;
    private String nature;
    private String shopping;
    private String art;
    private String themepark;
    private String city;
    private String food;
    private String google_rating;
    private String google_ratings_total;
    private String kakao_rating;
    private String kakao_ratings_total;

    public String distFromFirst;
    public String totalScore;

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

    public String getPlace_call(){
        return place_call;
    }

    public String getHomepage(){
        return homepage;
    }

    public String getGuide(){
        return guide;
    }

    public String getEntrance(){
        return entrance;
    }

    public String getElevator(){
        return elevator;
    }

    public String getToilet(){ return toilet; }

    public String getParking(){
        return parking;
    }

    public String getIntrodution(){
        return introdution;
    }

    public String getOutside(){
        return outside;
    }

    public String getInside(){
        return inside;
    }

    public String getHistory(){
        return history;
    }

    public String getNature(){
        return nature;
    }

    public String getShopping(){
        return shopping;
    }

    public String getArt(){
        return art;
    }

    public String getThemepark(){
        return themepark;
    }

    public String getCity(){
        return city;
    }

    public String getFood(){
        return food;
    }

    public String getGoogle_rating(){ return google_rating; }

    public String getGoogle_ratings_total(){
        return google_ratings_total;
    }

    public String getKakao_rating(){
        return kakao_rating;
    }

    public String getKakao_ratings_total(){
        return kakao_ratings_total;
    }

    public String getdistFromFirst(){
        return distFromFirst;
    }

    public String gettotalScore(){
        return totalScore;
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

    public void setPlace_call(String place_call){
        this.place_call = place_call;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public void setGuide(String guide) {
        this.guide = guide;
    }

    public void setEntrance(String entrance) {
        this.entrance = entrance;
    }

    public void setElevator(String elevator){ this.elevator = elevator; }

    public void setToilet(String toilet) { this.toilet = toilet; }

    public void setParking(String parking) {
        this.parking = parking;
    }

    public void setIntrodution(String introdution) {
        this.introdution = introdution;
    }

    public void setOutside(String outside){
        this.outside = outside;
    }

    public void setInside(String inside) {
        this.inside = inside;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public void setShopping(String shopping){
        this.shopping = shopping;
    }

    public void setArt(String art) {
        this.art = art;
    }

    public void setThemepark(String themepark) {
        this.themepark = themepark;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setFood(String food){
        this.food = food;
    }

    public void setGoogle_rating(String google_rating) {
        this.google_rating = google_rating;
    }

    public void setGoogle_ratings_total(String google_ratings_total) { this.google_ratings_total = google_ratings_total; }

    public void setKakao_rating(String kakao_rating) { this.kakao_rating = kakao_rating; }

    public void setKakao_ratings_total(String kakao_ratings_total) { this.kakao_ratings_total = kakao_ratings_total; }
}