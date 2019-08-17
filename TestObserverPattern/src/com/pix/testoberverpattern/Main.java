package com.pix.testoberverpattern;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        // 1、创建被观察者角色，服务器
        LOLServer subject = new LOLServer();
        // 2、定义观察者角色，玩家
        Player p1 = new Player("小明");
        Player p2 = new Player("张三");
        Player p3 = new Player("李四");
        // 3、将观察者注册到可观察者的列表
        subject.addObserver(p1);
        subject.addObserver(p2);
        subject.addObserver(p3);
        // 4、发布消息
        subject.postNewPublication("刀斩肉身心斩灵魂");
    }
}
