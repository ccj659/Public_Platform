package com.efly.platform.base;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.efly.platform.bean.User;
import com.efly.platform.jpush.ExampleUtil;
import com.efly.platform.utils.BitmapUtil;
import com.efly.platform.utils.SharedPreferenceUtil;
import com.efly.platform.utils.VolleyUtils;
import com.lidroid.xutils.DbUtils;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class BaseApplication extends Application {
    public static final String TAG = BaseApplication.class.getSimpleName();
    public static Display display;
    public static Context context;
    private static BaseApplication baseApplication;
    public DbUtils.DaoConfig daoConfig;

    //    public User currentUser;
    public static synchronized BaseApplication getInstance() {
        return baseApplication;
    }

    public DbUtils.DaoConfig getDaoConfig() {
        return daoConfig;
    }
    @Override
    public void onCreate() {    	     
    	 Log.d(TAG, "[Application] onCreate");
         super.onCreate();
        context = getBaseContext();
        baseApplication = this;
         JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
         JPushInterface.init(this);     		// 初始化 JPush


        SharedPreferenceUtil.initSharedPreference(BaseApplication.getInstance());
        BitmapUtil.initBitmapUtil(BaseApplication.getInstance());
        VolleyUtils.getInstance();
        //判断user的alias 是否存在
        User user = SharedPreferenceUtil.getInstance().getUser();
        //测试~~~~~~~~~~~~~~~~~~~~~~~!!!!!!!!!


        if (user != null && TextUtils.isEmpty(user.DogID)) {
            user.DogID="0x9469D00AC6930D3F32AC77490BC613B6";
            JPushInterface.setAliasAndTags(getApplicationContext(), user.DogID, null, mAliasCallback);
        }else {
            JPushInterface.setAliasAndTags(getApplicationContext(), "0x9469D00AC6930D3F32AC77490BC613B6", null, mAliasCallback);

        }


        if (SharedPreferenceUtil.getInstance().getAutoLogin() == false) {
            SharedPreferenceUtil.getInstance().setIsLogin(false);
        } else if (SharedPreferenceUtil.getInstance().getAutoLogin() == true && SharedPreferenceUtil.getInstance().getUser() != null) {
            SharedPreferenceUtil.getInstance().setIsLogin(true);
        }

        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        display = wm.getDefaultDisplay();

    }


    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs ;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.i(TAG, logs);
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i(TAG, logs);
                    /*if (ExampleUtil.isConnected(getApplicationContext())) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    } else {
                        Log.i(TAG, "No network");
                    }*/
                    break;

                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e(TAG, logs);
            }

            ExampleUtil.showToast(logs, getApplicationContext());
        }

    };


}
