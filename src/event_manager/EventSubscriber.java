package event_manager;

import model.User;

public interface EventSubscriber {

    void update(String eventType, String jsonObj);

}
