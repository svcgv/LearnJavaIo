package com.charjay.webservice;

import javax.xml.ws.Endpoint;

/**
 */
public class Main {

    public static void main(String[] args) {
    	//http://localhost:8090/charjay/hello?wsdl
    	//对应的客户端测试在scoket里面
        Endpoint.publish("http://localhost:8090/charjay/hello",new SayHelloImpl());

        System.out.println("publish success");
    }
}
