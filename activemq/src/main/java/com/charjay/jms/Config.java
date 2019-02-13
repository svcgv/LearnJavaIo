package com.charjay.jms;

public class Config {
//    public final static String ACTIVE_MQ_URL="tcp://127.0.0.1:61616";
//    public final static String ACTIVE_MQ_URL="tcp://192.168.98.165:61616";
    //容错连接
    public final static String ACTIVE_MQ_URL="failover:(tcp://192.168.98.165:61616,tcp://192.168.98.166:61616)";
}
