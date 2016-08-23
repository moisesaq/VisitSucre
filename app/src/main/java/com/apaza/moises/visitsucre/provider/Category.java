package com.apaza.moises.visitsucre.provider;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Category {

    /*String ID = "id";
    String LOGO = "logo";
    String NAME = "name";
    String DATE = "date";
    String DESCRIPTION = "description";

    String STATUS = "status";
    String ID_REMOTE = "idRemote";
    String PENDING_INSERTION = "pendingInsertion";*/

    public String idCategory;
    public String logo;
    public String name;
    public String date;
    public String description;

    public int status;

    @SerializedName("_id")
    public String idRemote;

    public int pendingInsertion;

    public Category(){

    }

    public Category(String idCategory, String logo, String name, String date, String description){
        this.idCategory = idCategory;
        this.logo = logo;
        this.name = name;
        this.date = date;
        this.description = description;
        /*this.status = status;
        this.idRemote = idRemote;
        this.pendingInsertion = pendingInsertion;*/
    }

    public String getIdCategory() {
        return idCategory;
    }

    public Category setIdCategory(String idCategory) {
        this.idCategory = idCategory;
        return this;
    }

    public String getLogo() {
        return logo;
    }

    public Category setLogo(String logo) {
        this.logo = logo;
        return this;
    }

    public String getDate() {
        return date;
    }

    public Category setDate(String date) {
        this.date = date;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Category setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getName() {
        return name;
    }

    public Category setName(String name) {
        this.name = name;
        return this;
    }

    public int getStatus() {
        return status;
    }

    public Category setStatus(int status) {
        this.status = status;
        return this;
    }

    public String getIdRemote() {
        return idRemote;
    }

    public Category setIdRemote(String idRemote) {
        this.idRemote = idRemote;
        return this;
    }

    public int getPendingInsertion() {
        return pendingInsertion;
    }

    public Category setPendingInsertion(int pendingInsertion) {
        this.pendingInsertion = pendingInsertion;
        return this;
    }
}
