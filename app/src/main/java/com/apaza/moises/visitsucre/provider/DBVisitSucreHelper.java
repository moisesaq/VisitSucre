package com.apaza.moises.visitsucre.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.provider.BaseColumns;

public class DBVisitSucreHelper extends SQLiteOpenHelper{

    private static final String NAME_DATA_BASE = "dbVisitSucre.db";
    private static final int CURRENT_VERSION = 1;
    private final Context context;

    interface Table{
        String CATEGORY = "category";
        String PLACE = "place";
    }

    private static final String tableCategory = "CREATE TABLE " + Table.CATEGORY + " (" +
            BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ContractVisitSucre.Category.ID + " TEXT UNIQUE NOT NULL, " +
            ContractVisitSucre.Category.CODE + " TEXT NOT NULL, " +
            ContractVisitSucre.Category.LOGO + " TEXT, " +
            ContractVisitSucre.Category.NAME + " TEXT NOT NULL, " +
            ContractVisitSucre.Category.DATE + " DATETIME NO NULL, " +
            ContractVisitSucre.Category.DESCRIPTION +" TEXT, " +
            ContractVisitSucre.Category.STATUS +" INTEGER NOT NULL DEFAULT " + ContractVisitSucre.STATUS_OK + ", "+
            ContractVisitSucre.Category.ID_REMOTE + " TEXT UNIQUE, " +
            ContractVisitSucre.Category.PENDING_INSERTION + " INTEGER NOT NULL DEFAULT 0)";

    interface References{
        String ID_CATEGORY = String.format("REFERENCES %s(%s) ON DELETE CASCADE", Table.CATEGORY, ContractVisitSucre.Category.ID);
    }

    public DBVisitSucreHelper(Context context){
        super(context, NAME_DATA_BASE, null, CURRENT_VERSION);
        this.context = context;
    }

    @Override
    public void onOpen(SQLiteDatabase db){
        super.onOpen(db);
        if(!db.isReadOnly()){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                db.setForeignKeyConstraintsEnabled(true);
            }else{
                db.execSQL("PRAGMA foreign_keys=ON");
            }
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        /*db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
        "%s TEXT UNIQUE NOT NULL, %s TEXT NOT NULL, %s TEXT, %s TEXT NOT NULL, %s DATETIME NO NULL, %s TEXT)",
                Table.CATEGORY, BaseColumns._ID, ContractVisitSucre.Category.ID, ContractVisitSucre.Category.CODE,
                ContractVisitSucre.Category.LOGO, ContractVisitSucre.Category.NAME, ContractVisitSucre.Category.DATE, ContractVisitSucre.Category.DESCRIPTION));*/

        db.execSQL(tableCategory);

        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "%s TEXT UNIQUE NOT NULL, %s TEXT NOT NULL, %s TEXT NOT NULL, %s TEXT NOT NULL, %s TEXT NOT NULL, %s TEXT NOT NULL, " +
                        "%s TEXT, %s TEXT NOT NULL, %s DATETIME NOT NULL, %s TEXT NOT NULL %s)",
                Table.PLACE, BaseColumns._ID, ContractVisitSucre.Place.ID,
                ContractVisitSucre.Place.CODE, ContractVisitSucre.Place.NAME,
                ContractVisitSucre.Place.ADDRESS, ContractVisitSucre.Place.LATITUDE,
                ContractVisitSucre.Place.LONGITUDE, ContractVisitSucre.Place.DESCRIPTION,
                ContractVisitSucre.Place.PATH_IMAGE, ContractVisitSucre.Place.DATE,
                ContractVisitSucre.Place.ID_CATEGORY, References.ID_CATEGORY));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        if(newVersion > oldVersion){
            db.execSQL("DROP TABLE IF EXISTS " + Table.CATEGORY);
            db.execSQL("DROP TABLE IF EXISTS " + Table.PLACE);
            onCreate(db);
        }

    }

}
