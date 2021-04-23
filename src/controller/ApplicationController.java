package controller;

import api.ApiRequest;
import application.Application;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.net.http.HttpResponse;
import java.util.ArrayList;

public class ApplicationController {

    private ArrayList<ObserverOutputInterface> subscribers;
    public static final String USER_LISTENER = "UserListener";
    public static final String CONTRACT_LISTENER = "ContractListener";

    public ApplicationController(ObserverInputInterface inputPage, String listenerType) {
        subscribers = new ArrayList<>();
        if (listenerType.equals(USER_LISTENER)) {
            inputPage.addActionListener(new LoginListener(inputPage));
        } else if (listenerType.equals(CONTRACT_LISTENER)) {
            inputPage.addActionListener(new ContractListener(inputPage));
        }
    }

    public void subscribe(ObserverOutputInterface subscriber) {
        subscribers.add(subscriber);
    }

    public void unsubscribe(ObserverOutputInterface subscriber) {subscribers.remove(subscriber);}

    public void notifySubscribers(String userId) {
        for (ObserverOutputInterface subscriber : subscribers) {
            subscriber.update(userId);
        }
    }

    class LoginListener extends EventListener {

        private ObserverInputInterface inputPage;

        public LoginListener(ObserverInputInterface inputPage) {
            super(inputPage);
        }

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

    class ContractListener extends EventListener {

        private ObserverInputInterface inputPage;

        public ContractListener(ObserverInputInterface inputPage) {
            super(inputPage);
        }

        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }
}
