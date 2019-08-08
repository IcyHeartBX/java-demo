package com.pix.tcpserver

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.ServerSocket
import java.io.PrintWriter

class TcpServer {
    val PORT = 10088
    fun startServer() {
        println("TcpServer::startServer(),PORT:$PORT")
        // 创建套接字
        var serverSocket = ServerSocket(PORT)
        // 接收连接
        var socket = serverSocket.accept()
        // 读取接收到的内容
        var input = socket.getInputStream()
        var isr = InputStreamReader(input)
        var br = BufferedReader(isr)
        var info:String?=br.readLine()
        while (info != null) {
           println("我是服务器，客户端说:$info")
            info = br.readLine()
        }
        socket.shutdownInput()
        // 回复客户端
        var os = socket.getOutputStream()
        val pw = PrintWriter(os)
        pw.write("欢迎您！")
        pw.flush()
        // 关闭流
        pw.close()
        os.close()
        br.close()
        isr.close()
        input.close()
        socket.close()
        serverSocket.close()
    }
}