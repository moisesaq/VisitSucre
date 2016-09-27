package com.apaza.moises.visitsucre.global;

public class Constants {
    private static final String PORT_HOST = ":3000";

    public static final String BASE_URL = "http://192.168.1.43";

    public static String URL_CATEGORIES = BASE_URL + PORT_HOST + "/api/categories";
    public static String URL_PLACES = BASE_URL + PORT_HOST + "/api/places";
    public static String URL_FIND_PLACE = BASE_URL + PORT_HOST + "/api/place/find";
    public static String URL_FIND_CATEGORY = BASE_URL + PORT_HOST + "/api/category/find";
    public static String URL_IMAGE = "http://vignette2.wikia.nocookie.net/ultradragonball/images/2/28/543px-MajinBuuFatNV.png/revision/latest?cb=20110330215918";

    /*FIELDS THE RESPONSE JSON*/
    public static final String ID_CATEGORY_REMOTE = "id";
    public static final String STATUS = "status";
    public static final String CATEGORIES = "categories";
    public static final String CATEGORY = "category";
    public static final String MESSAGE = "message";

    /*CODE STATUS*/
    public static final String SUCCESS = "1";
    public static final String FAILED = "2";

    /*TYPE ACCOUNT FOR SYNC*/
    public static final String ACCOUNT_TYPE = "com.apaza.moises.visitsucre.account";
}
