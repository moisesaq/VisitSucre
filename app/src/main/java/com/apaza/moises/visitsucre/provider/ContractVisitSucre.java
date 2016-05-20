package com.apaza.moises.visitsucre.provider;

import android.net.Uri;

import java.util.UUID;

public class ContractVisitSucre {
    /* CATEGORY
    code: {type: String},
    logo: {type: String},
    name: {type: String},
    date: {type: String},
    description: {type: String}*/

    /*PLACE
    code: {type: String},
    name: { type: String },
    address: { type: String },
    latitude: {type: String},
    longitude: {type: String},
    description: { type: String},
    pathImage:[{ type: String}],
    date: {type: String},
    idCategory: {type: Schema.ObjectId, ref: "Category" }*/

    interface ColumnsCategory{
        String ID = "id";
        String CODE = "code";
        String LOGO = "logo";
        String NAME = "name";
        String DATE = "date";
        String DESCRIPTION = "description";
    }

    interface ColumnsPlace{
        String ID = "id";
        String CODE = "code";
        String NAME = "name";
        String ADDRESS = "address";
        String LATITUDE = "latitude";
        String LONGITUDE = "longitude";
        String DESCRIPTION = "description";
        String PATH_IMAGE = "pathImage";
        String DATE = "date";
        String ID_CATEGORY = "idCategory";
    }

    //URIS
    public static final String AUTHORITY = "com.apaza.moises.visitsucre";
    public static final Uri URI_BASE = Uri.parse("content://" + AUTHORITY);

    private static final String ROUTE_CATEGORY = "route_category";
    private static final String ROUTE_PLACE = "route_place";
    //END URIS

    //TYPES MIME
    public static final String CONTENTS_BASE = "visitsucre.";
    public static final String TYPE_CONTENT = "vnd.android.cursor.dir/vnd." + CONTENTS_BASE;
    public static final String TYPE_CONTENT_ITEM = "vnd.android.cursor.item/vnd." + CONTENTS_BASE;

    public static String generateMime(String id){
        if(id != null)
            return TYPE_CONTENT + id;
        else
            return null;
    }

    public static String generateMimeItem(String id){
        if(id != null)
            return TYPE_CONTENT_ITEM + id;
        else
            return null;
    }
    //END MIME

    public static class Category implements ColumnsCategory{
        public static final Uri CONTENT_URI = URI_BASE.buildUpon().appendPath(ROUTE_CATEGORY).build();

        public static Uri createUriCAtegory(String id){
            return CONTENT_URI.buildUpon().appendPath(id).build();
        }

        public static String generateIdCategory(){
            return "CA-" + UUID.randomUUID().toString();
        }

        public static String getIdCategory(Uri uri){
            return uri.getLastPathSegment();
        }

    }

    public static class Place implements ColumnsPlace{
        public static final Uri CONTENT_URI = URI_BASE.buildUpon().appendPath(ROUTE_PLACE).build();
        public static final String PARAMS_FILTER = "filter";

        public static Uri createUriPlace(String id){
            return CONTENT_URI.buildUpon().appendPath(id).build();
        }

        public static String getIdPlace(Uri uri){
            return uri.getPathSegments().get(0);
        }

        public static Uri createUriForDetail(String id){
            return CONTENT_URI.buildUpon().appendPath(id).appendPath("detail").build();
        }

        public static String generateIdPlace(){
            return "PLA-" + UUID.randomUUID().toString();
        }
    }
}
