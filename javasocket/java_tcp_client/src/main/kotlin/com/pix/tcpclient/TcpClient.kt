package com.pix.tcpclient

import java.net.Socket
import java.io.PrintWriter
import java.io.InputStreamReader
import java.io.BufferedReader

class TcpClient {
//    val IP = "localhost"
    val IP = "47.74.146.51"
    val PORT = 6666
    fun startClient() {
       println("TcpClient::startClient(),IP:$IP,PORT:$PORT")
       // 创建客户端套接字
        try {
            var socket = Socket(IP,PORT)
            //2、获取输出流，向服务器端发送信息
            val os = socket.getOutputStream()//字节输出流
            val pw = PrintWriter(os)//将输出流包装成打印流
            pw.write("用户名：admin；密码：123")

            pw.flush()
            socket.shutdownOutput()
            val input = socket.getInputStream()
            val br = BufferedReader(InputStreamReader(input))
            var info: String? = br.readLine()
            while (info!=null) {
                println("我是客户端，服务器说:$info")
                info = br.readLine()
            }
            //4、关闭资源
            br.close()
            input.close()
            pw.close()
            os.close()
            socket.close()

        } catch (e:Exception) {
            e.printStackTrace()
        }

    }
}