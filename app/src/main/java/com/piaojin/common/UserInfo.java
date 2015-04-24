package com.piaojin.common;

import android.content.Context;

import com.google.gson.Gson;
import com.piaojin.domain.Employ;
import com.piaojin.helper.MySharedPreferences;

import javax.inject.Inject;

/**
 * Created by piaojin on 2015/4/8.
 */
public class UserInfo {

    Context context;
    MySharedPreferences mySharedPreferences;
    public static Employ employ;

    public void init(){
        String employstr=mySharedPreferences.getString("userinfo","");
        System.out.println("$$$"+employstr);
        if(!employstr.equals("")){
            employ=(new Gson()).fromJson(employstr,Employ.class);
            System.out.println("$$$"+employ.getName());
        }
    }

    public UserInfo(Context context,MySharedPreferences mySharedPreferences) {
        this.context = context;
        this.mySharedPreferences= mySharedPreferences;
    }
}
