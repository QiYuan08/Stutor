package controller;

import java.util.ArrayList;

public class Controller {

    private ArrayList<ObserverOutputInterface> subscribers;

    public Controller() {
        this.subscribers = new ArrayList<>();
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
