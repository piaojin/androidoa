package com.piaojin.ui.block;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.piaojin.helper.EmailHelper;
import com.piaojin.helper.NetWorkHelper;
import com.piaojin.tools.ExitApplication;
import com.piaojin.ui.block.task.SelectEmployDialog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import oa.piaojin.com.androidoa.HomeActivity_;
import oa.piaojin.com.androidoa.R;

@EActivity(R.layout.activity_email)
public class EmailActivity extends FragmentActivity {

    @ViewById
    com.piaojin.myview.ClearEditText receiver;
    @ViewById
    Button sendEmail;
    @ViewById
    Button cancel;
    @ViewById
    com.piaojin.myview.ClearEditText title;
    @ViewById
    com.piaojin.myview.ClearEditText content;
    private String emailaddress = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //把当前Activity放入集合，方便最后完全退出程序
        ExitApplication.getInstance().addActivity(this);
    }

    @AfterViews
    void init() {

        Intent intent = getIntent();
        emailaddress = intent.getStringExtra("emailaddress");
        if (!TextUtils.isEmpty(emailaddress)) {

            receiver.setText(emailaddress);
        }
    }

    @Click
    void sendEmail() {

        if (NetWorkHelper.isNetWorkAvailable(this)) {

            if (TextUtils.isEmpty(receiver.getText().toString())) {

                MyToast("请选择收件人!");
                return;
            }

            if (SelectEmployDialog.employ != null) {

                emailaddress = SelectEmployDialog.employ.getEmail();
            }
            EmailHelper.startSendToEmailIntent(this, emailaddress, title.getText().toString(), content.getText().toString());
        }

    }

    @Click
    void cancel() {

        HomeActivity_.intent(this).start();
    }

    @Click
    void receiver() {

        SelectEmployDialog selectEmployDialog = new SelectEmployDialog(receiver);
        selectEmployDialog.show(getFragmentManager(), "SelectEmployDialog");
    }

    void MyToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
