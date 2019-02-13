package com.charjay.idmaker.zkclient;


import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.charjay.Config;
 
public class TestIdMaker {
 
	static IdMaker idMaker = IdMaker.getInstance();
	public static int count=100;
	
	public static void main(String[] args) throws Exception {
		//第一次连接耗时，后面不会
//		test1();//consume1:9471ms consume2:412ms
		test2();//consume1:9359ms consume2:572ms
//		test3();//多线程抢资源测试100个线程抢1000个id，正常
		
	}

	private static void test3() throws Exception {
		long begintime=System.currentTimeMillis();
		
		idMaker.start(Config.CONNECTSTRING,"/NameService/IdGen", "ID");
		System.out.println("consume1:"+(System.currentTimeMillis()-begintime)+"ms");
		
		ExecutorService executorService = Executors.newFixedThreadPool(100);//开一个线程测试总时间
		long starttime = System.currentTimeMillis();
		try {
			for (int i = 0; i < 1000; i++) {
				executorService.execute(new Runnable() {
					@Override
					public void run() {
						try {
							String id = idMaker.generateId();
							System.out.println("线程="+Thread.currentThread().getId()+";id="+id);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		}catch(Exception e){
			e.printStackTrace();
		} finally {
//			idMaker.stop();
		}
		System.out.println();
		System.out.println("consume2:"+(System.currentTimeMillis()-starttime)+"ms");
		
	}

	private static void test2() throws Exception {
		long begintime=System.currentTimeMillis();
		
		idMaker.start(Config.CONNECTSTRING,"/NameService/IdGen", "ID");
		System.out.println("consume1:"+(System.currentTimeMillis()-begintime)+"ms");
		
		final CountDownLatch cd = new CountDownLatch(count);
		ExecutorService executorService = Executors.newFixedThreadPool(1);//开一个线程测试总时间
		long starttime = System.currentTimeMillis();
		try {
			for (int i = 0; i < count; i++) {
				executorService.execute(new Runnable() {
					@Override
					public void run() {
						try {
							String id = idMaker.generateId();
							System.out.println("线程="+Thread.currentThread().getId()+";id="+id);
							cd.countDown();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
			cd.await();
		}catch(Exception e){
			e.printStackTrace();
		} finally {
			idMaker.stop();
		}
		System.out.println();
		System.out.println("consume2:"+(System.currentTimeMillis()-starttime)+"ms");
	}

	private static void test1() throws Exception {
		long begintime=System.currentTimeMillis();
		idMaker.start(Config.CONNECTSTRING,"/NameService/IdGen", "ID");
		System.out.println("consume1:"+(System.currentTimeMillis()-begintime)+"ms");//consume1:9471ms
		
		long starttime = System.currentTimeMillis();
		try {
			for (int i = 0; i < count; i++) {
				String id = idMaker.generateId();
				System.out.println(id);
			}
		} finally {
			idMaker.stop();
		}
		System.out.println("consume2:"+(System.currentTimeMillis()-starttime)+"ms");//consume2:1207ms
		
	}
	
	
 
}
