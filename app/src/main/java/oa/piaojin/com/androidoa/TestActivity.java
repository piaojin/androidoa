package oa.piaojin.com.androidoa;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.piaojin.common.FileResource;
import com.piaojin.common.LookResource;
import com.piaojin.common.UploadfileResource;
import com.piaojin.dao.MySqliteHelper;
import com.piaojin.dao.UploadfileDAO;
import com.piaojin.helper.HttpHepler;
import com.piaojin.otto.BusProvider;
import com.piaojin.tools.DateUtil;
import com.piaojin.tools.EmojiUtil;
import com.piaojin.tools.MediaRecorderUtil;

import org.apache.http.conn.util.InetAddressUtils;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;


public class TestActivity extends Activity {

    private static final int POLL_INTERVAL = 300;
    private Handler mHandler;
    private String videopath;
    MediaRecorderUtil mediaRecorderUtil=null;
    File videofile=null;
    private Button piaojin;
    private ImageView volume;
    private Button longclick;
    private Button playvideo;
    private Button stopRecording;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // /data/data/oa.piaojin.com.androidoa/files
        setContentView(R.layout.activity_test);
        mHandler=new MyHandler();
        mediaRecorderUtil=new MediaRecorderUtil();
        String SDPath = FileResource.getExternalSdCardPath();
        File dir = new File(SDPath + File.separator + "MyVideo" + File.separator);
        if (!dir.exists())
            dir.mkdirs();
        videofile = new File(dir, DateUtil.CurrentTime()+".arm");
        if (!videofile.exists()) {
            try {
                videofile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        piaojin=(Button)findViewById(R.id.piaojin);
        volume=(ImageView)findViewById(R.id.volume);
        longclick=(Button)findViewById(R.id.longclick);
        playvideo=(Button)findViewById(R.id.playvideo);
        stopRecording=(Button)findViewById(R.id.stopRecording);
        piaojin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaRecorderUtil.startRecording(videofile.getAbsolutePath());
            }
        });
        stopRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaRecorderUtil.stopRecording();
            }
        });
        playvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videopath=videofile.getAbsolutePath();
                mediaRecorderUtil.startPlaying(videopath);
            }
        });

        longclick.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                MyToast("onLongClick");
                mediaRecorderUtil.startRecording(videofile.getAbsolutePath());
                new Thread(mPollTask).start();
                return true;
            }
        });

        longclick.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        MyToast("抬起。。。。。。。。。。。。。。。。");
                        mediaRecorderUtil.stopRecording();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    private void updateDisplay(double signalEMA) {

        switch ((int) signalEMA) {
            case 0:
            case 1:
                volume.setImageResource(R.drawable.amp1);
                break;
            case 2:
            case 3:
                volume.setImageResource(R.drawable.amp2);
                break;
            case 4:
            case 5:
                volume.setImageResource(R.drawable.amp3);
                break;
            case 6:
            case 7:
                volume.setImageResource(R.drawable.amp4);
                break;
            case 8:
            case 9:
                volume.setImageResource(R.drawable.amp5);
                break;
            case 10:
            case 11:
                volume.setImageResource(R.drawable.amp6);
                break;
            default:
                volume.setImageResource(R.drawable.amp7);
                break;
        }
    }

    private Runnable mPollTask = new Runnable(){
        public void run() {
            while(!Thread.interrupted()){
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message message=new Message();
                mHandler.sendMessage(message);
            }
        }
    };

    private class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            double amp = mediaRecorderUtil.getAmplitude();
            System.out.println(amp+"");
            if(amp>=0){
                updateDisplay(amp);
            }
        }
    }

    void MyToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
