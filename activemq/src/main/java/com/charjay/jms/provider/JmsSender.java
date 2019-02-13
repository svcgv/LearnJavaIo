package com.charjay.jms.provider;

import com.charjay.jms.Config;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**

 */
public class JmsSender {

    public static void main(String[] args) {
        ActiveMQConnectionFactory connectionFactory=new ActiveMQConnectionFactory(Config.ACTIVE_MQ_URL);
        Connection connection=null;
        try {
            //创建连接
            connection=connectionFactory.createConnection();
            //设置消息发送端发送持久化消息的异步方式（提高性能）
//            connectionFactory.setUseAsyncSend(true);
            //回执窗口大小设置（当生产这发送的消息大小超过时则等待）
//            connectionFactory.setProducerWindowSize(100);
            connection.start();

            Session session=connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);

            //创建队列（如果队列已经存在则不会创建， first-queue是队列名称）
            //destination表示目的地
            Destination destination=session.createQueue("first-queue?consumer.prefetchSize=100");
            //创建消息发送者
            MessageProducer producer=session.createProducer(destination);

            TextMessage textMessage=session.createTextMessage("hello 啦啦");
            textMessage.setStringProperty("key","value");//设置属性
            //非持久化消息设置
//            textMessage.setJMSDeliveryMode(DeliveryMode.NON_PERSISTENT);
            //非持久化消息模式下，默认就是异步发送过程，如果需要对非持久化消息的每次发送的消息都获得broker的回执的话
            connectionFactory.setAlwaysSyncSend(true);
            //回执窗口大小设置（当生产这发送的消息大小超过时则等待）
//            connectionFactory.setProducerWindowSize(100);

            producer.send(textMessage);
            session.commit();
            session.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }finally {
            if(connection!=null){
                try {
                    connection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
