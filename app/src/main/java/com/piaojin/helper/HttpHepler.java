package com.piaojin.helper;

import com.google.gson.reflect.TypeToken;
import com.piaojin.common.CommonResource;
import com.piaojin.domain.Department;
import com.piaojin.domain.Employ;
import com.piaojin.domain.Message;
import com.piaojin.domain.MyFile;
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
import java.io.PrintStream;
import java.lang.reflect.Type;
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
    //发布任务
    public static final String SENDTASK = "http://219.228.251.73:8080/cecWeb/taskAddTask";
    //聊天
    public static final String SENDMESSAGE = "http://219.228.251.73:8080/cecWeb/messagerelayMessage";
    //更新任务
    public static final String SENDEDITTASK = "http://219.228.251.73:8080/cecWeb/tasksendEditTask";
    //删除任务
    public static final String DELETETASK = "http://219.228.251.73:8080/cecWeb/taskdeleteTask";
    //完成任务
    public static final String FINISHTASK = "http://219.228.251.73:8080/cecWeb/taskfinishTask";
    //接收任务
    public static final String ACCEPTTASK = "http://219.228.251.73:8080/cecWeb/taskacceptTask";
    //获取我发布的任务
    public static final String GETTASK = "http://219.228.251.73:8080/cecWeb/taskgetTask";
    //获取我的任务
    public static final String GETMYTASK = "http://219.228.251.73:8080/cecWeb/taskgetMyTask";
    //获取共享文件
    public static final String GETALLSHAREDFILE = "http://219.228.251.73:8080/cecWeb/filegetAllSharedFile";
    //获取所有部门
    public static final String GETALLDEPARTMENT = "http://219.228.251.73:8080/cecWeb/departmentgetAllDepartment";
    //登录验证
    public static final String LOGIN = "http://219.228.251.73:8080/cecWeb/employLogin";
    //获取所有同事
    public static final String GETALLEMPLOY = "http://219.228.251.73:8080/cecWeb/employgetAllEmploy";
    //瞎子啊文件
    public static final String DOWNFILE = "http://219.228.251.73:8080/cecWeb/downDownFile";

    //发送聊天消息
    public void sendMessage(Message message){
        if(message!=null){
            String messagejson=CommonResource.gson.toJson(message);
            HttpPost(messagejson,SENDMESSAGE);
        }
    }

    //接收任务
    public String acceptTask(int kid) {
        return HttpPost(String.valueOf(kid),ACCEPTTASK);
    }

    //更新任务
    public String sendEditTask(Task task) {
        return HttpPost(CommonResource.gson.toJson(task),SENDEDITTASK);
    }

    //删除任务
    public String deleteTask(int kid) {
        return HttpPost(String.valueOf(kid),DELETETASK);
    }

    //完成任务
    public String finishTask(int kid) {
        return HttpPost(String.valueOf(kid),FINISHTASK);
    }

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

    //带大量数据的请求
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
    public List<Department> getAllDepartment() {
        StringBuffer listjson = new StringBuffer("");
        listjson.append(Post(GETALLDEPARTMENT));
        Type typelist = new TypeToken<ArrayList<Department>>() { //TypeToken GSON提供的数据类型转换器
        }.getType();
        List<Department> list = CommonResource.gson.fromJson(listjson.toString(), typelist);
        return list;
    }

    //获取所有共享文件集合
    public List<MyFile> getAllSharedFile() {
        StringBuffer listjson = new StringBuffer("");
        listjson.append(Post(GETALLSHAREDFILE));
        Type typelist = new TypeToken<ArrayList<MyFile>>() { //TypeToken GSON提供的数据类型转换器
        }.getType();
        List<MyFile> list = CommonResource.gson.fromJson(listjson.toString(), typelist);
        return list;
    }

    //登录验证
    public String Login(String[] name, String[] value) throws Exception {
        return clientpost(name, value, LOGIN);
    }

    //获取所有员工集合
    public List<Employ> getAllEmploy() {
        StringBuffer listjson = new StringBuffer("");
        listjson.append(Post(GETALLEMPLOY));
        Type typelist = new TypeToken<ArrayList<Employ>>() { //TypeToken GSON提供的数据类型转换器
        }.getType();
        List<Employ> list = CommonResource.gson.fromJson(listjson.toString(), typelist);
        return list;
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
                    byte data[] = new byte[1024];
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
