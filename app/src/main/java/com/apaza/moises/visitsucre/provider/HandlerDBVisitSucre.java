package com.apaza.moises.visitsucre.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class HandlerDBVisitSucre {

    private static DBVisitSucreHelper dbVisitSucreHelper;

    private static HandlerDBVisitSucre handler;

    private HandlerDBVisitSucre(){

    }

    public static HandlerDBVisitSucre getInstance(Context context){
        if(dbVisitSucreHelper == null)
            dbVisitSucreHelper = new DBVisitSucreHelper(context);

        if(handler == null)
            handler = new HandlerDBVisitSucre();
        return handler;
    }

    public Cursor getCategories(){
        SQLiteDatabase db = dbVisitSucreHelper.getWritableDatabase();
        String sql = String.format("SELECT * FROM %s", DBVisitSucreHelper.Table.CATEGORY);
        return db.rawQuery(sql, null);
    }

    public Cursor getCategoryWithId(String idCategory){
        SQLiteDatabase db = dbVisitSucreHelper.getWritableDatabase();
        String sql = String.format("SELECT * FROM %s WHERE %s=?", DBVisitSucreHelper.Table.CATEGORY, ContractVisitSucre.Category.ID);
        String[] selectionArgs = {idCategory};
        return db.rawQuery(sql, selectionArgs);
    }

    public String insertCategory(Category category){
        SQLiteDatabase db = dbVisitSucreHelper.getWritableDatabase();
        String idCategory = ContractVisitSucre.Category.generateIdCategory();

        ContentValues values = new ContentValues();
        values.put(ContractVisitSucre.Category.ID, idCategory);
        values.put(ContractVisitSucre.Category.CODE, category.getCode());
        values.put(ContractVisitSucre.Category.LOGO, category.getLogo());
        values.put(ContractVisitSucre.Category.NAME, category.getName());
        values.put(ContractVisitSucre.Category.DATE, category.getDate().toString());
        values.put(ContractVisitSucre.Category.DESCRIPTION, category.getDescription());

        db.insertOrThrow(DBVisitSucreHelper.Table.CATEGORY, null, values);
        return idCategory;
    }

    public boolean updateCategory(Category category){
        SQLiteDatabase db = dbVisitSucreHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ContractVisitSucre.Category.CODE, category.getCode());
        values.put(ContractVisitSucre.Category.LOGO, category.getLogo());
        values.put(ContractVisitSucre.Category.NAME, category.getName());
        values.put(ContractVisitSucre.Category.DATE, category.getDate().toString());
        values.put(ContractVisitSucre.Category.DESCRIPTION, category.getDescription());

        String whereClause = String.format("%s=?", ContractVisitSucre.Category.ID);
        String[] whereArgs = {category.getIdCategory()};

        int result = db.update(DBVisitSucreHelper.Table.CATEGORY, values, whereClause, whereArgs);

        return result > 0;
    }

    public boolean deleteCategory(String idCategory){
        SQLiteDatabase db = dbVisitSucreHelper.getWritableDatabase();

        String whereClause = String.format("%s=?", ContractVisitSucre.Category.ID);
        String[] whereArgs = {idCategory};

        int result = db.delete(DBVisitSucreHelper.Table.CATEGORY, whereClause, whereArgs);
        return result > 0;
    }

    public Cursor getPlaces(){
        SQLiteDatabase db = dbVisitSucreHelper.getWritableDatabase();
        String sql = String.format("SELECT * FROM %s", DBVisitSucreHelper.Table.PLACE);
        return db.rawQuery(sql, null);
    }

    public Cursor getPlaceWithId(String idPlace){
        SQLiteDatabase db = dbVisitSucreHelper.getWritableDatabase();
        String sql = String.format("SELECT * FROM %s WHERE %s=?", DBVisitSucreHelper.Table.PLACE, ContractVisitSucre.Place.ID);
        String[] selectArgs = {idPlace};
        return db.rawQuery(sql, selectArgs);
    }

    public String insertPlace(Place place){
        SQLiteDatabase db = dbVisitSucreHelper.getWritableDatabase();
        String idPlace = ContractVisitSucre.Place.generateIdPlace();

        ContentValues values = new ContentValues();
        values.put(ContractVisitSucre.Place.ID, idPlace);
        values.put(ContractVisitSucre.Place.CODE, place.getCode());
        values.put(ContractVisitSucre.Place.NAME, place.getName());
        values.put(ContractVisitSucre.Place.ADDRESS, place.getAddress());
        values.put(ContractVisitSucre.Place.LATITUDE, place.getLatitude());
        values.put(ContractVisitSucre.Place.LONGITUDE, place.getLongitude());
        values.put(ContractVisitSucre.Place.DESCRIPTION, place.getDescription());
        values.put(ContractVisitSucre.Place.PATH_IMAGE, place.getPathImage());
        values.put(ContractVisitSucre.Place.DATE, place.getDate().toString());
        values.put(ContractVisitSucre.Place.ID_CATEGORY, place.getIdCategory());

        db.insertOrThrow(DBVisitSucreHelper.Table.PLACE, null, values);

        return idPlace;
    }

    public boolean updatePlace(Place place){
        SQLiteDatabase db = dbVisitSucreHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ContractVisitSucre.Place.CODE, place.getCode());
        values.put(ContractVisitSucre.Place.NAME, place.getName());
        values.put(ContractVisitSucre.Place.ADDRESS, place.getAddress());
        values.put(ContractVisitSucre.Place.LATITUDE, place.getLatitude());
        values.put(ContractVisitSucre.Place.LONGITUDE, place.getLongitude());
        values.put(ContractVisitSucre.Place.DESCRIPTION, place.getDescription());
        values.put(ContractVisitSucre.Place.PATH_IMAGE, place.getPathImage());
        values.put(ContractVisitSucre.Place.DATE, place.getDate().toString());
        values.put(ContractVisitSucre.Place.ID_CATEGORY, place.getIdCategory());

        String whereClause = String.format("%s=?", ContractVisitSucre.Place.ID);
        String[] whereArgs = {place.getIdPlace()};

        int result =db.update(DBVisitSucreHelper.Table.PLACE, values,whereClause, whereArgs);

        return result > 0;
    }

    public boolean deletePlace(String idPlace){
        SQLiteDatabase db = dbVisitSucreHelper.getWritableDatabase();
        String whereClause = String.format("%s=?", ContractVisitSucre.Place.ID);
        String[] whereArgs = {idPlace};

        int result = db.delete(DBVisitSucreHelper.Table.PLACE, whereClause, whereArgs);
        return result > 0;
    }

    public SQLiteDatabase getDB(){
        return dbVisitSucreHelper.getWritableDatabase();
    }
}
