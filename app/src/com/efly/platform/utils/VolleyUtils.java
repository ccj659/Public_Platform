package com.efly.platform.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.efly.platform.base.BaseApplication;


/**
 * Created by yunjian on 2015/12/4.
 */
public class VolleyUtils {

    public static final String TAG = VolleyUtils.class.getSimpleName();
    public static final Context context = BaseApplication.getInstance();
    public static VolleyUtils volleyUtils;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;

    //	The best way to maintain volley core objects and request queue is, making them global by creating a singleton class.
    public static VolleyUtils getInstance() {
        if (volleyUtils == null) {
            volleyUtils = new VolleyUtils();
        }
        return volleyUtils;
    }

    //Default constructor
    private VolleyUtils() {
        getRequestQueue();
        getImageLoader();
    }

    public RequestQueue getRequestQueue() {

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
        return requestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (imageLoader == null) {
            imageLoader = new ImageLoader(requestQueue, new LruBitmapCache());
        }
        return imageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        //Set default tag if empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (requestQueue != null) {
            requestQueue.cancelAll(tag);
        }
    }


    /**
     * 检查当前网络是否可用
     *
     * @param
     * @return
     */

    public static boolean isNetworkAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
