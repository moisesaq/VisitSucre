package com.apaza.moises.visitsucre.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.BaseColumns;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.apaza.moises.visitsucre.R;
import com.apaza.moises.visitsucre.global.Constants;
import com.apaza.moises.visitsucre.global.VolleySingleton;
import com.apaza.moises.visitsucre.provider.Category;
import com.apaza.moises.visitsucre.provider.ContractVisitSucre;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SyncAdapter extends AbstractThreadedSyncAdapter{
    private static final String TAG = SyncAdapter.class.getSimpleName();

    ContentResolver resolver;
    private Gson gson = new Gson();

    /*PROJECTION FOR CONSULT*/
    private static final String[] PROJECTION = new String[]{
            BaseColumns._ID,
            ContractVisitSucre.Category.ID_REMOTE,
            ContractVisitSucre.Category.CODE,
            ContractVisitSucre.Category.LOGO,
            ContractVisitSucre.Category.NAME,
            ContractVisitSucre.Category.DATE,
            ContractVisitSucre.Category.DESCRIPTION
    };

    /*INDEX FOR COLUMNS PROJECTION*/
    public static final int COLOMN_ID = 0;
    public static final int COLUMN_ID_REMOTE = 1;
    public static final int COLUMN_CODE = 2;
    public static final int COLUMN_LOGO = 3;
    public static final int COLUMN_NAME = 4;
    public static final int COLUMN_DATE = 5;
    public static final int COLUMN_DESCRIPTION = 6;

    public SyncAdapter(Context context, boolean autoInitialize){
        super(context, autoInitialize);
        resolver = context.getContentResolver();
    }

    /*COMPATIBILITY VERSION 3.0*/
    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSync){
        super(context, autoInitialize, allowParallelSync);
        resolver = context.getContentResolver();
    }

    public static void setupSyncAdapter(Context context){
        getAccountToSync(context);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, SyncResult syncResult) {
        Log.i(TAG, "onPerformSync()...");

        boolean onlyLoad = extras.getBoolean(ContentResolver.SYNC_EXTRAS_UPLOAD, false);

        if(!onlyLoad){
            performSyncLocal(syncResult);
        }else {
            //performSyncRemote();
        }
    }

    private void performSyncLocal(final SyncResult syncResult){
        Log.i(TAG, "Updating client....");

        VolleySingleton.getInstance(getContext()).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.GET,
                        Constants.URL_CATEGORIES,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {
                                processResponseGet(jsonObject, syncResult);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i(TAG, error.networkResponse.toString());
                            }
                        }
                )
        );
    }

    private void processResponseGet(JSONObject response, SyncResult syncResult){
        try{
            String status = response.getString(Constants.STATUS);
            switch (status){
                case Constants.SUCESS:
                    updateDataLocal(response, syncResult);
                    break;
                case Constants.FAILED:
                    break;
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void updateDataLocal(JSONObject response, SyncResult syncResult) {
        JSONArray jsonCategories = null;
        try {
            jsonCategories = response.getJSONArray(Constants.CATEGORIES);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Parser with Gson
        Category[] res = gson.fromJson(jsonCategories != null ? jsonCategories.toString() : null, Category[].class);
        List<Category> data = Arrays.asList(res);

        /*LIST FOR GET PENDING OPERATIONS*/
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();

        /*TABLE HASH FOR CONTAIN INPUT*/
        HashMap<String, Category> categoryMap = new HashMap<>();
        for(Category category: data){
            //TODO CHECK AFTER THIS
            //FIXME THIS IS ID REMOTE
            categoryMap.put(category.getIdCategory(), category);
        }

        /*CONSULT CURRENT REGISTER REMOTES*/
        Uri uri = ContractVisitSucre.Category.CONTENT_URI;
        String select = ContractVisitSucre.Category.ID_REMOTE + "IS NOT NULL";

        //TODO VERIFY AFTER THIS ALSO
        Cursor c = resolver.query(uri, PROJECTION, select, null, null);

        assert c != null;

        Log.i(TAG, "FOUND " + c.getCount() + " REGISTER LOCAL");

        /*FIND DATA OBSOLETE*/
        String idRemote;
        String code;
        String logo;
        String name;
        String date;
        String description;
        while (c.moveToNext()){
            syncResult.stats.numEntries++;

            idRemote = c.getString(COLUMN_ID_REMOTE);
            code = c.getString(COLUMN_CODE);
            logo = c.getString(COLUMN_LOGO);
            name = c.getString(COLUMN_CODE);
            date = c.getString(COLUMN_DATE);
            description = c.getString(COLUMN_DESCRIPTION);

            Category match = categoryMap.get(idRemote);
            if(match != null){
                categoryMap.remove(idRemote);

                Uri existingUri = ContractVisitSucre.Category.CONTENT_URI.buildUpon().appendPath(idRemote).build();

                /*VERIFY UPDATE CATEGORY*/
                boolean b = match.getCode() != null && !match.getCode().equals(code);
                boolean b1 = match.getLogo() != null && !match.getLogo().equals(logo);
                boolean b2 = match.getName() != null && !match.getName().equals(name);
                boolean b3 = match.getDate() != null && !match.getDate().equals(date);
                boolean b4 = match.getDescription() != null && !match.getDescription().equals(description);

                if(b || b1 || b2 || b3 || b4){
                    Log.i(TAG, "Programming update of: " + existingUri);
                    ops.add(ContentProviderOperation.newUpdate(existingUri)
                            .withValue(ContractVisitSucre.Category.CODE, match.getCode())
                            .withValue(ContractVisitSucre.Category.LOGO, match.getLogo())
                            .withValue(ContractVisitSucre.Category.NAME, match.getName())
                            .withValue(ContractVisitSucre.Category.DATE, match.getDate())
                            .withValue(ContractVisitSucre.Category.DESCRIPTION, match.getDescription())
                            .build());
                    syncResult.stats.numUpdates++;
                }else {
                    Log.i(TAG, "There are not action for this register: " + existingUri);
                }
            }else{
                Uri deleteUri = ContractVisitSucre.Category.CONTENT_URI.buildUpon().appendPath(idRemote).build();
                Log.i(TAG, "Programming deleting of: " + deleteUri);
                ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        /*INSERT DATA RESULTS*/
        for (Category ca: categoryMap.values()){
            Log.i(TAG, "Programming inserting of: " + ca.getIdCategory());
            ops.add(ContentProviderOperation.newInsert(ContractVisitSucre.Category.CONTENT_URI)
                    .withValue(ContractVisitSucre.Category.ID_REMOTE, ca.getIdCategory()) //TODO SEE HERE
                    .withValue(ContractVisitSucre.Category.CODE, ca.getCode())
                    .withValue(ContractVisitSucre.Category.LOGO, ca.getLogo())
                    .withValue(ContractVisitSucre.Category.NAME, ca.getName())
                    .withValue(ContractVisitSucre.Category.DATE, ca.getDate())
                    .withValue(ContractVisitSucre.Category.DESCRIPTION, ca.getDescription())
                    .build());
            syncResult.stats.numInserts++;
        }

        if(syncResult.stats.numInserts > 0 ||
                syncResult.stats.numUpdates > 0 ||
                syncResult.stats.numDeletes > 0){
            Log.i(TAG, "Start operations...");
            try{
                resolver.applyBatch(ContractVisitSucre.AUTHORITY, ops);
            }catch (RemoteException |OperationApplicationException e){
                e.printStackTrace();
            }

            resolver.notifyChange(
                    ContractVisitSucre.Category.CONTENT_URI,
                    null,
                    false);

            Log.i(TAG, "Sync finished.");
        }else{
            Log.i(TAG, "It is not require sync");
        }
    }

    public static void sincronizeNow(Context context, boolean onlyUpload){
        Log.d(TAG, "REQUEST OF SYNCHRONIZE MANUAL.");
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        if(onlyUpload)
            bundle.putBoolean(ContentResolver.SYNC_EXTRAS_UPLOAD, true);
        ContentResolver.requestSync(getAccountToSync(context), context.getString(R.string.provider_authority), bundle);
    }

    public static Account getAccountToSync(Context context){
        AccountManager accountManager = (AccountManager)context.getSystemService(Context.ACCOUNT_SERVICE);
        Account newAccount = new Account(context.getString(R.string.app_name), Constants.ACCOUNT_TYPE);

        if(accountManager.getPassword(newAccount) == null){
            if(!accountManager.addAccountExplicitly(newAccount, "", null))
                return null;
        }
        Log.i(TAG, "ACCOUNT USER OBTAINED");
        return newAccount;
    }
}
