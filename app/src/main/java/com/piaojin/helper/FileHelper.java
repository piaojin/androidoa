package com.piaojin.helper;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import java.io.File;

/**
 * Created by piaojin on 2015/5/11.
 */
public class FileHelper {

    public static boolean openFile(Context context, String filepath) {
       /* Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(OPEN_MODE, READ_ONLY);
        bundle.putBoolean(SEND_CLOSE_BROAD, true);
        bundle.putString(THIRD_PACKAGE,"oa.piaojin.com.androidoa");
        bundle.putBoolean(CLEAR_BUFFER, true);
        bundle.putBoolean(CLEAR_TRACE, true);
        //bundle.putBoolean(CLEAR_FILE, true);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setClassName(packageName, className);

        File file = new File(path);
        if (file == null || !file.exists()) {
            return false;
        }

        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        intent.putExtras(bundle);

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();

            return false;
        }*/

        File file=new File(filepath);
        if(file.isFile()){
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(android.content.Intent.ACTION_VIEW);
            intent.setClassName("cn.wps.moffice_eng", "cn.wps.moffice.documentmanager.PreStartActivity2");
            Uri uri = Uri.fromFile(file);
            intent.setData(uri);
            try
            {
                context.startActivity(intent);
            } catch (ActivityNotFoundException e)
            {
                e.printStackTrace();
            }
        }
        return true;
    }
}
