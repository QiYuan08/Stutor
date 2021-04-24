package event_manager;

import interfaces.BidEventSubscriber;

import java.util.ArrayList;

/**
 * Event listener class for Bid event specifically for passing bidId between class for
 * closing and updating bid
 */
public class BidEventManager {

    ArrayList<BidEventSubscriber> subscribers = new ArrayList<>();
    public static final String BID = "bid";

    public void subscribe(BidEventSubscriber subscriber) {
        subscribers.add(subscriber);
    }

    public void unsubscribe(BidEventSubscriber subscriber) {
        subscribers.remove(subscriber);
    }

    public void notify(String bidId) {
        for (BidEventSubscriber subscriber : subscribers) {
            subscriber.updateBidId(bidId);
        }
    }
}
