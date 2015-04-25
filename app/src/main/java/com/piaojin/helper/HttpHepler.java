package com.piaojin.helper;

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
import java.io.PrintStream;
import java.net.Socket;
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
    public static final String GETALLSHAREDFILE = "http://219.228.251.23:8080/cecWeb/employgetAllSharedFile";
    public static final String LOGIN = "http://219.228.251.23:8080/cecWeb/employLogin";
    public static final String GETALLEMPLOY = "http://219.228.251.23:8080/cecWeb/employgetAllEmploy";
    public static final String DOWNFILE="http://219.228.251.23:8080/cecWeb/downDownFile";

    //下载文件
    public static InputStream DownFile(int pid){
        InputStream inputStream=null;
        try {
            HttpPost clientpost = new HttpPost(DOWNFILE);
            //请求超时
            clientpost.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 60000);
            //读取超时
            clientpost.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("pid",String.valueOf(pid)));
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
    //获取所有共享文件集合
    public StringBuffer getAllSharedFile(){
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
