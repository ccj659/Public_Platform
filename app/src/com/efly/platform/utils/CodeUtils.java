package com.efly.platform.utils;

/**
 * Created by Administrator on 2016/3/9.
 */
public class CodeUtils {

    public static String codeSwitching(String code) {
        String encode=null;
        switch (code){
            case "45":
                encode="建设单位";
                break;
            case "15":
                encode="施工单位";
                break;
            case "42":
                encode="监理单位";
                break;
            case "47":
                encode="勘察单位";
                break;
            case "48":
                encode="设计单位";
                break;
            case "105":
                encode="代理单位";
                break;
            default:
                encode="";
                break;
        }
        return encode;
    }

}
