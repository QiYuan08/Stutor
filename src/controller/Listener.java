package controller;

import controller.ObserverInputInterface;
import controller.ObserverOutputInterface;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

public abstract class Listener implements ActionListener {

    protected ArrayList<ObserverOutputInterface> subscribers;
    protected ObserverInputInterface inputPage;

//    public Listener(ObserverInputInterface[] inputPages, ObserverOutputInterface[] outputPages) {
//        subscribers = new ArrayList<>();
//        for (ObserverInputInterface inputPage: inputPages) {
//            inputPage.addActionListener(this);
//        }
//        Collections.addAll(subscribers, outputPages);
//    }

    public Listener(ObserverInputInterface inputPage) {
        subscribers = new ArrayList<>();
        this.inputPage = inputPage;
    }

    public Listener() {
        subscribers = new ArrayList<>();
    }

    public void subscribe(ObserverOutputInterface subscriber) {
        subscribers.add(subscriber);
    }

    public void unsubscribe(ObserverOutputInterface subscriber) {subscribers.remove(subscriber);}

    public void notifySubscribers(String data) {
        for (ObserverOutputInterface subscriber : subscribers) {
            subscriber.update(data);
        }
    }
}
