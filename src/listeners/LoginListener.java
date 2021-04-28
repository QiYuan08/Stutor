package listeners;

import api.ApiRequest;
import application.Application;
import application.ApplicationManager;
import application.ProfilePage;
import controller.ApplicationController;
import controller.ObserverInputInterface;
import org.json.JSONArray;
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
            applicationController.notifySubscribers(getUserId(jsonObj.getString("userName")));
            ApplicationManager.loadPage(ApplicationManager.DASHBOARD_PAGE);
//            Application.loadPage(Application.DASHBOARD_PAGE, getUserId(jsonObj.getString("userName")));
        } else if (response.statusCode() == 403) {
            JOptionPane.showMessageDialog(new JFrame(), "The username you have entered is invalid. Please try again.",
                    "Username Invalid", JOptionPane.ERROR_MESSAGE);
        } else {
            System.out.println(response.statusCode());
        }
    }

    private String getUserId(String username) {
        HttpResponse<String> response = ApiRequest.get("/user");
        if (response.statusCode() == 200) {
            JSONArray users = new JSONArray(response.body());
            JSONObject user;
            for (int i = 0; i < users.length(); i++) {
                user = users.getJSONObject(i);
                if (user.get("userName").equals(username)) {
                    return user.get("id").toString();
                }
            }
        }
        return null;
    }
}
