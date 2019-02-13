package com.charjay.base.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import com.charjay.Config;

/**
 */
public class CuratorClientUtils {

    private static CuratorFramework curatorFramework;

    public static CuratorFramework getInstance(){
        curatorFramework= CuratorFrameworkFactory.
                newClient(Config.CONNECTSTRING,5000,5000,
                        new ExponentialBackoffRetry(1000,3));
        curatorFramework.start();
        return curatorFramework;
    }
}
