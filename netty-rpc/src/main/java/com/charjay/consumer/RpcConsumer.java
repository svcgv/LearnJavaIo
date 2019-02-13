package com.charjay.consumer;

import com.charjay.api.IRpcCalc;
import com.charjay.api.IRpcHello;
import com.charjay.config.CustomConfig;
import com.charjay.rpc.proxy.RpcProxy;

public class RpcConsumer {
	
	public static void main(String[] args) {
		CustomConfig.load("config.properties");
		//本机一个人在玩
		//自娱自乐
		//肯定是用动态代理来实现的,传给它一个接口，返回一个实例，伪代理
		IRpcHello rpcHello = RpcProxy.create(IRpcHello.class);
		String r = rpcHello.hello("Tom老师");
		System.out.println(r);
		
		
		int a = 8,b = 2;
		IRpcCalc calc = RpcProxy.create(IRpcCalc.class);
		System.out.println(a + " + " + b +" = " + calc.add(a, b));
		System.out.println(a + " - " + b +" = " + calc.sub(a, b));
		System.out.println(a + " * " + b +" = " + calc.mult(a, b));
		System.out.println(a + " / " + b +" = " + calc.div(a, b));
		
		
	}
	
}
