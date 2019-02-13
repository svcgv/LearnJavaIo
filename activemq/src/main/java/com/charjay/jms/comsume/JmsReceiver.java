package com.charjay.jms.comsume;

import com.charjay.jms.Config;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**

 */
public class JmsReceiver {

    public static void main(String[] args) {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(Config.ACTIVE_MQ_URL);
        Connection connection = null;
        try {
            //创建连接
            connection = connectionFactory.createConnection();
            connection.start();

            //
            Session session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);

            //创建队列（如果队列已经存在则不会创建， first-queue是队列名称）
            //destination表示目的地
            Destination destination = session.createQueue("first-queue");
            //创建消息接收者
            MessageConsumer consumer = session.createConsumer(destination);

            TextMessage textMessage = (TextMessage) consumer.receive();
//            textMessage.getStringProperty("key");

            System.out.println(textMessage.getText());
            session.commit();//签收消息，如果不签收，那么消息会一直存在
            session.close();
        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }


    }
}