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
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.apaza.moises.visitsucre.database.CategoryDao;
import com.apaza.moises.visitsucre.database.DaoSession;
import com.apaza.moises.visitsucre.database.PlaceDao;
import com.apaza.moises.visitsucre.deprecated.DBVisitSucreHelper;

import java.util.ArrayList;

import de.greenrobot.dao.DaoLog;

public class ProviderSucre extends ContentProvider{

    public static final String TAG = "PROVIDER VISIT SUCRE";
    public static final String NO_SUPPORTED_URI = "NO SUPPORTED URI";

    private ContentResolver resolver;

    public ProviderSucre(){

    }

    public static final UriMatcher uriMatcher;

    public static final int USER = 50;
    public static final int USER_ID = 51;

    public static final int CATEGORIES = 100;
    public static final int CATEGORY_ID = 101;

    public static final int PLACES = 200;
    public static final int PLACE_ID = 201;
    public static final int PLACE_ID_DETAIL = 202;
    public static final int DETAILED_PLACES = 203;

    public static final int IMAGES = 300;
    public static final int IMAGE_ID = 301;

    public static final String AUTHORITY = "com.apaza.moises.visitsucre";

    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(AUTHORITY, ContractVisitSucre.ROUTE_USER, USER);
        uriMatcher.addURI(AUTHORITY, ContractVisitSucre.ROUTE_USER+"/*", USER_ID);

        uriMatcher.addURI(AUTHORITY, ContractVisitSucre.ROUTE_CATEGORY, CATEGORIES);
        uriMatcher.addURI(AUTHORITY, ContractVisitSucre.ROUTE_CATEGORY+"/*", CATEGORY_ID);

        uriMatcher.addURI(AUTHORITY, ContractVisitSucre.ROUTE_PLACE, PLACES);
        uriMatcher.addURI(AUTHORITY, ContractVisitSucre.ROUTE_PLACE + "/detailed", DETAILED_PLACES);
        uriMatcher.addURI(AUTHORITY, ContractVisitSucre.ROUTE_PLACE + "/*", PLACE_ID);
        uriMatcher.addURI(AUTHORITY, ContractVisitSucre.ROUTE_PLACE + "/*/detail", PLACE_ID_DETAIL);

