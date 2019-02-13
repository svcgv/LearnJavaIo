package com.charjay.webservice;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 */
@WebService //SE和SEI的实现类
public interface ISayHello {

    @WebMethod //SEI中的方法
    String sayHello(String name);

    @WebMethod //SEI中的方法
    SayHelloVo sayHello2(SayHelloParam param);
}
