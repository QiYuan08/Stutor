package controller;

import java.util.ArrayList;

public class ApplicationController {

    private ArrayList<ObserverOutputInterface> subscribers;

    public ApplicationController() {
        this.subscribers = new ArrayList<>();
    };

    public void subscribe(ObserverOutputInterface subscriber) {
        subscribers.add(subscriber);
    }

    public void unsubscribe(ObserverOutputInterface subscriber) {subscribers.remove(subscriber);}

    public void notifySubscribers(String userId) {
        for (ObserverOutputInterface subscriber : subscribers) {
            subscriber.update(userId);
        }
    }
}
