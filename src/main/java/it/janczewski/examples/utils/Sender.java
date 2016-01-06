package it.janczewski.examples.utils;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.math.BigInteger;
import java.security.SecureRandom;

public class Sender {
    private final Logger logger = LoggerFactory.getLogger(Sender.class);
    private SecureRandom random = new SecureRandom();

    public void createTask(){
        String taskName = generateTaskName();
        Runnable sendTask = () -> {
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
            Connection connection = null;
            try {
                connection = connectionFactory.createConnection();
                connection.start();
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                Destination destination = session.createQueue("TJ Test");
                MessageProducer producer = session.createProducer(destination);
                producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
                String text = "Hello from: " + taskName + " : " + this.hashCode();
                TextMessage message = session.createTextMessage(text);
                logger.info("Sent message hash code: "+ message.hashCode() + " : " + taskName);
                producer.send(message);
                session.close();
                connection.close();
            } catch (JMSException e) {
                logger.error("Sender createTask method error", e);
            }
        };
        new Thread(sendTask).start();
    }

    private String generateTaskName() {
        return new BigInteger(20, random).toString(16);
    }
}
