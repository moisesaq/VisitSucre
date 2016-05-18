package com.apaza.moises.visitsucre.provider;

import java.util.Date;

/**
 * Created by moises on 18/05/16.
 */
public class Place {
    String code, name, address, description, pathImage, category;
    double latitude, longitude;
    Date date;

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

    public String getCategory() {
        return category;
    }

    public Place setCategory(String category) {
        this.category = category;
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
