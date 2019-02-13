package com.charjay.base.javaapi;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import com.charjay.Config;

/**
 */
public class CreateSessionDemo {
	//CountDownLatch能够使一个线程在等待另外一些线程完成各自工作之后，再继续执行。使用一个计数器进行实现。
//	计数器初始值为线程的数量。当每一个线程完成自己任务后，计数器的值就会减一。当计数器的值为0时，
//	表示所有的线程都已经完成了任务，然后在CountDownLatch上等待的线程就可以恢复执行任务。
    private static CountDownLatch countDownLatch=new CountDownLatch(1);
    
    public static void main(String[] args) throws IOException, InterruptedException {
        ZooKeeper zooKeeper=new ZooKeeper(Config.CONNECTSTRING, 5000, new Watcher() {
            public void process(WatchedEvent watchedEvent) {
                //如果当前的连接状态是连接成功的，那么通过计数器去控制
            	System.out.println("state1="+watchedEvent.getState());
                if(watchedEvent.getState()==Event.KeeperState.SyncConnected){
                    countDownLatch.countDown();
                }
            }
        });
        countDownLatch.await();
        System.out.println("state2="+zooKeeper.getState());
    }
}
