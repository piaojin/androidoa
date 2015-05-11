package oa.piaojin.com.androidoa;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.piaojin.ui.block.workmates.chat.VideoDialog;


public class TestActivity extends Activity {

    private VideoDialog videoDialog;
    private Button piaojin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //videoDialog=new VideoDialog(this);
        // /data/data/oa.piaojin.com.androidoa/files
        setContentView(R.layout.activity_test);
        piaojin=(Button)findViewById(R.id.piaojin);
        piaojin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoDialog.show();
            }
        });
    }

    void MyToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
