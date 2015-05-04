package oa.piaojin.com.androidoa;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.piaojin.common.UploadfileResource;
import com.piaojin.dao.MySqliteHelper;
import com.piaojin.dao.UploadfileDAO;
import com.piaojin.helper.HttpHepler;
import com.piaojin.otto.BusProvider;

import org.apache.http.conn.util.InetAddressUtils;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;


public class TestActivity extends Activity {

    private Button piaojin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // /data/data/oa.piaojin.com.androidoa/files
        setContentView(R.layout.chat_listview_item);
        /*piaojin=(Button)super.findViewById(R.id.piaojin);
        piaojin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(getLocalIpAddress());
                new Thread(new TestThread()).start();
            }
        });*/
    }

    private void test() throws Exception {
        DatagramSocket client = new DatagramSocket();

        String sendStr = "Hello! I'm Client";
        byte[] sendBuf;
        sendBuf = sendStr.getBytes();
        InetAddress addr = InetAddress.getByName(UploadfileResource.IP);
        int port = 6060;
        DatagramPacket sendPacket = new DatagramPacket(sendBuf, sendBuf.length,
                addr, port);
        client.send(sendPacket);
        byte[] recvBuf = new byte[100];
        DatagramPacket recvPacket = new DatagramPacket(recvBuf, recvBuf.length);
        client.receive(recvPacket);
        String recvStr = new String(recvPacket.getData(), 0,
                recvPacket.getLength());
        System.out.println("收到:" + recvStr);
        client.close();
    }

    public static String getLocalIpAddress(){

        try{
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && InetAddressUtils.isIPv4Address(inetAddress.getHostAddress())) {

                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        }catch (SocketException e) {
            // TODO: handle exception
           System.out.println(e.getMessage());
        }
        return null;
    }


    private void UDPServer() throws Exception {
        DatagramSocket  server = new DatagramSocket(6060);
        byte[] recvBuf = new byte[100];
        DatagramPacket recvPacket
                = new DatagramPacket(recvBuf , recvBuf.length);
        server.receive(recvPacket);
        String recvStr = new String(recvPacket.getData() , 0 , recvPacket.getLength());
        System.out.println("Hello World!" + recvStr);
        int port = recvPacket.getPort();
        InetAddress addr = recvPacket.getAddress();
        String sendStr = "Hello ! I'm Server";
        byte[] sendBuf;
        sendBuf = sendStr.getBytes();
        DatagramPacket sendPacket
                = new DatagramPacket(sendBuf , sendBuf.length , addr , port );
        server.send(sendPacket);
        server.close();
    }

    private class TestThread implements Runnable{

        @Override
        public void run() {
            System.out.println("TestThread");
            try {
                test();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
