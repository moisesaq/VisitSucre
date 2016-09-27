package com.apaza.moises.visitsucre.web.api;

import org.json.JSONObject;

public interface ApiClientResponseListener {
    void onProcess();

    void onSuccess(int statusCode, JSONObject response);

    void onFailure(int statusCode, JSONObject response);

    void onError(Throwable error);
}
