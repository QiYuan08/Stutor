package event_manager;

import interfaces.EventSubscriber;

import java.util.ArrayList;
import java.util.HashMap;

public class EventManager {

    HashMap<String, ArrayList<EventSubscriber>> subscribersTable = new HashMap<>();
    public static final String USER = "user";
    public static final String BID = "bid";
    public static final String CONTRACT = "contract";

    public EventManager() {
//        for (String component : components) {
//            this.subscribers.put(component, new ArrayList<>());
//        } String... components
        subscribersTable.put(USER, new ArrayList<>());
        subscribersTable.put(BID, new ArrayList<>());
        subscribersTable.put(CONTRACT, new ArrayList<>());
    }

    public void subscribe(String component, EventSubscriber subscriber) {
        ArrayList<EventSubscriber> subscribers = subscribersTable.get(component);
        subscribers.add(subscriber);
    }

    public void unsubscribe(String component, EventSubscriber subscriber) {
        ArrayList<EventSubscriber> subscribers = subscribersTable.get(component);
        subscribers.remove(subscriber);
    }

    public void notify(String component, String jsonObj) {
        ArrayList<EventSubscriber> subscribers = subscribersTable.get(component);
        for (EventSubscriber subscriber : subscribers) {
            subscriber.update(jsonObj);
        }
    }
}