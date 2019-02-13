package com.charjay.base.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.transaction.CuratorTransactionResult;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 */
public class CuratorOperatorDemo {

	static String path="/curator";
	static String paths=path+"/curator1";
	//fluent风格
    public static void main(String[] args) throws Exception {
        CuratorFramework curatorFramework=CuratorClientUtils.getInstance();
        System.out.println("连接成功.........");
        /**
         * crud
         */
//        crud(curatorFramework);

        /**
         * 异步操作
         */
//        yibu(curatorFramework);

        /**
         * 事务操作（curator独有的）
         */
        shiwu(curatorFramework);
    }
    
    
    
	private static void shiwu(CuratorFramework curatorFramework) {
		try {
			Collection<CuratorTransactionResult> resultCollections=curatorFramework.inTransaction()
					.create().forPath("/trans","111".getBytes()).and()
					.setData().forPath(path,"111".getBytes()).and().commit();
			for (CuratorTransactionResult result2:resultCollections){
				//两个语句合成一个事务
				System.out.println("事务"+result2.getForPath()+"->"+result2.getType());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}



	private static void yibu(CuratorFramework curatorFramework) throws InterruptedException {
        ExecutorService service= Executors.newFixedThreadPool(1);
        CountDownLatch countDownLatch=new CountDownLatch(1);
        try {
            curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).
                    inBackground(new BackgroundCallback() {
                        @Override
                        public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
                            System.out.println("异步"+Thread.currentThread().getName()+"->resultCode:"+curatorEvent.getResultCode()+"->"
                            +curatorEvent.getType());
                            countDownLatch.countDown();
                        }
                    },service).forPath(path,"123".getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        countDownLatch.await();
        service.shutdown();
		
	}



	private static void crud(CuratorFramework curatorFramework) throws Exception {
        /**
         * 创建节点
         */
        String result=curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).
                forPath(paths,"123".getBytes());
        System.out.println("创建"+result);//递归创建


        /**
         * 查询
         */
        Stat stat=new Stat();
        byte[] bytes=curatorFramework.getData().storingStatIn(stat).forPath(path);
        System.out.println("查询"+new String(bytes)+"-->stat:"+stat);

        /**
         * 更新
         */
        Stat stat2=curatorFramework.setData().forPath(path,"123".getBytes());
        System.out.println("更新"+stat2);

        /**
         * 删除节点
         */
        //默认情况下，version为-1
//        curatorFramework.delete().deletingChildrenIfNeeded().forPath(path);//从根目录递归删除
//        System.out.println("删除");
	}
}
