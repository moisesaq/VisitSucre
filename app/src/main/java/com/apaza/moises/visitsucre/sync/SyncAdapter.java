package com.apaza.moises.visitsucre.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;

import com.apaza.moises.visitsucre.R;
import com.apaza.moises.visitsucre.global.Global;
import com.apaza.moises.visitsucre.provider.ContractVisitSucre;
import com.google.gson.Gson;

public class SyncAdapter extends AbstractThreadedSyncAdapter{
    private static final String TAG = SyncAdapter.class.getSimpleName();

    ContentResolver resolver;
    private Gson gson = new Gson();

    /*PROJECTION FOR CONSULT*/
    private static final String[] PROJECTION = new String[]{
            BaseColumns._ID,
            ContractVisitSucre.Category.ID_REMOTE,
            ContractVisitSucre.Category.NAME,
            ContractVisitSucre.Category.LOGO,
            ContractVisitSucre.Category.DATE,
            ContractVisitSucre.Category.DESCRIPTION
    };

    /*INDEX FOR COLUMNS PROJECTION*/
    public static final int COLOMN_ID = 0;
    public static final int COLUMN_ID_REMOTE = 1;
    public static final int COLUMN_NAME = 2;
    public static final int COLUMN_LOGO = 3;
    public static final int COLUMN_DATE = 4;
    public static final int COLUMN_DESCRIPTION = 5;

    public SyncAdapter(Context context, boolean autoInitialize{
        super(context, autoInitialize);
        resolver = context.getContentResolver();
    }

    /*COMPATIBILITY VERSION 3.0*/
    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSync){
        super(context, autoInitialize, allowParallelSync);
        resolver = context.getContentResolver();
    }

    public static void setupSyncAdapter(Context context){

    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, SyncResult syncResult) {

    }

    public static Account getAccountToSync(Context context){
        AccountManager accountManager = (AccountManager)context.getSystemService(Context.ACCOUNT_SERVICE);
        Account newAccount = new Account(context.getString(R.string.app_name), Global.ACCOUNT_TYPE);

        if(accountManager.getPassword(newAccount) == null){
            if(!accountManager.addAccountExplicitly(newAccount, "", null))
                return null;
        }
        Log.i(TAG, "ACCOUNT USER OBTAINED");
        return newAccount;
    }
}
