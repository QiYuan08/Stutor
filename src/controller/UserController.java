package controller;

import api.ApiRequest;
import application.Application;
import event_manager.EventManager;
import event_manager.EventSubscriber;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.http.HttpResponse;
import java.util.ArrayList;

public class UserController {

    private InputInterface inputPage;
    private ArrayList<EventSubscriber> subscribers;

    public UserController(InputInterface inputPage) {
        this.inputPage = inputPage;
    }

    public void subscribe(EventSubscriber subscriber) {
        subscribers.add(subscriber);
    }

    public void unsubscribe(EventSubscriber subscriber) {
        subscribers.remove(subscriber);
    }

    public void notifySubscribers() {
        for (EventSubscriber subscriber : subscribers) {
            subscriber.update();
        }
    }

    class UserListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String[] inputs = inputPage.retrieveInputs();
            String jsonObj = "{ \"userName\": \"" + inputs[0] + "\", \"password\": \"" + inputs[1] + "\"}";

            HttpResponse<String> response = ApiRequest.post("/user/login", jsonObj);
            if (response.statusCode() == 200) {
                notifySubscribers();
                loadDashboardPage(inputs[0]);
            } else if (response.statusCode() == 403) {
                JOptionPane.showMessageDialog(new JFrame(), "The username you have entered is invalid. Please try again.",
                        "Username Invalid", JOptionPane.ERROR_MESSAGE);
            } else {
                System.out.println(response.statusCode());
            }
        }
        private void loadDashboardPage(String username) {
            HttpResponse<String> response = ApiRequest.get("/user");
            if (response.statusCode() == 200) {
                JSONArray users = new JSONArray(response.body());
                JSONObject user;
                String userId = null;
                for (int i = 0; i < users.length(); i++) {
                    user = users.getJSONObject(i);
                    if (user.get("userName").equals(username)) {
                        userId = user.get("id").toString();
                        Application.getEventManager().notify(EventManager.USER, user.toString());
                        break;
                    }
                }
                response = ApiRequest.get("/user/" + userId + "?fields=competencies.subject");
//                JSONArray competencies = new JSONArray(new JSONObject(response.body()).get("competencies"));
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            } else {
                System.out.println(response.statusCode());
            }
            Application.loadPage(Application.DASHBOARD_PAGE);

        }
    }
}
