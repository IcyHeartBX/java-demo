package com.pix.multiserver;

public class Main {
    public static void main(String [] args) {
        System.out.println("multi server");
        MultiTcpServer tcpServer = new MultiTcpServer();
        tcpServer.startServer();
    }
}
