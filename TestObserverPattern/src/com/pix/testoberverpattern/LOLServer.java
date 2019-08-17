package com.pix.testoberverpattern;

import java.util.Observable;

public class LOLServer extends Observable {
     public void postNewPublication(String msg) {
         // 标识内容
         setChanged();
         // 通知所有观察者状态发生改变
         notifyObservers(msg);
     }
}
