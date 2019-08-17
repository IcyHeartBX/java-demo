package com.pix.testoberverpattern;

import java.util.Observable;
import java.util.Observer;

/**
 * 玩家类，观察者，观察服务器是否更新
 * 实现Oberver接口
 */
public class Player implements Observer {
    public String name;

    public Player(String name) {
        this.name = name;
    }


    // 重写update方法，被观察者更新，会被通知
    @Override
    public void update(Observable observable, Object o) {
        System.out.println("Hello," + name + ",LOL更新啦~，内容：" + o.toString());
    }

    @Override
    public String toString() {
        return "玩家：" + name;
    }
}
