package com.pix.tcpclient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;

/**
 * 回显TCP客户端
 */
public class TCPEchoClient {
    private static final String IP = "127.0.0.1";
    private static final int PORT = 3456;
    public static void main(String[] args) throws IOException {
        // 创建Socket
        Socket socket = new Socket(IP,PORT);
//        Socket socket = new Socket(InetAddress.getByName(IP),PORT);
        System.out.println("Connected to server...");
        // 获取输入输出流
        InputStream in = socket.getInputStream();
        OutputStream out = socket.getOutputStream();
        // 发送数据
        out.write("hello ...".getBytes());
        // 读回显的数据
        int totalRecv = 0;
        byte data[] = new byte[1024];
        in.read(data);
        System.out.println("Recv data:" + new String(data));
        in.close();
        out.close();
        socket.close();
    }
}
