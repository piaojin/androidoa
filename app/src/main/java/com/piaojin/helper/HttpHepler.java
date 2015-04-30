package com.piaojin.helper;

import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.piaojin.common.CommonResource;
import com.piaojin.domain.Task;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by piaojin on 2015/3/30.
 */
public class HttpHepler {
    private PrintStream output;
    public final String SUC = "success";
    public final String ERROR = "error";
    public final String NULLERROR = "nullerror";
    //服务器ip地址
    public static final String SENDTASK = "http://219.228.251.102:8080/cecWeb/taskAddTask";
    public static final String GETTASK = "http://219.228.251.102:8080/cecWeb/taskgetTask";
    public static final String GETMYTASK = "http://219.228.251.102:8080/cecWeb/taskgetMyTask";
    public static final String GETALLSHAREDFILE = "http://219.228.251.102:8080/cecWeb/filegetAllSharedFile";
    public static final String GETALLDEPARTMENT = "http://219.228.251.102:8080/cecWeb/departmentgetAllDepartment";
    public static final String LOGIN = "http://219.228.251.102:8080/cecWeb/employLogin";
    public static final String GETALLEMPLOY = "http://219.228.251.102:8080/cecWeb/employgetAllEmploy";
    public static final String DOWNFILE = "http://219.228.251.102:8080/cecWeb/downDownFile";

    //发布任务
    public String sendTask(String taskjson, String httpurl) {
        return HttpPost(taskjson, httpurl);
    }

    //获取所有我的任务
    public List<Task> getMyTask(int eid) {
        List<Task> list = null;
        String jsonresult = HttpPost(String.valueOf(eid), GETMYTASK);
        Type typelist = new TypeToken<ArrayList<Task>>() { //TypeToken GSON提供的数据类型转换器
        }.getType();
        list = CommonResource.gson.fromJson(jsonresult, typelist);
        return list;
    }

    //获取所有我发布的任务
    public List<Task> getTask(int uid) {
        List<Task> list = null;
        String jsonresult = HttpPost(String.valueOf(uid), GETTASK);
        Type typelist = new TypeToken<ArrayList<Task>>() { //TypeToken GSON提供的数据类型转换器
        }.getType();
        list = CommonResource.gson.fromJson(jsonresult, typelist);
        return list;
    }

