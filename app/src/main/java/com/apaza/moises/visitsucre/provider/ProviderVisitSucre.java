package com.apaza.moises.visitsucre.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by moises on 19/05/16.
 */
public class ProviderVisitSucre extends ContentProvider{

    public static final String TAG = "PROVIDER VISIT SUCRE";
    public static final String NO_SUPPORTED_URI = "NO SUPPORTED URI";

    private DBVisitSucreHelper helper;
    private ContentResolver resolver;

    public ProviderVisitSucre(){

    }

    public static final UriMatcher uriMatcher;

    public static final int CATEGORIES = 100;
    public static final int CATEGORY_ID = 101;

    public static final int PLACES = 200;
    public static final int PLACE_ID = 201;
    public static final int PLACE_ID_DETAIL = 202;

    public static final String AUTHORITY = "com.apaza.moises.visitsucre";

    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(AUTHORITY, DBVisitSucreHelper.Table.CATEGORY, CATEGORIES);
        uriMatcher.addURI(AUTHORITY, DBVisitSucreHelper.Table.CATEGORY+"/*", CATEGORY_ID);

        uriMatcher.addURI(AUTHORITY, DBVisitSucreHelper.Table.PLACE, PLACES);
        uriMatcher.addURI(AUTHORITY, DBVisitSucreHelper.Table.PLACE + "/*", PLACE_ID);
        uriMatcher.addURI(AUTHORITY, DBVisitSucreHelper.Table.PLACE + "/*/detail", PLACE_ID_DETAIL);
    }

    private static final String PLACE_JOIN_CATEGORY = "place INNER JOIN category " +
            "ON place.idCategory = category.id";

    private final String[] projectionPlace = new String[]{
            DBVisitSucreHelper.Table.PLACE + "." + ContractVisitSucre.Place.ID,
            ContractVisitSucre.Place.NAME,
            ContractVisitSucre.Place.DESCRIPTION,
            ContractVisitSucre.Category.NAME
    };

    @Override
    public boolean onCreate(){
        helper = new DBVisitSucreHelper(getContext());
        resolver = getContext().getContentResolver();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
