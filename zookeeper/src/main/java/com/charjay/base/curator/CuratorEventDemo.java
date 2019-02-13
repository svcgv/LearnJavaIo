package com.charjay.base.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.TimeUnit;

/**
 */
public class CuratorEventDemo {

    /**
     * 三种watcher来做节点的监听
     * pathcache   监视一个路径下子节点的创建、删除、节点数据更新
     * NodeCache   监视一个节点的创建、更新、删除
     * TreeCache   pathcaceh+nodecache 的合体（监视路径下的创建、更新、删除事件），
     * 缓存路径下的所有子节点的数据
     */
	static String path="/event";
	static String paths=path+"/event1";
	
    public static void main(String[] args) throws Exception {
        CuratorFramework curatorFramework=CuratorClientUtils.getInstance();

        /**
         * 监视一个节点NodeCache
         */
        nodeCache(curatorFramework);

        /**
         * 监视一个路径下子节点PatchChildrenCache
         */
//        pathChildrenCache(curatorFramework);
        

    }

	private static void pathChildrenCache(CuratorFramework curatorFramework) throws Exception {
		PathChildrenCache cache=new PathChildrenCache(curatorFramework,path,true);
        cache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        // Normal / BUILD_INITIAL_CACHE /POST_INITIALIZED_EVENT

        cache.getListenable().addListener((curatorFramework1,pathChildrenCacheEvent)->{
            switch (pathChildrenCacheEvent.getType()){
                case CHILD_ADDED:
                    System.out.println("增加子节点");
                    break;
                case CHILD_REMOVED:
                    System.out.println("删除子节点");
                    break;
                case CHILD_UPDATED:
                    System.out.println("更新子节点");
                    break;
                default:break;
            }
        });

        //这个节点是跟节点，不监听，只监听该根节点的子节点
        curatorFramework.create().withMode(CreateMode.PERSISTENT).forPath(path,"event".getBytes());
        TimeUnit.SECONDS.sleep(1);
        System.out.println("1");
        curatorFramework.create().withMode(CreateMode.EPHEMERAL).forPath(paths,"1".getBytes());
        TimeUnit.SECONDS.sleep(1);
        System.out.println("2");

        curatorFramework.setData().forPath(paths,"222".getBytes());
        TimeUnit.SECONDS.sleep(1);
        System.out.println("3");

        curatorFramework.delete().forPath(paths);
        System.out.println("4");

        System.in.read();
		
	}

	private static void nodeCache(CuratorFramework curatorFramework) throws Exception {
        NodeCache cache=new NodeCache(curatorFramework,"/curator",false);
        cache.start(true);
        cache.getListenable().addListener(()-> 
        	System.out.println("节点数据发生变化,变化后的结果："+new String(cache.getCurrentData().getData()))
        );
        Stat ret = curatorFramework.setData().forPath("/curator","菲菲".getBytes());
		System.out.println("nodeCache->"+ret);
	}
}
