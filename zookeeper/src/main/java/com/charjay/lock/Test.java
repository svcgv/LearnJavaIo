package com.charjay.lock;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class Test {
    public static void main(String[] args) {
        final CountDownLatch latch=new CountDownLatch(10);
        Random random=new Random();
        for(int i=0;i<10;i++){
            new Thread(()->{
                DistributeLock lock=null;
                try {
                    lock=new DistributeLock();
                    latch.countDown();
                    latch.await();
                    lock.lock();
                    Thread.sleep(random.nextInt(500));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    if(lock!=null){
                        lock.unlock();
                    }
                }
            }).start();
        }
    }
}
