package oa.piaojin.com.androidoa;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.piaojin.dao.MySqliteHelper;
import com.piaojin.dao.UploadfileDAO;
import com.piaojin.helper.HttpHepler;
import com.piaojin.otto.BusProvider;


public class TestActivity extends Activity {

    private Button piaojin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        piaojin=(Button)super.findViewById(R.id.piaojin);
        piaojin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new MyThread()).start();
            }
        });
    }

    private class MyThread implements Runnable{

        @Override
        public void run() {
            HttpHepler.Post(HttpHepler.GETALLEMPLOY);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
