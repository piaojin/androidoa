package com.piaojin.helper;

import com.google.gson.reflect.TypeToken;
import com.piaojin.common.CommonResource;
import com.piaojin.domain.Department;
import com.piaojin.domain.Employ;
import com.piaojin.domain.Message;
import com.piaojin.domain.MyFile;
import com.piaojin.domain.Task;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import java.io.BufferedReader;
import java.io.File;
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
    //语音准备
    public static final String RELAY = "http://219.228.251.73:8080/cecWeb/messagerelay";
    //发送语音
    public static final String SENDVIDEO = "http://219.228.251.73:8080/cecWeb/messagerelayVideo";
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
    //下载文件
    public static final String DOWNFILE = "http://219.228.251.73:8080/cecWeb/messagerelayFile";

    //发送聊天消息
    public void sendMessage(Message message) {
        if (message != null) {
            String messagejson = CommonResource.gson.toJson(message);
            HttpPost(messagejson, SENDMESSAGE);
        }
    }

    //接收任务
    public String acceptTask(int kid) {
        return HttpPost(String.valueOf(kid), ACCEPTTASK);
    }

    //更新任务
    public String sendEditTask(Task task) {
        return HttpPost(CommonResource.gson.toJson(task), SENDEDITTASK);
    }

    //删除任务
    public String deleteTask(int kid) {
        return HttpPost(String.valueOf(kid), DELETETASK);
    }

    //完成任务
    public String finishTask(int kid) {
        return HttpPost(String.valueOf(kid), FINISHTASK);
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

    /**
     * 上传的url,f 为要上传的文件
     * @url 上传的目标url
     * @f 要上传的文件
     * @return 成功返回true,失败返回false
     */
    public boolean postFile(String url, Message message) {

            File f=new File(message.getVideourl());
            System.out.println("***************"+url+"?videoname="+f.getName());
            if(url == null || !f.isFile()){
                return false;
            }
            HttpClient client = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url+"?videoname="+f.getName());
            HttpResponse response = null;
            try {
                FileEntity entity = new FileEntity(f, "binary/octet-stream");
                httpPost.setEntity(entity);
                response = client.execute(httpPost);
            } catch (Exception e) {
            } finally {
            }
            // 判断上传的状态和打印调试信息
            if (response != null
                    && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                // 打印调试信息,上传的url和上传的文件大小
                System.out.println("$$$$$$$$发送语音成功!");
                return true;
            }
        return false;
    }

   /* *//* 上传文件至Server，uploadUrl：接收文件的处理页面 *//*
    public void uploadFile(Message message, String uploadUrl) throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter(
                CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        HttpPost httppost = new HttpPost(uploadUrl);
        MultipartEntity entity = new MultipartEntity();
        File file = new File(message.getVideourl());
        FileBody fileBody = new FileBody(file);
        entity.addPart("uploadedfile", fileBody);
        httppost.setEntity(entity);
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity resEntity = response.getEntity();
        if (resEntity != null) {
            System.out.println("$$$$$$$$$$$$$$$$$$piaojin!");
        }
        httpclient.getConnectionManager().shutdown();
    }*/

    /* 上传文件至Server的方法 *//*
    public void uploadFile(Message message, String serverurl) {
        String end = "\r\n";
        String twoHyphens = "--";
        String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
        try {
            URL url = new URL(serverurl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setChunkedStreamingMode(1024 * 50);
            *//* 允许Input、Output，不使用Cache *//*
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setUseCaches(false);
            *//* 设置传送的method=POST *//*
            con.setRequestMethod("POST");
            *//* setRequestProperty *//*
            *//* 设置维持长连接 *//*
            con.setRequestProperty("Connection", "Keep-Alive");
            *//* 设置编码 *//*
            con.setRequestProperty("Charset", "UTF-8");
            *//* 设置文件类型 *//*
            con.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + BOUNDARY);
            con.setChunkedStreamingMode(10240);
            con.setConnectTimeout(10 * 100 * 1000);
            //在3.0以上需要在独立线程下才能正确连接
            con.connect();
            if (message != null) {

                File videofile=new File(message.getVideourl());
                *//* 设置DataOutputStream *//*
                DataOutputStream ds = new DataOutputStream(con.getOutputStream());
                ds.writeBytes(twoHyphens + BOUNDARY + end);
                ds.writeBytes("Content-Disposition: form-data; "+ "name=\"fuBrowser\";filename=\"" + videofile.getName() + "\"" + end);
                ds.writeBytes(end);
                *//* 取得文件的FileInputStream *//*
                FileInputStream fStream = new FileInputStream(message.getVideourl());
                *//* 设置每次写入1024bytes *//*
                byte[] buffer = new byte[1024];
                int length = -1;
                int cout=0;
                *//* 从文件读取数据至缓冲区 *//*
                while ((length = fStream.read(buffer)) != -1) {
                    *//* 将资料写入DataOutputStream中 *//*
                    ds.write(buffer, 0, length);
                    cout=cout+buffer.length;
                }
                ds.writeBytes(end);
                ds.writeBytes(twoHyphens + BOUNDARY + twoHyphens + end);
                *//* close streams *//*
                fStream.close();
                ds.flush();
                if (con.getResponseCode() == 200) {
                    *//* 取得Response内容 *//*
                    InputStream is = con.getInputStream();
                    int ch;
                    StringBuffer b = new StringBuffer();
                    while ((ch = is.read()) != -1) {
                        b.append((char) ch);
                    }
                    *//* 关闭DataOutputStream *//*
                    ds.close();
                    con.disconnect();
                } else {
                    throw new RuntimeException();
                }
            } else {
                throw new FileNotFoundException();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    /*public String PostVideo(String taskjson, String httpurl) {
        String nextLine = "\r\n";
        String dividerStart = "--";
        String boundary = "******";
        StringBuffer jsonresult = new StringBuffer("");
        try {
            URL url = new URL(httpurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(10000);
            conn.setChunkedStreamingMode(1024 * 256);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);//打开输入流，以便从服务器获取数据
            conn.setDoOutput(true);//打开输出流，以便向服务器提交数据
            conn.setUseCaches(false);//使用post不能用缓存
            // 设置Http请求头
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charset", "UTF-8");
            //必须在Content-Type 请求头中指定分界符
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            //conn.setRequestProperty("Content-Length", String.valueOf(taskjson.getBytes().length));
            //定义数据写入流，准备上传文件
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            dos.writeBytes(dividerStart + boundary + nextLine);
            //设置与上传文件相关的信息
            dos.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\""
                    + "piaojinfile" + "\"" + nextLine);
            dos.writeBytes(nextLine);

            //conn.connect();
            *//*OutputStream os = conn.getOutputStream();
            os.write(taskjson.getBytes());//请求写给服务器*//*
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
    }*/
    //下载文件
    public InputStream DownFile(Message message) {
        InputStream inputStream = null;
        try {
            HttpPost clientpost = new HttpPost(DOWNFILE);
            //请求超时
            clientpost.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 60000);
            //读取超时
            clientpost.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("parameter", CommonResource.gson.toJson(message)));
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
