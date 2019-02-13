package com.charjay.catalina;

import com.charjay.catalina.netty.server.MyTomcat;

public class App {
    public static void main(String [] args){
        try {
            new MyTomcat().start(8080);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
