package com.apaza.moises.visitsucre.global;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public final class VolleySingleton {

    private static VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private static Context context;
    private ImageLoader imageLoader;

    private VolleySingleton(Context context){
        VolleySingleton.context = context;
        requestQueue = getRequestQueue();
        imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(20);
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

    public static synchronized VolleySingleton getInstance(Context context){
        if(volleySingleton == null)
            volleySingleton = new VolleySingleton(context);
        return volleySingleton;
    }

    public RequestQueue getRequestQueue(){
        if(requestQueue == null)
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
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
