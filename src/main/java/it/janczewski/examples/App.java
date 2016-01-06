package it.janczewski.examples;

import it.janczewski.examples.utils.Reciver;
import it.janczewski.examples.utils.Sender;

public class App {

    public static void main(String[] args) throws Exception {
        Sender sender = new Sender();
        sender.createTask();
        Thread.sleep(3000);
        Reciver reciver = new Reciver();
        reciver.createRecieveTask();
    }
}
