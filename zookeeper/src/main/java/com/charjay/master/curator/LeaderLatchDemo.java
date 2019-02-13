package com.charjay.master.curator;


import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.retry.ExponentialBackoffRetry;

import com.charjay.Config;

public class LeaderLatchDemo {

	public static void main(String[] args) throws Exception {
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		CuratorFramework client = CuratorFrameworkFactory.builder().connectString(Config.CONNECTSTRING)
				.sessionTimeoutMs(2000).connectionTimeoutMs(10000).retryPolicy(retryPolicy).namespace("text").build();
		client.start();
		// 选举Leader 启动
		LeaderLatch latch = new LeaderLatch(client,"/path");
		latch.start();
		latch.await();
		System.err.println("我启动了");
		Thread.currentThread().sleep(1000000);
		latch.close();
		client.close();
	}
}

