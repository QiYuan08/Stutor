package controller;

import api.ApiRequest;
import application.Application;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.http.HttpResponse;
import java.util.ArrayList;

public class LoginController {

    private ObserverInputInterface inputPage;
    private ArrayList<ObserverOutputInterface> subscribers;

    public LoginController(ObserverInputInterface inputPage) {
        this.inputPage = inputPage;
        subscribers = new ArrayList<>();
        inputPage.addActionListener(new LoginListener());
    }

    public void subscribe(ObserverOutputInterface subscriber) {
        subscribers.add(subscriber);
    }

    public void unsubscribe(ObserverOutputInterface subscriber) {
        subscribers.remove(subscriber);
    }

    public void notifySubscribers(String userId) {
        for (ObserverOutputInterface subscriber : subscribers) {
            subscriber.update(userId);
        }
    }

    class LoginListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JSONObject jsonObj = inputPage.retrieveInputs();
            HttpResponse<String> response = ApiRequest.post("/user/login", jsonObj.toString());

            if (response.statusCode() == 200) {
                notifySubscribers(jsonObj.get("userName").toString());
                Application.loadPage(Application.DASHBOARD_PAGE);
            } else if (response.statusCode() == 403) {
                JOptionPane.showMessageDialog(new JFrame(), "The username you have entered is invalid. Please try again.",
                        "Username Invalid", JOptionPane.ERROR_MESSAGE);
            } else {
                System.out.println(response.statusCode());
            }
        }
    }
}
