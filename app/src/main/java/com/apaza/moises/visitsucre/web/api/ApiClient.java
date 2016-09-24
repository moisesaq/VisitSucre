package com.apaza.moises.visitsucre.web.api;

import android.util.Log;

import com.android.volley.VolleyError;
import com.apaza.moises.visitsucre.web.api.volley.VolleyClient;
import com.apaza.moises.visitsucre.web.api.volley.VolleyClientResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ApiClient {

    public static String TAG = "API CLIENT";
    private VolleyClient volleyClient = new VolleyClient();
    protected String url;

    protected ApiClient(String url){
        this.url = url;
    }

    private VolleyClientResponseHandler handleResponse(final ApiClientResponseListener apiClientResponseListener){

        apiClientResponseListener.onProcess();

        return new VolleyClientResponseHandler() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                int status = 0;
                try{
                    status = jsonObject.getInt("status");
                }catch (JSONException e){
                    e.printStackTrace();
                }

                if(status == 100 || status == 200){
                    apiClientResponseListener.onSuccess(status, jsonObject);
                }else{
                    apiClientResponseListener.onFailure(status, jsonObject);
                }
            }

            @Override
            public void onFailure(VolleyError volleyError) {
                apiClientResponseListener.onError(volleyError);
            }
        };
    }

    protected void get(String route, HashMap<String, String> headers, ApiClientResponseListener apiClientResponseListener){
        Log.d(TAG, "VOLLEY GET >>> " + url + route);
        volleyClient.get(url + route, headers, handleResponse(apiClientResponseListener));
    }

    protected void get(String route, JSONObject params, ApiClientResponseListener apiClientResponseListener){
        Log.d(TAG, "VOLLEY GET >>> " + url + route);
        volleyClient.get(url + route, params, handleResponse(apiClientResponseListener));
    }

    protected void post(String route, JSONObject params, ApiClientResponseListener apiClientResponseListener){
        volleyClient.post(url + route, params, handleResponse(apiClientResponseListener));
    }

    public interface API{
        String USER = "usersApi/";
        String OPERATION = "operationApi/";
        String TRIP = "tripApi/";

        String LOGIN = USER + "login/";
        String CREATE_TRIP = OPERATION + "createTrip/";
        String TRIP_HISTORY = TRIP + "getOperationHistory/";
        String SCHEDULED_HISTORY = TRIP + "getOperationScheduled/";
    }
}
