package com.apaza.moises.visitsucre.provider.deprecated;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

public class PlaceProvider extends ContentProvider {

    private static final String DATA_BASE_NAME = "DBPlace";
    private static final int DB_VERSION = 1;

    private DataBaseHelper dataBaseHelper;

    @Override
    public boolean onCreate() {
        dataBaseHelper = new DataBaseHelper(getContext(), DATA_BASE_NAME, null, DB_VERSION);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
        int match = PlaceContract.uriMatcher.match(uri);

        Cursor cursor;

        switch (match){
            case PlaceContract.ALL_ROWS:
                cursor = db.query(PlaceContract.PLACE, projection,
                        selection, selectionArgs,
                        null, null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(), PlaceContract.CONTENT_URI);
                break;
            case PlaceContract.SINGLE_ROWS:
                long idPlace = ContentUris.parseId(uri);
                cursor = db.query(PlaceContract.PLACE, projection,
                        PlaceContract.Columns._ID + " = " + idPlace,
                        selectionArgs, null, null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(), PlaceContract.CONTENT_URI);
                break;
            default:
                throw new IllegalArgumentException("Uri no support" + uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (PlaceContract.uriMatcher.match(uri)) {
            case PlaceContract.ALL_ROWS:
                return PlaceContract.MULTIPLE_MIME;
            case PlaceContract.SINGLE_ROWS:
                return PlaceContract.SINGLE_MIME;
            default:
                throw new IllegalArgumentException("Type of place unknown: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if(PlaceContract.uriMatcher.match(uri) != PlaceContract.ALL_ROWS){
            throw new IllegalArgumentException("URI unknown: " + uri);
        }
        ContentValues contentValues;
        if(values != null){
            contentValues = new ContentValues(values);
        }else{
            contentValues = new ContentValues();
        }

        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();

        long idPlace = db.insert(PlaceContract.PLACE, null, contentValues);

        if(idPlace > 0){
            Uri uriPlace = ContentUris.withAppendedId(PlaceContract.CONTENT_URI, idPlace);
            getContext().getContentResolver().notifyChange(uriPlace, null);
            return uriPlace;
        }
        throw new SQLiteException("Fail in insert: " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
        int match = PlaceContract.uriMatcher.match(uri);
        int affected;

        switch (match){
            case PlaceContract.ALL_ROWS:
                affected = db.delete(PlaceContract.PLACE, selection, selectionArgs);
                break;
            case PlaceContract.SINGLE_ROWS:
                long idPlace = ContentUris.parseId(uri);
                affected = db.delete(PlaceContract.PLACE, PlaceContract.Columns._ID + " = " + idPlace
                        + (!TextUtils.isEmpty(selection) ?  " AND (" + selection + ")" : ""), selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            default:
                throw new IllegalArgumentException("Element not known " + uri);
        }
        return affected;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
        int affected;
        switch (PlaceContract.uriMatcher.match(uri)) {
            case PlaceContract.ALL_ROWS:
                affected = db.update(PlaceContract.PLACE, values, selection, selectionArgs);
                break;
            case PlaceContract.SINGLE_ROWS:
                String placeId = uri.getPathSegments().get(1);
                affected = db.update(PlaceContract.PLACE, values,
                        PlaceContract.Columns._ID + "=" + placeId
                                + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("URI unknown: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return affected;
    }

}
