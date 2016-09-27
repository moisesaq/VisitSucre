package com.apaza.moises.visitsucre.web.api.volley;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface VolleyClientResponseHandler {
    void onSuccess(JSONObject jsonObject);

    void onFailure(VolleyError volleyError);
}
