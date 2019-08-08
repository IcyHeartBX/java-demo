package com.pix.socketbase;

import java.net.*;
import java.util.Enumeration;

/**
 * 取得本机所有网卡信息
 * 取得网卡下面的IP地址信息
 * 根据域名取IP
 */
public class InetAddressExample {
    public static void main(String [] args) throws UnknownHostException {
        try {
            // 取得所有网络接口对应的实例
            Enumeration<NetworkInterface> interfaceList =  NetworkInterface.getNetworkInterfaces();
            if(null == interfaceList) {
                System.out.println("InetAddressExample.main(),no interface found!");
            }
            else {
                while (interfaceList.hasMoreElements()) {
                    // 取得网卡名称
                    NetworkInterface nInterface = interfaceList.nextElement();
                    System.out.println("Interface," + nInterface.getName() + ":");
                    // 取得网卡下的IP地址，有IPv4和IPv6两种
                    Enumeration<InetAddress> addList = nInterface.getInetAddresses();
                    if(!addList.hasMoreElements()) {
                        System.out.println("\t-- No More Interface --");
                    }
                    while (addList.hasMoreElements()) {
                        InetAddress addr = addList.nextElement();
                        System.out.print("\tAddress " + ((addr instanceof Inet4Address ? "(v4)" : addr instanceof Inet6Address ? "(v6)" : "(?)")));
                        System.out.println(": " + addr.getHostAddress());
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        String host = "www.baidu.com";
        // 根据地址取得ip
        System.out.println(host + ":");
        InetAddress [] address = InetAddress.getAllByName(host);
        for (InetAddress addr : address) {
            System.out.println("\t" + addr.getHostName() + "/" + addr.getHostAddress());
        }
    }
}
