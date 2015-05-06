package com.piaojin.ui.block.workmates.chat;

import android.content.Context;

import com.piaojin.common.CommonResource;
import com.piaojin.common.FileResource;
import com.piaojin.domain.Message;
import com.piaojin.event.ReceiveMessageEvent;
import com.piaojin.helper.HttpHepler;
import com.piaojin.otto.BusProvider;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

/**
 * Created by piaojin on 2015/5/6.
 */
public class VideoThread implements Runnable{
    public VideoThread(Context context, Message message) {
        this.context = context;
        this.message = message;
        httpHelper=new HttpHepler();
    }

    private Context context;
    private HttpHepler httpHelper;
    private Message message;

    @Override
    public void run() {

        InputStream inputStream=httpHelper.DownFile(message);
        if(inputStream!=null){
            Downfile(message, inputStream);
            CommonResource.isLoadVideoFinish=true;
        }
    }

    private void Downfile(Message message,InputStream inputStream) {

        File dir = null;
        File file = null;
        String SDPath = FileResource.getExternalSdCardPath();
        dir = new File(SDPath + File.separator + "MyVideo" + File.separator);
        try {
            if (!dir.exists())
                dir.mkdirs();
            file = new File(dir, new File(message.getVideourl()).getName());
            if (!file.exists()) {
                file.createNewFile();
            }
            message.setVideourl(file.getAbsolutePath());
            RandomAccessFile fileOutStream = new RandomAccessFile(file, "rwd");
            byte[] buffer = new byte[1024];
            int len = -1;
            while (((len = inputStream.read(buffer)) != -1)) {// 从输入流中读取数据写入到文件中
                fileOutStream.write(buffer, 0, len);
                System.out.println("长度:"+ len );
            }
            fileOutStream.close();
            inputStream.close();
            System.out.println("语音文件全部读完...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
