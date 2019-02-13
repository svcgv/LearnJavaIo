package com.charjay.webservice;

import javax.jws.WebService;

/**
 */

@WebService
public class SayHelloImpl implements ISayHello{

    public String sayHello(String name) {
        System.out.println("call sayHello()");
        return "Hello ,"+name+",I'am 菲菲";
    }

    public SayHelloVo sayHello2(SayHelloParam param) {
        System.out.println("call sayHello()");
        SayHelloVo vo =new SayHelloVo();
        vo.setAge(param.getAge()+1);
        vo.setName(param.getName()+",你好");
        return vo;
    }
}
