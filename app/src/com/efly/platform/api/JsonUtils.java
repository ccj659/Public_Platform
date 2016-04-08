package com.efly.platform.api;

import android.text.TextUtils;

import com.efly.platform.bean.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yunjian on 2015/12/4.
 */
public class JsonUtils {

    private static final String TAG = "JsonUtils";

    public static int getStatus(String json) {
        if (TextUtils.isEmpty(json)) {
            return APIService.FAILURE;
        }
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("code").toString();
            if (status.equalsIgnoreCase("200")) {
                return APIService.SUCCESS;
            } else {
                return jsonObject.getInt("errorCode");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return APIService.FAILURE;
    }


    public static User parseUser(String json) {
        User user = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject goodsJson = jsonObject.getJSONObject("result");
            Gson gson = new Gson();
            user = gson.fromJson(goodsJson.toString(), new TypeToken<User>() {
            }.getType());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }



}
