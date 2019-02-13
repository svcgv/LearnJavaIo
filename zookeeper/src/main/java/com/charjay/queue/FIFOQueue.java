package com.charjay.queue;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import com.charjay.Config;

/**
 * http://blog.fens.me/zookeeper-queue-fifo/
 * 
 * @author Tony
 *
 */
public class FIFOQueue {
	protected static ZooKeeper zk = null;

	protected static Integer mutex;

	int sessionTimeout = 10000;

	protected String root = "/app1";

	public FIFOQueue(String connectString) {
		if (zk == null) {
			try {
				zk = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
					@Override
					public void process(WatchedEvent event) {
						System.out.println("receive watch event :" + event);
					}
				});
				mutex = new Integer(-1);
			} catch (IOException e) {
				zk = null;
			}
		}
		try {
			Stat s = zk.exists(root, false);
			if (s == null) {
				zk.create(root, "task-queue-fifo".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			}
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 生产者
	 *
	 * @param i
	 * @return
	 */

	boolean produce(int i) throws KeeperException, InterruptedException {
		ByteBuffer b = ByteBuffer.allocate(4);
		byte[] value;
		b.putInt(i);
		value = b.array();
		zk.exists("/queue/start", true);//?
		zk.create(root + "/element", value, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
		return true;
	}

	/**
	 * 消费者
	 *
	 * @return
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	int consume() throws KeeperException, InterruptedException {
		int retvalue = -1;
		Stat stat = null;
		while (true) {
			synchronized (mutex) {
				List<String> list = zk.getChildren(root, true);
				if (list.size() == 0) {
					mutex.wait();
				} else {
					String node = list.get(0).substring(7);
					Integer min = new Integer(node);
					for (String s : list) {
						String tempNode = s.substring(7);
						Integer tempValue = new Integer(tempNode);
						if (tempValue < min) {
							min = tempValue;
							node = tempNode;
						}
					}
					byte[] b = zk.getData(root + "/element" + node, false, stat);
					zk.delete(root + "/element" + node, 0);
					ByteBuffer buffer = ByteBuffer.wrap(b);
					retvalue = buffer.getInt();
					return retvalue;
				}
			}
		}
	}

	public static void main(String args[]) {
		FIFOQueue q = new FIFOQueue(Config.CONNECTSTRING);
		int i;
		Integer max = new Integer(5);
		System.out.println("Producer");
		for (i = 0; i < max; i++){
			try {
				q.produce(10 + i);
			} catch (KeeperException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		for (i = 0; i < max; i++) {
			try {
				int r = q.consume();
				System.out.println("Item: " + r);
			} catch (KeeperException e) {
				i--;
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		// System.out.println("element0000000000".substring(7));

	}

}
