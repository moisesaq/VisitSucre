package com.apaza.moises.visitsucre.provider;

import android.net.Uri;

import com.apaza.moises.visitsucre.database.CategoryDao;
import com.apaza.moises.visitsucre.database.ImageDao;
import com.apaza.moises.visitsucre.database.PlaceDao;

import java.util.UUID;

public class ContractVisitSucre {
    public static final String NAME_DB = "visitSucreDataBase";

    public static final String TABLE_NAME_CATEGORY = CategoryDao.TABLENAME;
    public static final String PK_CATEGORY = CategoryDao.Properties.Id.columnName;

    public static final String TABLE_NAME_PLACE = PlaceDao.TABLENAME;
    public static final String PK_PLACE = PlaceDao.Properties.Id.columnName;

    public static final String TABLE_NAME_IMAGE = ImageDao.TABLENAME;
    public static final String PK_IMAGE = ImageDao.Properties.Id.columnName;

    public static final int STATUS_OK = 0;
    public static final int STATUS_SYNC = 1;

    public interface Table{
        String CATEGORY = "category";
        String PLACE = "place";
    }

    public interface ColumnsCategory{
        String ID = "id";
        String LOGO = "logo";
        String NAME = "name";
        String DATE = "date";
        String DESCRIPTION = "description";

        String STATUS = "status";
        String ID_REMOTE = "idRemote";
        String PENDING_INSERTION = "pendingInsertion";
    }

    public interface ColumnsPlace{
        String ID = "id";
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

    public static final String ROUTE_CATEGORY = "route_category";
    public static final String ROUTE_PLACE = "route_place";
    public static final String ROUTE_IMAGE = "route_image";
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

        public static Uri createUriCategory(String id){
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
        public static final Uri CONTENT_URI_DETAILED = CONTENT_URI.buildUpon().appendPath("detailed").build();

        public static final String PARAMS_FILTER = "filter";
        public static final String FILTER_PLACE_DATE = "CREATED_AT";
        public static final String FILTER_CATEGORY = "CATEGORY";

        public static Uri createUriPlace(String id){
            return CONTENT_URI.buildUpon().appendPath(id).build();
        }

        public static String getIdPlace(Uri uri){
            return uri.getLastPathSegment();
        }

        public static String getIdPlaceForDetail(Uri uri){
            return uri.getPathSegments().get(1);
        }

        public static Uri createUriForDetail(String id){
            return CONTENT_URI.buildUpon().appendPath(id).appendPath("detail").build();
        }

        public static String generateIdPlace(){
            return "PLA-" + UUID.randomUUID().toString();
        }

        public static boolean hasFilter(Uri uri){
            return uri != null && uri.getQueryParameter(PARAMS_FILTER) != null;
        }
    }

    public static class Image{
        public static final Uri CONTENT_URI = URI_BASE.buildUpon().appendPath(ROUTE_IMAGE).build();

        public static Uri createUriImage(String id){
            return CONTENT_URI.buildUpon().appendPath(id).build();
        }

        public static String getIdImage(Uri uri){
            return uri.getLastPathSegment();
        }
    }
}
