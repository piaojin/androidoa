package oa.piaojin.com.androidoa;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.piaojin.common.CommonResource;
import com.piaojin.common.UserInfo;
import com.piaojin.event.LoadDataFinishEvent;
import com.piaojin.helper.HttpHepler;
import com.piaojin.helper.MySharedPreferences;
import com.piaojin.helper.NetWorkHelper;
import com.piaojin.otto.BusProvider;
import com.piaojin.service.BackgroudService_;
import com.squareup.otto.Subscribe;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import javax.inject.Inject;
import dagger.ObjectGraph;

@EActivity(R.layout.activity_auto_login)
public class AutoLoginActivity extends Activity {

    final String NAME = "name";
    final String PWD = "pwd";
    @Inject
    HttpHepler httpHepler;
    @Inject
    MySharedPreferences mySharedPreferences;
    @Inject
    NetWorkHelper netWorkHelper;
    @Inject
    UserInfo userInfo;
    Thread t;
    private ObjectGraph objectGraph;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void init() {
        if (NetWorkHelper.isNetWorkAvailable(this)) {
            httpHepler=new HttpHepler();
            mySharedPreferences=new MySharedPreferences(this);
            handler = new MyHandler();
            LoadConfig();
            userInfo=new UserInfo(this,mySharedPreferences);
        }else{
            MainActivity_.intent(this).start();
        }
    }

    //加载用户配置
    private void LoadConfig() {
        boolean isAutoLogin = mySharedPreferences.getBoolean("isAutoLogin", false);
        if (isAutoLogin) {
            //自动登录
            String sharename = mySharedPreferences.getString(NAME, "");
            String sharepwd = mySharedPreferences.getString(PWD, "");
            if (!TextUtils.isEmpty(sharename) && !TextUtils.isEmpty(sharepwd)) {
                Login(new String[]{NAME, PWD}, new String[]{sharename, sharepwd});
            } else {
                MainActivity_.intent(this).start();
            }
        } else {
            MainActivity_.intent(this).start();
        }
    }

    @Subscribe
    public void onLoadDataFinishEvent(LoadDataFinishEvent loadDataFinishEvent) {
        HomeActivity_.intent(this).start();
    }

    private void Login(String name[], String value[]) {
        //去服务器登录验证
        t = new Thread(new HttpThreadHelper(name, value));
        t.start();
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String str = "";
            if (msg.obj != null) {
                String result = msg.obj.toString();
                if (httpHepler.ERROR.equals(result)) {
                    str = "用户名或密码错误!";
                } else {
                    //包含用户信息的json字符串
                    str = result;
                    //保存用户信息
                    mySharedPreferences.putString("userinfo", str);
                    //初始化用户信息
                    userInfo.init();
                    //启动后台服务
                    CommonResource.isloginClicked=false;
                    Intent intent = new Intent(AutoLoginActivity.this, BackgroudService_.class);
                    AutoLoginActivity.this.startService(intent);
                    return;
                }
            } else {
                str = "登录失败!";
            }
            MyToast(str);
        }
    }

    private class HttpThreadHelper implements Runnable {

        private String name[];
        private String value[];

        HttpThreadHelper(String name[], String value[]) {
            this.name = name;
            this.value = value;
        }

        @Override
        public void run() {
            //去服务器登录验证
            try {
                Looper.prepare();
                String result = httpHepler.Login(name, value);
                Message message = new Message();
                message.obj = result;
                handler.sendMessage(message);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("$$$error" + e.getMessage());
            }
        }
    }

    void MyToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

}
