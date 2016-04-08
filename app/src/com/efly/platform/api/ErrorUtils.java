package com.efly.platform.api;

import android.content.Context;
import android.widget.Toast;

import com.efly.platform.base.BaseApplication;


/**
 * Created by yunjian on 2015/12/28.
 */
public class ErrorUtils {
    private static Context context = BaseApplication.getInstance();
    public static void toastMessage(Context context,String code,String message) {
        switch (code){
            case "200" :
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();

                break;
            case "400":
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();

            break;
            case "500":
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();

            break;
            default :
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            break;

        }
    }
}
