package com.efly.platform.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/3/1.
 */
public class ToastUtils {
    public  static  ToastUtils toastUtils;

    public static void ToastShort(Context context,String str){
        Toast.makeText(context,str,Toast.LENGTH_SHORT);
    }

    public static void ToastLong(Context context,String str){
        Toast.makeText(context,str,Toast.LENGTH_LONG);
    }


}
