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
    }
}
