package com.charjay.base.javaapi;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;

import com.charjay.Config;
import com.charjay.base.zkclient.ZkClientApiOperatorDemo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 */
public class ApiOperatorDemo implements Watcher{
    
    private static CountDownLatch countDownLatch=new CountDownLatch(1);
    private static ZooKeeper zookeeper;
    private static Stat stat=new Stat();
    
    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        zookeeper=new ZooKeeper(Config.CONNECTSTRING, 5000, new ApiOperatorDemo());
        countDownLatch.await();
//        ACL acl=new ACL(ZooDefs.Perms.ALL,new Id("ip","192.168.98.165"));
//        List<ACL> acls=new ArrayList<>();
//        acls.add(acl);
//        zookeeper.create("/authTest","111".getBytes(),acls,CreateMode.PERSISTENT);
//        zookeeper.getData("/authTest",true,new Stat());
//        System.out.println(zookeeper.getState());

        //创建节点
        System.out.println("=====开始操作临时节点node1，没有监听");
        String result=zookeeper.create("/node1","123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
//        zookeeper.getData("/node1",new ZkClientApiOperatorDemo(),stat); //增加一个
        System.out.println("创建成功："+result);

        //修改数据
        zookeeper.setData("/node1","mic123".getBytes(),-1);
        TimeUnit.SECONDS.sleep(1);
        //修改数据
        zookeeper.setData("/node1","mic234".getBytes(),-1);
        TimeUnit.SECONDS.sleep(1);
        System.out.println("=====结束创建与修改node1节点");


        //创建节点和子节点
        String path="/node3";
        String cpath="/node31";
        System.out.println("=====开始操作永久节点"+path+"，监听"+path+cpath+"节点");
        
        zookeeper.create(path,"123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
        TimeUnit.SECONDS.sleep(1);
        Stat stat=zookeeper.exists(path+cpath,true);
        if(stat==null){//表示节点不存在
            zookeeper.create(path+cpath,"123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
            TimeUnit.SECONDS.sleep(1);
        }
        //修改子路径
        zookeeper.setData(path+cpath,"mic123".getBytes(),-1);
        TimeUnit.SECONDS.sleep(1);
        
        //获取指定节点下的子节点
        List<String> childrens=zookeeper.getChildren(path,true);
        System.out.println("获取"+path+"节点下的子节点"+childrens);
        
        //删除节点
		zookeeper.delete(path+cpath,-1);
		zookeeper.delete(path,-1);
		Thread.sleep(2000);
        
        System.out.println("=====结束操作"+path+"节点");



    }

    public void process(WatchedEvent watchedEvent) {
        //如果当前的连接状态是连接成功的，那么通过计数器去控制
        if(watchedEvent.getState()==Event.KeeperState.SyncConnected){
            if(Event.EventType.None==watchedEvent.getType()&&null==watchedEvent.getPath()){//None表示第一次连接上
                countDownLatch.countDown();
                System.out.println("连接成功："+watchedEvent.getState()+"-->"+watchedEvent.getType());
            }else if(watchedEvent.getType()== Event.EventType.NodeDataChanged){
                try {
                    System.out.println("数据变更触发路径："+watchedEvent.getPath()+"->改变后的值："+
                            zookeeper.getData(watchedEvent.getPath(),true,stat));
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else if(watchedEvent.getType()== Event.EventType.NodeChildrenChanged){//子节点的数据变化会触发
                try {
                    System.out.println("子节点数据变更路径："+watchedEvent.getPath()+"->节点的值："+
                            zookeeper.getData(watchedEvent.getPath(),true,stat));
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else if(watchedEvent.getType()== Event.EventType.NodeCreated){//创建子节点的时候会触发
                try {
                    System.out.println("节点创建路径："+watchedEvent.getPath()+"->节点的值："+
                            zookeeper.getData(watchedEvent.getPath(),true,stat));
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else if(watchedEvent.getType()== Event.EventType.NodeDeleted){//子节点删除会触发
                System.out.println("节点删除路径："+watchedEvent.getPath());
            }
            System.out.println("type=="+watchedEvent.getType());
        }

    }
}
