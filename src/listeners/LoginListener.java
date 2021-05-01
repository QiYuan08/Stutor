package listeners;

import api.ApiRequest;
import controller.Listener;
import services.ViewManagerService;
import controller.ObserverInputInterface;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.http.HttpResponse;

public class LoginListener extends Listener implements ActionListener {

    private ObserverInputInterface inputPage;
    public LoginListener(ObserverInputInterface inputPage) {
        super();
        this.inputPage = inputPage;
        inputPage.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JSONObject jsonObj = inputPage.retrieveInputs();
        HttpResponse<String> response = ApiRequest.post("/user/login", jsonObj.toString());
        if (response.statusCode() == 200) {
            notifySubscribers(getUserId(jsonObj.getString("userName")));
            ViewManagerService.loadPage(ViewManagerService.DASHBOARD_PAGE);
        } else if (response.statusCode() == 403) {
            JOptionPane.showMessageDialog(new JFrame(), "The username you have entered is invalid. Please try again.",
                    "Username Invalid", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(new JFrame(), "Login failed! Error: " + response.statusCode(),
                    "Username Invalid", JOptionPane.ERROR_MESSAGE);
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
