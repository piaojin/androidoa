package com.piaojin.ui.block.workmates.chat;

import android.content.Context;

import com.piaojin.common.MessageResource;
import com.piaojin.dao.MessageDAO;
import com.piaojin.dao.MySqliteHelper;
import com.piaojin.domain.Message;
import com.piaojin.helper.HttpHepler;

/**
 * Created by piaojin on 2015/5/3.,聊天线程
 */
public class ChatThread implements Runnable{
    public ChatThread(Context context, HttpHepler httpHelper, Message message) {
        this.context = context;
        this.httpHelper = httpHelper;
        this.message = message;
    }

    private Context context;
    private HttpHepler httpHelper;
    private Message message;
    private MessageDAO messageDAO;
    private MySqliteHelper mySqliteHelper;

    @Override
    public void run() {
        switch(message.getType()){
            case MessageResource.TEXT:
                sendText();
                break;
            case MessageResource.VIDEO:
                break;
            case MessageResource.PICTURE:
                break;
        }
    }

    //文字聊天包含表情
    private void sendText(){
        httpHelper.sendMessage(message);
    }

    //图片
    private void sendPicture(){

    }

    //语音聊天
    private void sendVideo(){

    }
}
