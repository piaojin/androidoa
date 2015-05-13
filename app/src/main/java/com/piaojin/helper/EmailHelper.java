package com.piaojin.helper;

import android.content.Context;
import android.content.Intent;

/**
 * Created by piaojin on 2015/5/12.
 */
public class EmailHelper {

    public static void startSendToEmailIntent(Context context,String emailaddress,String title,String content){
        Intent data=new Intent(Intent.ACTION_SEND);
        data.putExtra(Intent.EXTRA_EMAIL, new String[]{emailaddress});
        data.putExtra(Intent.EXTRA_SUBJECT, title);
        data.putExtra(Intent.EXTRA_TEXT, content);
        //data.putExtra(Intent.EXTRA_STREAM, Uri.parse(""));
        data.setType("text/plain");
        context.startActivity(data);
    }
}