        uriMatcher.addURI(AUTHORITY, ContractVisitSucre.ROUTE_IMAGE, IMAGES);
        uriMatcher.addURI(AUTHORITY, ContractVisitSucre.ROUTE_IMAGE + "/*", IMAGE_ID);
    }

    private static final String PLACE_JOIN_CATEGORY = "PLACE as place INNER JOIN CATEGORY as category " +
            "ON place.ID_CATEGORY = category._id";

    private final String[] projectionPlace = new String[]{
            ContractVisitSucre.TABLE_NAME_PLACE + "." + PlaceDao.Properties.Id.columnName,
            ContractVisitSucre.TABLE_NAME_PLACE + "." + PlaceDao.Properties.Name.columnName,
            ContractVisitSucre.TABLE_NAME_PLACE + "." +PlaceDao.Properties.Address.columnName,
            ContractVisitSucre.TABLE_NAME_PLACE + "." + PlaceDao.Properties.Description.columnName,
            ContractVisitSucre.TABLE_NAME_CATEGORY + "." + CategoryDao.Properties.Name.columnName,
    };

    private static DaoSession daoSession;

    @Override
    public boolean onCreate(){
        DaoLog.d("Content Provider started");
        if(getContext() != null)
            resolver = getContext().getContentResolver();
        return true;
    }

    public static void setDaoSession(DaoSession daoSession){
        ProviderSucre.daoSession = daoSession;
    }

    protected SQLiteDatabase getDatabase() {
        if(daoSession == null) {
            throw new IllegalStateException("DaoSession must be set during content provider is active");
        }
        return daoSession.getDatabase();
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = getDatabase();
        String id;
        Cursor cursor;
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        switch (uriMatcher.match(uri)){
            case USER:
                cursor = db.query(ContractVisitSucre.TABLE_NAME_USER, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case USER_ID:
                id = ContractVisitSucre.User.getIdUser(uri);
                cursor = db.query(ContractVisitSucre.TABLE_NAME_USER, projection,
                        ContractVisitSucre.PK_USER + "=" + "\'" + id + "\'" + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : ""),
                        selectionArgs, null , null, sortOrder);
                break;
            case CATEGORIES:
                cursor = db.query(ContractVisitSucre.TABLE_NAME_CATEGORY, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case CATEGORY_ID:
                id = ContractVisitSucre.Category.getIdCategory(uri);
                cursor = db.query(ContractVisitSucre.TABLE_NAME_CATEGORY, projection,
                        ContractVisitSucre.PK_CATEGORY + "=" + "\'" + id + "\'" + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : ""),
                        selectionArgs, null , null, sortOrder);
                break;
            case PLACES:
                cursor = db.query(ContractVisitSucre.TABLE_NAME_PLACE, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case PLACE_ID:
                id = ContractVisitSucre.Place.getIdPlace(uri);
                cursor = db.query(ContractVisitSucre.TABLE_NAME_PLACE, projection,
                        ContractVisitSucre.PK_PLACE + "=" + "\'" + id +"\'" + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : ""),
                        selectionArgs, null, null, sortOrder);
                break;

            case PLACE_ID_DETAIL:
                id = ContractVisitSucre.Place.getIdPlaceForDetail(uri);
                builder.setTables(PLACE_JOIN_CATEGORY);
                cursor = builder.query(db, projectionPlace,
                        ContractVisitSucre.TABLE_NAME_PLACE + "." + ContractVisitSucre.PK_PLACE + "=" + "\'" + id +"\'" + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : "") ,
                        selectionArgs, null, null, sortOrder);
                break;

            case DETAILED_PLACES:
                String filter = ContractVisitSucre.Place.hasFilter(uri) ? createFilter(uri.getQueryParameter(ContractVisitSucre.Place.PARAMS_FILTER)) : null;
                builder.setTables(PLACE_JOIN_CATEGORY);
                cursor = builder.query(db, projectionPlace, selection, selectionArgs, null, null, filter);//sortOrder);
                break;

            case IMAGES:
                cursor = db.query(ContractVisitSucre.TABLE_NAME_IMAGE, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case IMAGE_ID:
                id = ContractVisitSucre.Image.getIdImage(uri);
                cursor = db.query(ContractVisitSucre.TABLE_NAME_IMAGE, projection,
                        ContractVisitSucre.PK_IMAGE + "=" + "\'" + id + "\'" + (!TextUtils.isEmpty(selection) ? "AND (" +selection + ")" : ""),
                        selectionArgs, null, null, sortOrder);
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
                sequence = ContractVisitSucre.Table.PLACE + "." + ContractVisitSucre.ColumnsPlace.DATE;
                break;
            case ContractVisitSucre.Place.FILTER_CATEGORY:
                sequence = ContractVisitSucre.Table.CATEGORY + "." + ContractVisitSucre.ColumnsCategory.NAME;
                break;
        }
        return sequence;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            case USER:
                return ContractVisitSucre.generateMime(ContractVisitSucre.TABLE_NAME_USER);
            case USER_ID:
                return ContractVisitSucre.generateMimeItem(ContractVisitSucre.TABLE_NAME_USER);
            case CATEGORIES:
                return ContractVisitSucre.generateMime(ContractVisitSucre.TABLE_NAME_CATEGORY);
            case CATEGORY_ID:
                return ContractVisitSucre.generateMimeItem(ContractVisitSucre.TABLE_NAME_CATEGORY);
            case PLACES:
                return ContractVisitSucre.generateMime(ContractVisitSucre.TABLE_NAME_PLACE);
            case PLACE_ID:
                return ContractVisitSucre.generateMimeItem(ContractVisitSucre.TABLE_NAME_PLACE);
            case IMAGES:
                return ContractVisitSucre.generateMime(ContractVisitSucre.TABLE_NAME_IMAGE);
            case IMAGE_ID:
                return ContractVisitSucre.generateMimeItem(ContractVisitSucre.TABLE_NAME_IMAGE);
            default:
                throw new UnsupportedOperationException("Uri unknown: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.d(TAG, "INSERT: " + uri + "( " + values.toString() + ")\n");
        SQLiteDatabase db = getDatabase();
        String id;
        switch (uriMatcher.match(uri)){
            case USER:
                id = String.valueOf(db.insertOrThrow(ContractVisitSucre.TABLE_NAME_USER, null, values));
                notifyChange(uri);
                return ContractVisitSucre.User.createUriUser(id);
            case CATEGORIES:
                id = String.valueOf(db.insertOrThrow(ContractVisitSucre.TABLE_NAME_CATEGORY, null, values));
                notifyChange(uri);
                return ContractVisitSucre.Category.createUriCategory(id);
            case PLACES:
                id = String.valueOf(db.insertOrThrow(ContractVisitSucre.TABLE_NAME_PLACE, null, values));
                notifyChange(uri);
                return ContractVisitSucre.Place.createUriPlace(id);
            case IMAGES:
                id = String.valueOf(db.insertOrThrow(ContractVisitSucre.TABLE_NAME_IMAGE, null, values));
                notifyChange(uri);
                return ContractVisitSucre.Image.createUriImage(id);
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.d(TAG, "Delete: " + uri);
        SQLiteDatabase db = getDatabase();
        String id;
        int affects;

        switch (uriMatcher.match(uri)){
            case USER:
                affects = db.delete(ContractVisitSucre.TABLE_NAME_USER, null, null);
                notifyChange(uri);
                break;
            case USER_ID:
                id = ContractVisitSucre.User.getIdUser(uri);
                affects = db.delete(ContractVisitSucre.TABLE_NAME_USER, ContractVisitSucre.PK_USER + " = ?", new String[]{id});
                notifyChange(uri);
                break;
            case CATEGORIES:
                affects = db.delete(ContractVisitSucre.TABLE_NAME_CATEGORY, null, null);
                notifyChange(uri);
                break;
            case CATEGORY_ID:
                id = ContractVisitSucre.Category.getIdCategory(uri);
                affects = db.delete(ContractVisitSucre.TABLE_NAME_CATEGORY, ContractVisitSucre.PK_CATEGORY + " = ?", new String[]{id});
                notifyChange(uri);
                break;
            case PLACES:
                affects = db.delete(ContractVisitSucre.TABLE_NAME_PLACE, null, null);
                notifyChange(uri);
                break;
            case PLACE_ID:
                id = ContractVisitSucre.Place.getIdPlace(uri);
                affects = db.delete(ContractVisitSucre.TABLE_NAME_PLACE, ContractVisitSucre.PK_PLACE + " = ?", new String[]{id});
                notifyChange(uri);
                break;
            case IMAGES:
                affects = db.delete(ContractVisitSucre.TABLE_NAME_IMAGE, null, null);
                notifyChange(uri);
                break;
            case IMAGE_ID:
                id = ContractVisitSucre.Image.getIdImage(uri);
                affects = db.delete(ContractVisitSucre.TABLE_NAME_IMAGE, ContractVisitSucre.PK_IMAGE + " = ?", new String[]{id});
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
        SQLiteDatabase db = getDatabase();
        String id;
        int affects;
        switch (uriMatcher.match(uri)){
            case USER:
                affects = db.update(ContractVisitSucre.TABLE_NAME_USER, values, selection, selectionArgs);
                break;
            case USER_ID:
                id = ContractVisitSucre.User.getIdUser(uri);
                affects = db.update(ContractVisitSucre.TABLE_NAME_USER, values, ContractVisitSucre.PK_USER + " = ?", new String[]{id});
                notifyChange(uri);
                break;
            case CATEGORIES:
                affects = db.update(ContractVisitSucre.TABLE_NAME_CATEGORY, values, selection, selectionArgs);
                break;
            case CATEGORY_ID:
                id = ContractVisitSucre.Category.getIdCategory(uri);
                affects = db.update(ContractVisitSucre.TABLE_NAME_CATEGORY, values, ContractVisitSucre.PK_CATEGORY + " = ?", new String[]{id});
                notifyChange(uri);
                break;
            case PLACE_ID:
                id = ContractVisitSucre.Place.getIdPlace(uri);
                affects = db.update(ContractVisitSucre.TABLE_NAME_PLACE, values, ContractVisitSucre.PK_PLACE + " = ?", new String[]{id});
                break;
            case IMAGE_ID:
                id = ContractVisitSucre.Image.getIdImage(uri);
                affects = db.update(ContractVisitSucre.TABLE_NAME_IMAGE, values, ContractVisitSucre.PK_IMAGE + " = ?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException(NO_SUPPORTED_URI);
        }
        resolver.notifyChange(uri, null, false);
        return affects;
    }

    public void notifyChange(Uri uri){
        resolver.notifyChange(uri, null);
    }

    @Override
    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations) throws OperationApplicationException{
        final SQLiteDatabase db = getDatabase();
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
