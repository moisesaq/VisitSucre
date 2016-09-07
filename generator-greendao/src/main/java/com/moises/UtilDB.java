package com.moises;

public class UtilDB {

    public static String NAME_DB = "DataBaseVisitSucre";

    public interface Table{
        String CATEGORY = "Category";
        String PLACE = "Place";
        String IMAGE = "Image";
        String USER = "User";
    }

    public interface ColumnUser{
        String NAME = "name";
        String LAST_NAME = "lastName";
        String PHONE = "phone";
        String EMAIL = "email";
        String IMAGE_PROFILE = "imageProfile";
    }

    public interface ColumnCategory{
        String NAME = "name";
        String LOGO = "logo";
        String CREATED_AT = "createdAt";
        String DESCRIPTION = "description";
    }

    public interface ColumnPlace{
        String NAME = "name";
        String ADDRESS = "address";
        String LATITUDE = "latitude";
        String LONGITUDE = "longitude";
        String DESCRIPTION = "description";
        String CREATED_AT = "createdAt";
        String ID_CATEGORY = "idCategory";
        String ID_USER = "idUser";
    }

    public interface ColumnImage{
        String PATH = "path";
        String DESCRIPTION = "description";
        String ID_PLACE = "idPlace";
    }
}
