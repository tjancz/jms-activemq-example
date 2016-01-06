package it.janczewski.examples.utils;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.jms.*;

public class Reciver implements ExceptionListener{
    private final Logger logger = LoggerFactory.getLogger(Reciver.class);

    public void createRecieveTask() {
        Runnable recTask = () -> {
            try {
                ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
                Connection connection = connectionFactory.createConnection();
                connection.start();
                connection.setExceptionListener(this);
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                Destination destination = session.createQueue("TJ Test");
                MessageConsumer consumer = session.createConsumer(destination);
                Message message = consumer.receive(4000);
                if (message instanceof TextMessage) {
                    TextMessage textMessage = (TextMessage) message;
                    String text = textMessage.getText();
                   logger.info("Received TextMessage object: " + text);
                } else {
                    logger.info("Received other object type with message: " + message);
                }
                consumer.close();
                session.close();
                connection.close();

            } catch (JMSException e) {
                logger.error("Reciver createRecieveTask method error", e);
            }
        };
        new Thread(recTask).start();
    }

    @Override
    public void onException(JMSException exception) {
        logger.error("Recieve error occured.");
    }
}
