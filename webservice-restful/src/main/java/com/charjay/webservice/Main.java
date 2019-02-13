package com.charjay.webservice;


public class Main {
    public static void main(String[] args) {
        SayHelloImplService service =new SayHelloImplService();
        SayHelloImpl sayHello = service.getSayHelloImplPort();
        System.out.println(sayHello.sayHello("小林"));

        SayHelloParam param = new SayHelloParam();
        param.setAge(1);
        param.setName("小林");
        SayHelloVo vo = sayHello.sayHello2(param);
        System.out.println(vo.getAge()+"="+vo.getName());
    }
}
