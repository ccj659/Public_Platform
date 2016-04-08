package com.efly.platform.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/4/8.
 */
public class User implements Serializable{

    public String ID;
    public String UserGuid;
    public String UserName;
    public String PassWord;

    public String Name;
    public String DogID;


    @Override
    public String toString() {
        return "User{" +
                "ID='" + ID + '\'' +
                ", UserGuid='" + UserGuid + '\'' +
                ", UserName='" + UserName + '\'' +
                ", Name='" + Name + '\'' +
                ", DogID='" + DogID + '\'' +
                '}';
    }
}
/*
"ID":5,
        "UserGuid":"61C2BE86-3638-4252-8ED6-1E2BAFFBA8F6",
        "UserName":"72672440",
        "Name":"张三",
        "DogID":"8000952"*/
