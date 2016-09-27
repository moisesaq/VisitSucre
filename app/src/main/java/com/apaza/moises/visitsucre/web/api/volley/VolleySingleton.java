package com.apaza.moises.visitsucre.web.api.volley;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.apaza.moises.visitsucre.global.Global;

public final class VolleySingleton {

    private static VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;

    private VolleySingleton(){
        requestQueue = getRequestQueue();
        imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> cache = new LruCache<>(20);
            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });
    }

    public static synchronized VolleySingleton getInstance(){
        if(volleySingleton == null)
            volleySingleton = new VolleySingleton();
        return volleySingleton;
    }

    public RequestQueue getRequestQueue(){
        if(requestQueue == null)
            requestQueue = Volley.newRequestQueue(Global.getContext().getApplicationContext());
        return requestQueue;
    }

    public void addToRequestQueue(Request  request){
        getRequestQueue().add(request);
    }

    public void cancelPendingRequest(String tag){
        if(requestQueue != null)
            requestQueue.cancelAll(tag);
    }

    public ImageLoader getImageLoader(){
        return imageLoader;
    }
}
