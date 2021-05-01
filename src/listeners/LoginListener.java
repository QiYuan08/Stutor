package listeners;

import api.ApiRequest;
import controller.Controller;
import services.ViewManagerService;
import controller.ObserverInputInterface;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.http.HttpResponse;

public class LoginListener implements ActionListener {

    private ObserverInputInterface inputPage;
    private Controller controller;

    public LoginListener(ObserverInputInterface inputPage, Controller controller) {
        this.inputPage = inputPage;
        this.controller = controller;
        inputPage.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JSONObject jsonObj = inputPage.retrieveInputs();
        HttpResponse<String> response = ApiRequest.post("/user/login", jsonObj.toString());

        if (response.statusCode() == 200) {
            controller.notifySubscribers(getUserId(jsonObj.getString("userName")));
            ViewManagerService.loadPage(ViewManagerService.DASHBOARD_PAGE);
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
