package com.apaza.moises.visitsucre.provider;

import java.util.Date;

public class Category {

    /*String ID = "id";
    String CODE = "code";
    String LOGO = "logo";
    String NAME = "name";
    String DATE = "date";
    String DESCRIPTION = "description";*/

    public String idCategory, code, logo, name, description;
    public Date date;

    public Category(){

    }

    public Category(String idCategory, String code, String logo, String name, String description, Date date){
        this.idCategory = idCategory;
        this.code = code;
        this.logo = logo;
        this.name = name;
        this.date = date;
        this.description = description;
    }

    public String getIdCategory() {
        return idCategory;
    }

    public Category setIdCategory(String idCategory) {
        this.idCategory = idCategory;
        return this;
    }

    public String getCode() {
        return code;
    }

    public Category setCode(String code) {
        this.code = code;
        return this;
    }

    public String getLogo() {
        return logo;
    }

    public Category setLogo(String logo) {
        this.logo = logo;
        return this;
    }

    public Date getDate() {
        return date;
    }

    public Category setDate(Date date) {
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
}
