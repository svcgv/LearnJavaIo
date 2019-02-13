package com.charjay.base.javaapi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

import com.charjay.Config;

/**
 */
public class AuthControlDemo implements Watcher{
    private static CountDownLatch countDownLatch=new CountDownLatch(1);

    private static ZooKeeper zookeeper;
    static String path="/autu3";
    
    // acl (create /delete /admin /read/write)
    //权限模式： ip/Digest（username:password）/world/super
    public static void main(String[] args) throws Exception {
//        create();
        read();
    }
    
    
    private static void read() throws Exception {
    	zookeeper=new ZooKeeper(Config.CONNECTSTRING, 5000, new AuthControlDemo());
        countDownLatch.await();
        Stat stat=new Stat();
//        zookeeper.addAuthInfo("digest", "root:root".getBytes());//要加权限验证才可以读取
		byte[] ret = zookeeper.getData(path, false, stat);
		System.out.println(new String(ret));
		
	}


	private static void create() throws Exception {
    	zookeeper=new ZooKeeper(Config.CONNECTSTRING, 5000, new AuthControlDemo());
        countDownLatch.await();
        
        ACL acl=new ACL(ZooDefs.Perms.CREATE|ZooDefs.Perms.READ, new Id("digest",
        		DigestAuthenticationProvider.generateDigest("root:root")));
        ACL acl2=new ACL(ZooDefs.Perms.CREATE, new Id("ip","192.168.98.165"));
        List<ACL> acls=new ArrayList<>();
        acls.add(acl);
        acls.add(acl2);
        
        String ret = zookeeper.create(path,"123".getBytes(),acls,CreateMode.PERSISTENT);
        System.out.println(ret);

	}


	public void process(WatchedEvent watchedEvent) {
        //如果当前的连接状态是连接成功的，那么通过计数器去控制
        if(watchedEvent.getState()==Event.KeeperState.SyncConnected){
            if(Event.EventType.None==watchedEvent.getType()&&null==watchedEvent.getPath()){
                countDownLatch.countDown();
                System.out.println(watchedEvent.getState()+"-->"+watchedEvent.getType());
            }
        }

    }
}
