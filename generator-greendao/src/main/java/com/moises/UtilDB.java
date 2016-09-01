package com.moises;

public class UtilDB {

    public static String NAME_DB = "DataBaseVisitSucre";

    public interface Table{
        String CATEGORY = "Category";
        String PLACE = "Place";
        String IMAGE = "Image";
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
        String FAVORITE = "favorite";
        String CREATED_AT = "createdAt";
        String ID_CATEGORY = "idCategory";
    }

    public interface ColumnImage{
        String PATH = "path";
        String DESCRIPTION = "description";
        String ID_PLACE = "idPlace";
    }
}
