package com.apaza.moises.visitsucre.provider;

import java.util.Date;

public class Place {

    /*String ID = "id";
    String CODE = "code";
    String NAME = "name";
    String ADDRESS = "address";
    String LATITUDE = "latitude";
    String LONGITUDE = "longitude";
    String DESCRIPTION = "description";
    String PATH_IMAGE = "pathImage";
    String DATE = "date";
    String ID_CATEGORY = "idCategory";*/

    String idPlace, code, name, address, description, pathImage, idCategory;
    double latitude, longitude;
    Date date;

    public Place(){

    }

    public Place(String idPlace, String code, String name, String address,
                 double latitude, double longitude, String description, String pathImage, Date date, String idCategory){
        this.idPlace = idPlace;
        this.code = code;
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.pathImage = pathImage;
        this.date = date;
        this.idCategory = idCategory;
    }

    public String getIdPlace(){
        return idPlace;
    }

    public Place setIdPlace(String idPlace){
        this.idPlace = idPlace;
        return this;
    }

    public String getCode() {
        return code;
    }

    public Place setCode(String code) {
        this.code = code;
        return this;
    }

    public String getName() {
        return name;
    }

    public Place setName(String name) {
        this.name = name;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public Place setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Place setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getPathImage() {
        return pathImage;
    }

    public Place setPathImage(String pathImage) {
        this.pathImage = pathImage;
        return this;
    }

    public String getIdCategory() {
        return idCategory;
    }

    public Place setIdCategory(String idCategory) {
        this.idCategory = idCategory;
        return this;
    }

    public double getLatitude() {
        return latitude;
    }

    public Place setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public double getLongitude() {
        return longitude;
    }

    public Place setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

    public Date getDate() {
        return date;
    }

    public Place setDate(Date date) {
        this.date = date;
        return this;
    }
}
