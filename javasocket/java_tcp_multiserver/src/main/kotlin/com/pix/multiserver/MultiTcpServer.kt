package com.pix.multiserver

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.Socket

class MultiTcpServer {
    val PORT = 10088
    var serverSocket: ServerSocket?=null
    var clientCount:Int = 0
    fun startServer() {
        println("MultiTcpServer::startServer(),PORT:$PORT")
       serverSocket = ServerSocket(PORT)
        var socket: Socket?=null
        while (true) {
            socket = serverSocket?.accept()
            var serverThread = ServerThread("multi_tcp_server", socket!!)
            serverThread?.start()
            clientCount++
            println("MultiTcpServer::startServer(),client count:$clientCount")
        }
    }

    class ServerThread(name: String?,var socket:Socket?) : Thread(name) {
        override fun run() {
            super.run()
            // 读取接收到的内容
            var input = socket?.getInputStream()
            var isr = InputStreamReader(input)
            var br = BufferedReader(isr)
            var info:String?=br.readLine()
            while (info != null) {
                println("我是服务器，客户端说:$info")
                info = br.readLine()
            }
            socket?.shutdownInput()
            // 回复客户端
            var os = socket?.getOutputStream()
            val pw = PrintWriter(os)
            pw.write("欢迎您！")
            pw.flush()
            // 关闭流
            pw.close()
            os?.close()
            br.close()
            isr.close()
            input?.close()
        }
    }
}