package com.piaojin.helper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

/**
 * Created by piaojin on 2015/3/30.
 */
public class MySharedPreferences {
    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public MySharedPreferences(Context context) {
        this.context = context.getApplicationContext();
        this.sharedPreferences = context.getSharedPreferences("piaojin", Context.MODE_PRIVATE);
        editor = this.sharedPreferences.edit();
    }

    public void putString(String key, String values) {
        editor.putString(key, values);
        editor.commit();
    }

    public String getString(String key, String defaultvalue) {
        return sharedPreferences.getString(key, defaultvalue);
    }

    public void putBoolean(String key, Boolean values) {
        editor.putBoolean(key, values);
        editor.commit();
    }

    public Boolean getBoolean(String key, Boolean defaultvalue) {
        return sharedPreferences.getBoolean(key, defaultvalue);
    }
    public void putInt(String key, int values) {
        editor.putInt(key, values);
        editor.commit();
    }

    public int getInt(String key, int defaultvalue) {
        return sharedPreferences.getInt(key, defaultvalue);
    }
}
