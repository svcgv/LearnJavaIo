package com.charjay.master.zkclient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

import com.charjay.Config;

/**
 */
public class MasterChooseTest {


    public static void main(String[] args) throws IOException {
        List<MasterSelector> selectorLists=new ArrayList<>();
        try {
            for(int i=0;i<10;i++) {
                ZkClient zkClient = new ZkClient(Config.CONNECTSTRING, 5000,
                        5000,
                        new SerializableSerializer());
                UserCenter userCenter = new UserCenter();
                userCenter.setMc_id(i);
                userCenter.setMc_name("客户端：" + i);

                MasterSelector selector = new MasterSelector(userCenter,zkClient);
                selectorLists.add(selector);
                selector.start();//触发选举操作
                TimeUnit.SECONDS.sleep(1);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            for(MasterSelector selector:selectorLists){
                selector.stop();
            }
        }
    }
}
