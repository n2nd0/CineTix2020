package com.uas.cinetix;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.collection.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookieStore;

public class VolleySingleton {
    private static VolleySingleton mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private VolleySingleton(Context context) {
        CookieManager manager = new CookieManager();
        CookieHandler.setDefault(manager);

        mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());

        mImageLoader = new ImageLoader(this.mRequestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> mCache = new LruCache<>(10);

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                mCache.put(url, bitmap);
            }

            @Override
            public Bitmap getBitmap(String url) {
                return mCache.get(url);
            }
        });
    }

    public static synchronized VolleySingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleySingleton(context);
        }

        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }
}