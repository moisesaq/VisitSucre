package com.apaza.moises.visitsucre.provider;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;

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
    public static final int DETAILED_PLACES = 203;

    public static final String AUTHORITY = "com.apaza.moises.visitsucre";

    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(AUTHORITY, ContractVisitSucre.ROUTE_CATEGORY, CATEGORIES);
        uriMatcher.addURI(AUTHORITY, ContractVisitSucre.ROUTE_CATEGORY+"/*", CATEGORY_ID);

        uriMatcher.addURI(AUTHORITY, ContractVisitSucre.ROUTE_PLACE, PLACES);
        uriMatcher.addURI(AUTHORITY, ContractVisitSucre.ROUTE_PLACE + "/detailed", DETAILED_PLACES);
        uriMatcher.addURI(AUTHORITY, ContractVisitSucre.ROUTE_PLACE + "/*", PLACE_ID);
        uriMatcher.addURI(AUTHORITY, ContractVisitSucre.ROUTE_PLACE + "/*/detail", PLACE_ID_DETAIL);
    }

    private static final String PLACE_JOIN_CATEGORY = "place as place INNER JOIN category as category " +
            "ON place.idCategory = category.id";

    private final String[] projectionPlace = new String[]{
            DBVisitSucreHelper.Table.PLACE + "." + ContractVisitSucre.Place.ID,
            DBVisitSucreHelper.Table.PLACE + "." + ContractVisitSucre.Place.NAME,
            DBVisitSucreHelper.Table.PLACE + "." + ContractVisitSucre.Place.DESCRIPTION,
            DBVisitSucreHelper.Table.CATEGORY + "." + ContractVisitSucre.Category.NAME
    };

    @Override
    public boolean onCreate(){
        helper = new DBVisitSucreHelper(getContext());
        if(getContext() != null)
            resolver = getContext().getContentResolver();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String id;
        Cursor cursor;
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        switch (uriMatcher.match(uri)){
            case CATEGORIES:
                cursor = db.query(DBVisitSucreHelper.Table.CATEGORY, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case CATEGORY_ID:
                id = ContractVisitSucre.Category.getIdCategory(uri);
                cursor = db.query(DBVisitSucreHelper.Table.CATEGORY, projection,
                        ContractVisitSucre.Category.ID + "=" + "\'" + id + "\'" + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : ""),
                        selectionArgs, null , null, sortOrder);
                break;
            case PLACES:
                cursor = db.query(DBVisitSucreHelper.Table.PLACE, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case PLACE_ID:
                id = ContractVisitSucre.Place.getIdPlace(uri);
                cursor = db.query(DBVisitSucreHelper.Table.PLACE, projection,
                        ContractVisitSucre.Place.ID + "=" + "\'" + id +"\'" + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : ""),
                        selectionArgs, null, null, sortOrder);
                break;

            case PLACE_ID_DETAIL:
                id = ContractVisitSucre.Place.getIdPlaceForDetail(uri);
                builder.setTables(PLACE_JOIN_CATEGORY);
                cursor = builder.query(db, projectionPlace,
                        DBVisitSucreHelper.Table.PLACE + "." + ContractVisitSucre.Place.ID + "=" + "\'" + id +"\'" + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : "") ,
                        selectionArgs, null, null, sortOrder);
                break;

            case DETAILED_PLACES:
                String filter = ContractVisitSucre.Place.hasFilter(uri) ? createFilter(uri.getQueryParameter(ContractVisitSucre.Place.PARAMS_FILTER)) : null;
                builder.setTables(PLACE_JOIN_CATEGORY);
                cursor = builder.query(db, projectionPlace, selection, selectionArgs, null, null, filter);//sortOrder);
                break;

            default:
                throw new UnsupportedOperationException(NO_SUPPORTED_URI);
        }
        cursor.setNotificationUri(resolver, uri);
        return cursor;
    }

    private String createFilter(String filter){
        String sequence = null;
        switch (filter){
            case ContractVisitSucre.Place.FILTER_PLACE_DATE:
                sequence = DBVisitSucreHelper.Table.PLACE + "." + ContractVisitSucre.ColumnsPlace.DATE;
                break;
            case ContractVisitSucre.Place.FILTER_CATEGORY:
                sequence = DBVisitSucreHelper.Table.CATEGORY + "." + ContractVisitSucre.ColumnsCategory.NAME;
                break;
        }
        return sequence;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            case CATEGORIES:
                return ContractVisitSucre.generateMime(DBVisitSucreHelper.Table.CATEGORY);
            case CATEGORY_ID:
                return ContractVisitSucre.generateMimeItem(DBVisitSucreHelper.Table.CATEGORY);
            case PLACES:
                return ContractVisitSucre.generateMime(DBVisitSucreHelper.Table.PLACE);
            case PLACE_ID:
                return ContractVisitSucre.generateMimeItem(DBVisitSucreHelper.Table.PLACE);
            default:
                throw new UnsupportedOperationException("Uri unknown: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.d(TAG, "INSERT: " + uri + "( " + values.toString() + ")\n");
        SQLiteDatabase db = helper.getWritableDatabase();
        String id = null;
        switch (uriMatcher.match(uri)){
            case CATEGORIES:
                if(values.getAsString(ContractVisitSucre.Category.ID) == null){
                    id = ContractVisitSucre.Category.generateIdCategory();
                    values.put(ContractVisitSucre.Category.ID, id);
                }
                db.insertOrThrow(DBVisitSucreHelper.Table.CATEGORY, null, values);
                notifyChange(uri);
                return ContractVisitSucre.Category.createUriCategory(id);
            case PLACES:
                if(values.getAsString(ContractVisitSucre.Place.ID) == null){
                    id = ContractVisitSucre.Place.generateIdPlace();
                    values.put(ContractVisitSucre.Place.ID, id);
                }
                db.insertOrThrow(DBVisitSucreHelper.Table.PLACE, null, values);
                notifyChange(uri);
                return ContractVisitSucre.Place.createUriPlace(id);
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        //db.delete(DBVisitSucreHelper.Table.CATEGORY, whereClause, whereArgs)
        Log.d(TAG, "Delete: " + uri);
        SQLiteDatabase db = helper.getWritableDatabase();
        String id;
        int affects;

        switch (uriMatcher.match(uri)){
            case CATEGORY_ID:
                id = ContractVisitSucre.Category.getIdCategory(uri);
                affects = db.delete(DBVisitSucreHelper.Table.CATEGORY, ContractVisitSucre.Category.ID_REMOTE + " = ?", new String[]{id});
                notifyChange(uri);
                break;
            case PLACE_ID:
                id = ContractVisitSucre.Place.getIdPlace(uri);
                affects = db.delete(DBVisitSucreHelper.Table.CATEGORY, ContractVisitSucre.Place.ID + " = ?", new String[]{id});
                notifyChange(uri);
                break;
            default:
                throw new UnsupportedOperationException(NO_SUPPORTED_URI);
        }
        return affects;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Log.d(TAG, "UPDATE: " + uri);
        SQLiteDatabase db = helper.getWritableDatabase();
        String id;
        int affects;
        switch (uriMatcher.match(uri)){
            case CATEGORIES:
                affects = db.update(DBVisitSucreHelper.Table.CATEGORY, values, selection, selectionArgs);
                break;
            case CATEGORY_ID:
                id = ContractVisitSucre.Category.getIdCategory(uri);
                affects = db.update(DBVisitSucreHelper.Table.CATEGORY, values, ContractVisitSucre.Category.ID_REMOTE + " = ?", new String[]{id});
                notifyChange(uri);
                break;
            case PLACE_ID:
                id = ContractVisitSucre.Place.getIdPlace(uri);
                affects = db.update(DBVisitSucreHelper.Table.PLACE, values, ContractVisitSucre.Place.ID + " = ?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException(NO_SUPPORTED_URI);
        }
        resolver.notifyChange(uri, null, false);
        return affects;
    }

    private void notifyChange(Uri uri){
        resolver.notifyChange(uri, null);
    }

    @Override
    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations) throws OperationApplicationException{
        final SQLiteDatabase db = helper.getWritableDatabase();
        db.beginTransaction();
        try{
            final int numOperations = operations.size();
            final ContentProviderResult[] results = new ContentProviderResult[numOperations];
            for (int i = 0; i < numOperations; i++){
                results[i] = operations.get(i).apply(this, results, i);
            }
            db.setTransactionSuccessful();
            return results;
        }finally {
            db.endTransaction();
        }
    }
}
