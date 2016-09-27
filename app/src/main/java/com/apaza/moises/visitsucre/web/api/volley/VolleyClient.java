package com.apaza.moises.visitsucre.web.api.volley;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class VolleyClient {
    public static final String TAG = "VOLLEY_CLIENT";

    public void get(String url, final HashMap<String, String> headers, final VolleyClientResponseHandler volleyClientResponseHandler){
        Log.d(TAG, "PARAMS RECEIVED >>> " + headers.toString());
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url /*+ "?" + urlParams*/,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, " <<< " + response.toString());
                        volleyClientResponseHandler.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, " <<< " + error.toString());
                        volleyClientResponseHandler.onFailure(error);
                    }
                })
                    {
                    @Override
                    public Map<String, String> getHeaders(){
                        return headers;
                    }
                };
        try{
            Log.d(TAG, "HEADERS >>> " + request.getHeaders().toString());
            Log.d(TAG, "BODY >>> " + request.getBodyContentType());
        }catch (Exception e){
            e.printStackTrace();
        }

        VolleySingleton.getInstance().addToRequestQueue(request);
    }

    public void get(String url, final JSONObject params, final VolleyClientResponseHandler volleyClientResponseHandler){
        Log.d(TAG, "PARAMS RECEIVED >>> " + params.toString());
        Iterator<String> keys = params.keys();
        String urlParams = "";
        while (keys.hasNext()){
            String key = keys.next();
            String param = "";
            try {
                // Strings
                Object paramObj = params.get(key);
                if(paramObj instanceof String) param = params.getString(key);
                if(paramObj instanceof Boolean) param = String.valueOf(params.getBoolean(key));
                if(paramObj instanceof Integer) param = String.valueOf(params.getInt(key));
                if(paramObj instanceof Long) param = String.valueOf(params.getLong(key));
                if(paramObj instanceof Double) param = String.valueOf(params.getDouble(key));
                if(paramObj instanceof JSONObject) param = params.getJSONObject(key).toString();

                urlParams = urlParams + key + "=" + URLEncoder.encode(param, "UTF-8");
                if(keys.hasNext()) urlParams = urlParams + "&";
                /*if(param != null && !param.isEmpty()) {
                    urlParams = urlParams + key + "=" + param;
                    if(keys.hasNext()) urlParams = urlParams + "&";
                }*/
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, "REQUEST >>> " + url + "?" + urlParams);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url + "?" + urlParams,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, " <<< " + response.toString());
                        volleyClientResponseHandler.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, " <<< " + error.toString());
                        volleyClientResponseHandler.onFailure(error);
                    }
                }) {
                @Override
                public Map<String, String> getHeaders(){
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "text/json; charset=utf-8");
                    return headers;
                }
                };
        try{
            Log.d(TAG, "HEADERS >>> " + request.getHeaders().toString());
            Log.d(TAG, "BODY >>> " + request.getBodyContentType());
        }catch (Exception e){
            e.printStackTrace();
        }
        VolleySingleton.getInstance().addToRequestQueue(request);
    }

    public void post(String url, JSONObject params, final VolleyClientResponseHandler volleyClientResponseHandler){
        Log.d(TAG, "POST >>> " + url + "?" + params.toString());

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                params,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, " <<< " + response.toString());
                        volleyClientResponseHandler.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, " <<< " + error.toString());
                        volleyClientResponseHandler.onFailure(error);
                    }
                });
        VolleySingleton.getInstance().addToRequestQueue(request);
    }
}
