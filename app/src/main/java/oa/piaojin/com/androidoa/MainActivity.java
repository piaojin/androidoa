package oa.piaojin.com.androidoa;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import com.piaojin.common.CommonResource;
import com.piaojin.common.UserInfo;
import com.piaojin.helper.HttpHepler;
import com.piaojin.helper.MySharedPreferences;
import com.piaojin.helper.NetWorkHelper;
import com.piaojin.module.AppModule;
import com.piaojin.service.BackgroudService_;
import com.piaojin.tools.ExitApplication;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import dagger.ObjectGraph;

@EActivity(R.layout.login)
public class MainActivity extends Activity {

    final String NAME = "name";
    final String PWD = "pwd";
    @ViewById
    Spinner ipaddress;
    @ViewById
    com.piaojin.myview.MyEditText name;
    @ViewById
    com.piaojin.myview.MyEditText pwd;
    @ViewById
    CheckBox savepwd;
    @ViewById
    CheckBox autologin;
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
    private List<String> iplist = new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    private Handler handler;
    private String uname;
    private String upwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化dagger
        objectGraph = ObjectGraph.create(new AppModule(this));
        objectGraph.inject(this);
        //把当前Activity放入集合，方便最后完全退出程序
        ExitApplication.getInstance().addActivity(this);
        StrictMode.ThreadPolicy policy = new StrictMode.
                ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @AfterViews
    void init() {
        iplist.add("飘金版OA");
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, iplist);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ipaddress.setAdapter(adapter);
        handler = new MyHandler();
        LoadConfig();
        //AutoLogin();
    }

    //加载用户配置
    private void LoadConfig() {
        boolean isAutoLogin = mySharedPreferences.getBoolean("isAutoLogin", false);
        if (isAutoLogin) {
            autologin.setChecked(true);
        } else {
            autologin.setChecked(false);
        }

        boolean isSave = mySharedPreferences.getBoolean("isSave", false);
        if (isSave) {
            savepwd.setChecked(true);
            String sharename = mySharedPreferences.getString(NAME, "");
            String sharepwd = mySharedPreferences.getString(PWD, "");
            name.setText(sharename);
            pwd.setText(sharepwd);
        } else {
            savepwd.setChecked(false);
        }

    }

    //是否自动登录
    private void AutoLogin() {
        Boolean isAutoLogin = mySharedPreferences.getBoolean("isAutoLogin", false);
        if (isAutoLogin) {
            String sharename = mySharedPreferences.getString(NAME, "");
            String sharepwd = mySharedPreferences.getString(PWD, "");
            if (sharename == null || "".equals(sharename) || sharepwd == null || "".equals(sharepwd)) {
                return;
            }
            Login(new String[]{NAME, PWD}, new String[]{sharename, sharepwd});
        }
    }

    @Click
    void savepwd() {
        uname = this.name.getText().toString().trim();
        upwd = this.pwd.getText().toString().trim();
        boolean isSave = savepwd.isChecked();
        if (isSave && uname != null && !"".equals(uname) && upwd != null && !"".equals(upwd)) {
            mySharedPreferences.putBoolean("isSave", true);
            mySharedPreferences.putString(NAME, uname);
            mySharedPreferences.putString(PWD, upwd);
        } else {
            mySharedPreferences.putBoolean("isSave", false);
            mySharedPreferences.putString(NAME, "");
            mySharedPreferences.putString(PWD, "");
        }
    }

    @Click
    void autologin() {
        mySharedPreferences.putBoolean("isAutoLogin", autologin.isChecked());
    }

    @Click
    void loginClicked() {
        uname = this.name.getText().toString().trim();
        upwd = this.pwd.getText().toString().trim();
        if (uname == null || "".equals(uname) || upwd == null || "".equals(upwd)) {
            MyToast("用户名和密码不能为空!");
            return;
        }
        if (netWorkHelper.isAvailableNetwork(this)) {
            Login(new String[]{NAME, PWD}, new String[]{uname, upwd});
            savepwd();
        } else {
            MyToast("没有网络!");
        }
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
                    CommonResource.isloginClicked=true;
                    //启动后台服务
                    Intent intent = new Intent(MainActivity.this, BackgroudService_.class);
                    MainActivity.this.startService(intent);
                    HomeActivity_.intent(MainActivity.this).start();
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
}
