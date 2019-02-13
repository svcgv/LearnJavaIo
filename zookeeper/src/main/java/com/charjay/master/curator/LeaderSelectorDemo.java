package com.charjay.master.curator;


import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.retry.ExponentialBackoffRetry;

import com.charjay.Config;

public class LeaderSelectorDemo {

	public static void main(String[] args) throws Exception {
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		final CuratorFramework client = CuratorFrameworkFactory.builder().connectString(Config.CONNECTSTRING)
				.sessionTimeoutMs(5000).connectionTimeoutMs(10000).retryPolicy(retryPolicy).namespace("text").build();
		client.start();
		
		final LeaderSelector leaderSelector = new LeaderSelector(client, "/led", new LeaderSelectorListenerAdapter(){

			@Override
			public void takeLeadership(CuratorFramework client) throws Exception {
				System.err.println("work ing...");
				Thread.currentThread().sleep(3000);
				System.err.println("end");
			}
			
		});
		leaderSelector.autoRequeue();
		leaderSelector.start();
		System.in.read();
	}
}