    //带大量数据的额请求,原生post方式
    private String HttpPost(String parameter, String httpurl) {
        StringBuffer result = new StringBuffer("");
        try {
            HttpPost clientpost = new HttpPost(httpurl);
            //请求超时
            clientpost.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 60000);
            //读取超时
            clientpost.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("parameter", parameter));
            clientpost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            HttpResponse response = new DefaultHttpClient().execute(clientpost);
            if (response.getStatusLine().getStatusCode() == 200) {    // 现在已经发现了数据了
                InputStream input = response.getEntity().getContent();
                BufferedReader br = new BufferedReader(new InputStreamReader(input));
                String readLine = null;
                while ((readLine = br.readLine()) != null) {
                    result = result.append(readLine);
                }
                input.close();
                br.close();
            }
        } catch (Exception e) {
            System.out.println("@@@@@@@@@@@@@@@@" + e);
        }
        System.out.println("@@@@@@@@@@@@@@@@" + result.toString());
        return result.toString();
    }

    /*private String HttpPost(String taskjson, String httpurl) {
        StringBuffer jsonresult = new StringBuffer("");
        try {
            URL url = new URL(httpurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(10000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", httpurl.getBytes().length+"");
            conn.setDoOutput(true);
            OutputStream os = conn.getOutputStream();
            os.write(httpurl.getBytes());//请求写给服务器
            int code = conn.getResponseCode();
            System.out.println("code:"+code);
            if (code == 200) {
                byte data[] = new byte[512];
                int len = -1;
                while ((len = conn.getInputStream().read(data)) != -1) {
                    jsonresult.append(new String(data, 0, len));
                }
                System.out.println(jsonresult);
                conn.getInputStream().close();
            } else {
                System.out.println("请求出错了亲!");
                return null;
            }
        } catch (Exception e) {
            System.out.println("@@@@@@@@@@" + e);
        }
        if (TextUtils.isEmpty(jsonresult)) {
            return "";
        }
        return jsonresult.toString();
    }
*/
    //下载文件
    public static InputStream DownFile(int pid) {
        InputStream inputStream = null;
        try {
            HttpPost clientpost = new HttpPost(DOWNFILE);
            //请求超时
            clientpost.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 60000);
            //读取超时
            clientpost.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("pid", String.valueOf(pid)));
            clientpost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            HttpResponse response = new DefaultHttpClient().execute(clientpost);
            if (response.getStatusLine().getStatusCode() == 200) {    // 现在已经发现了数据了
                inputStream = response.getEntity().getContent();
            }
        } catch (Exception e) {
            System.out.println("@@@@@@@@@@@@@@@@" + e);
        }
        return inputStream;
    }

    //获取所有部门集合
    public StringBuffer getAllDepartment() {
        StringBuffer listjson = new StringBuffer("");
        listjson.append(Post(GETALLDEPARTMENT));
        return listjson;
    }

    //获取所有共享文件集合
    public StringBuffer getAllSharedFile() {
        StringBuffer listjson = new StringBuffer("");
        listjson.append(Post(GETALLSHAREDFILE));
        return listjson;
    }

    //登录验证
    public String Login(String[] name, String[] value) throws Exception {
        return clientpost(name, value, LOGIN);
    }

    //获取所有员工集合
    public StringBuffer getAllEmploy() {
        StringBuffer listjson = new StringBuffer("");
        listjson.append(Post(GETALLEMPLOY));
        return listjson;
    }

    //获取所有员工集合
    public static StringBuffer Post(String url) {
        StringBuffer listjson = new StringBuffer("");
        try {
            HttpPost clientpost = new HttpPost(url);
            //请求超时
            clientpost.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 60000);
            //读取超时
            clientpost.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);
            HttpResponse response = new DefaultHttpClient().execute(clientpost);
            if (response.getStatusLine().getStatusCode() == 200) {    // 现在已经发现了数据了
                InputStream input = response.getEntity().getContent();
                BufferedReader br = new BufferedReader(new InputStreamReader(input));
                String readLine = null;
                while ((readLine = br.readLine()) != null) {
                    listjson = listjson.append(readLine);
                }
                input.close();
                br.close();
            }
        } catch (Exception e) {
            System.out.println("@@@@@@@@@@@@@@@@" + e);
        }
        System.out.println("********" + listjson);
        return listjson;
    }

    //client post方式
    public static String clientpost(String[] name, String[] value, String url) {
        String result = "";
        if (name == null || value == null) {
            return "";
        }
        if (name.length > 0 && value.length > 0 && !url.equals("")) {
            try {
                HttpPost clientpost = new HttpPost(url);
                //请求超时
                clientpost.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 60000);
                //读取超时
                clientpost.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                for (int i = 0; i < name.length; i++) {
                    params.add(new BasicNameValuePair(name[i], value[i]));
                }
                clientpost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                HttpResponse response = new DefaultHttpClient().execute(clientpost);
                if (response.getStatusLine().getStatusCode() == 200) {    // 现在已经发现了数据了
                    InputStream input = response.getEntity().getContent();
                    byte data[] = new byte[8829];
                    int len = input.read(data); // 输入流读取
                    if (len > 0) {    // 已经读取到内容
                        result = new String(data, 0, len).trim();
                    }
                    input.close();
                }
            } catch (Exception e) {
                System.out.println("@@@@@@@@@@@@@@@@" + e);
            }
        }
        System.out.println("@@@@@@@@@@@@@@@@" + result);
        return result;
    }
}
