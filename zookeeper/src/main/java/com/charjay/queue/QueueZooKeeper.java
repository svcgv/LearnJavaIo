package com.charjay.queue;

import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooDefs.Ids;

import com.charjay.Config;

import org.apache.zookeeper.ZooKeeper;

public class QueueZooKeeper {

	  protected static ZooKeeper zkClient = null;
	  
	  protected static Integer mutex;
	  int sessionTimeout = 10000;
	  
	  protected String root="/queue";
	
	  public QueueZooKeeper(String connectString) throws Exception {
			if (zkClient == null) {
				zkClient = new ZooKeeper(connectString, sessionTimeout, new Watcher(){
					@Override
					public void process(WatchedEvent event) {
						System.out.println("receive watch event :"+event);
						if (event.getType() == EventType.NodeChildrenChanged) {
				            try {
				                System.out.println("ReGet Child:"+zkClient.getChildren(event.getPath(),true));
				            } catch (Exception e) {}
				        }
					}
				}); 
				mutex = new Integer(-1);
			}
			zkClient.exists("/queue/start", true);
			if (zkClient.exists("/queue", false) == null) {
				System.out.println("create /queue task-queue-fifo");
				zkClient.create("/queue", "task-queue-fifo".getBytes(),
						Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			} else {
				System.out.println("/queue is exist!");
			}
			 
	}
	  
	 
	 public boolean addQueue(int i) throws KeeperException, InterruptedException{
		System.out.println("create /queue/x" + i + " x" + i);
		zkClient.create("/queue/x" + i, ("x" + i).getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
		isCompleted();
		return true;
	 }
	 
	public void isCompleted() throws KeeperException, InterruptedException {
		int size = 3; //可以节点初始化
        int length = zkClient.getChildren("/queue", true).size();
        System.out.println("Queue Complete:" + length + "/" + size);
        if (length >= size) {
            System.out.println("create /queue/start start");
         //   zkClient.create("/queue/start", "start".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        } 
	}
	
	 public static void main(String[] args) throws Exception {
		ZkClient zkClient = new ZkClient(Config.CONNECTSTRING, 5000);
		String path = "/queue";
		zkClient.deleteRecursive(path);
		zkClient.close();
		
		QueueZooKeeper keeper = new QueueZooKeeper(Config.CONNECTSTRING);
		for (int i = 0; i < 8; i++) {
			keeper.addQueue(i);
		}
	     
	     
	 }
	 
	  
	  
}

