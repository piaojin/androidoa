package com.piaojin.helper;

import com.piaojin.domain.Message;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by piaojin on 2015/5/3.
 */

//接收消息的后台线程
public class UDPServerThread implements Runnable{

    private Message message;

    @Override
    public void run() {

        while (!Thread.interrupted()){
            UDPServer();
        }
    }

    private void UDPServer(){
        try{
            DatagramSocket server = new DatagramSocket(6060);
            byte[] recvBuf = new byte[1024];
            DatagramPacket recvPacket
                    = new DatagramPacket(recvBuf , recvBuf.length);
            server.receive(recvPacket);
            String recvStr = new String(recvPacket.getData() , 0 , recvPacket.getLength());
            System.out.println("来着服务器端转发的消息:" + recvStr);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
