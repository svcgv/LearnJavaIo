package com.charjay.base.zkclient;

import org.I0Itec.zkclient.ZkClient;

import com.charjay.Config;

/**
 */
public class SessionDemo {


    public static void main(String[] args) {
        ZkClient zkClient=new ZkClient(Config.CONNECTSTRING,4000);

        System.out.println(zkClient+" - > success");
    }
}
