package com.piaojin.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.http.conn.util.InetAddressUtils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by piaojin on 2015/4/12.
 */
public class CommonResource {
    public static boolean isSharedfileLoading = false;
    public static Gson gson = new Gson();
    public static final int ACTION_MESSAGE = 0;//删除聊天
    public static final int ACTION_FILE = 1;//删除文件
    public static final int ACTION_NOTIF = 2;//删除公告
    public static final int ACTION_SCHEDULE = 3;//删除日程
    public static final int ACTION_TASK = 4;//删除任务
    public static final int EMPLOY = 9;
    public static final int MESSAGE = 0;
    public static final int MYFILE = 6;
    public static final int SCHEDULE = 4;
    public static final int TASK = 2;
    public static final int SHAREDFILE = 7;
    public static int ThreadCount = 0;//加载数据的线程数
    public static final int CURRENTTHREADCOUNT = 4;//加载数据完成的线程数
    public static boolean isLoadSharedfileFinish = false;
    public static boolean isLoadTaskFinish = false;
    public static boolean isLoadEmployFinish = false;
    public static boolean LoginType = true;//登录类型:true表示点击登录按钮登录,false表示自动登录

    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && InetAddressUtils.isIPv4Address(inetAddress.getHostAddress())) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }
        return null;
    }
}
