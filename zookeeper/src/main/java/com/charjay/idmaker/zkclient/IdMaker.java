package com.charjay.idmaker.zkclient;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;
import org.I0Itec.zkclient.serialize.BytesPushThroughSerializer;
 
public class IdMaker {
	
	private ZkClient client = null;
	private String server;//记录服务器的地址
	private String root;//记录父节点的路径
	private String nodeName;//节点的名称
	private volatile boolean running = false;
	private ExecutorService cleanExector = null;
	
	//单例
	private static class LazyHolder {
		private static final IdMaker INSTANCE = new IdMaker();
	}
	private IdMaker (){}
	public static final IdMaker getInstance() {  
		return LazyHolder.INSTANCE;
	}
	
	public void start(String zkServer,String root,String nodeName) throws Exception {
		if (running){
			System.out.println("server has started...");
			return;
		}
		this.root = root;
		this.server = zkServer;
		this.nodeName = nodeName;
		
		running = true;
		
		client = new ZkClient(server,5000,5000,new BytesPushThroughSerializer());
		cleanExector = Executors.newFixedThreadPool(10);
		try{
			client.createPersistent(root,true);
		}catch (ZkNodeExistsException e){
			//ignore;
		}
	}
	
	
	public void stop() throws Exception {
		if (!running){
			System.out.println("server has stopped...");
			return;
		}
			
		running = false;
		freeResource();
		
	}
	
	/**
	 * 释放服务资源
	 */
	private void freeResource(){
	
		cleanExector.shutdown();
		try{
			cleanExector.awaitTermination(2, TimeUnit.SECONDS);
		}catch(InterruptedException e){
			e.printStackTrace();
		}finally{
			cleanExector = null;
		}
	
		if (client!=null){
			client.close();
			client=null;
		}
	}
	
	/**
	 * 检测服务是否正在运行
	 * @throws Exception
	 */
	private void checkRunning() throws Exception {
		if (!running)
			throw new Exception("请先调用start");
	}
	
	private String ExtractId(String str){
		int index = str.lastIndexOf(nodeName);
		if (index >= 0){
			index+=nodeName.length();
			return index <= str.length()?str.substring(index):"";
		}
		return str;
		
	}
	
	/**
	 * 产生ID
	 * 核心函数
	 * @param removeMethod 删除的方法
	 * @return
	 * @throws Exception
	 */
	public String generateId() throws Exception{
		checkRunning();
		final String fullNodePath = root.concat("/").concat(nodeName);
		//返回创建的节点的名称
		final String ourPath = client.createPersistentSequential(fullNodePath, null);
		
		/**
		 * 在创建完节点后为了不占用存储，用完即删
		 */
		cleanExector.execute(new Runnable() {
			public void run() {
				client.delete(ourPath);
			}
		});
		//ID0000000000, ID0000000001，ExtractId提取ID
		return ExtractId(ourPath);
	}
 
}
