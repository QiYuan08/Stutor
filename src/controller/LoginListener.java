package controller;

import api.ApiRequest;
import application.Application;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.http.HttpResponse;

public class LoginListener implements ActionListener {

    private ObserverInputInterface inputPage;
    private ApplicationController applicationController;

    public LoginListener(ObserverInputInterface inputPage, ApplicationController applicationController) {
        this.inputPage = inputPage;
        this.applicationController = applicationController;
        inputPage.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JSONObject jsonObj = this.inputPage.retrieveInputs();
        HttpResponse<String> response = ApiRequest.post("/user/login", jsonObj.toString());

        if (response.statusCode() == 200) {
            applicationController.notifySubscribers(jsonObj.get("userName").toString());
            Application.loadPage(Application.DASHBOARD_PAGE);
        } else if (response.statusCode() == 403) {
            JOptionPane.showMessageDialog(new JFrame(), "The username you have entered is invalid. Please try again.",
                    "Username Invalid", JOptionPane.ERROR_MESSAGE);
        } else {
            System.out.println(response.statusCode());
        }
    }
}
