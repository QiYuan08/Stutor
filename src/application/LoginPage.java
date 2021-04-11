package application;

import api.ApiRequest;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.http.HttpResponse;

public class LoginPage extends JPanel {

    private JLabel activityTitle, usernameField, passwordField, registerField;
    private JTextField usernameInput;
    private JPasswordField passwordInput;
    private JButton loginUserButton, registerPageButton;
    private HttpResponse<String> response;

    LoginPage () {
        this.setBorder(new EmptyBorder(15, 15, 15, 15));
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
//        c.weightx = 1;
//        c.weighty = 1;
        c.insets = new Insets(5, 5, 0, 5);

        activityTitle = new JLabel("User Login");
        activityTitle.setHorizontalAlignment(JLabel.CENTER);
        activityTitle.setVerticalAlignment(JLabel.TOP);
        activityTitle.setFont(new Font("Bahnschrift", Font.BOLD, 20));
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        c.fill = GridBagConstraints.HORIZONTAL;
        this.add(activityTitle, c);


        usernameField = new JLabel("Username: ");
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        this.add(usernameField, c);
        usernameInput = new JTextField();
        c.gridx = 1;
        c.gridwidth = 2;
        this.add(usernameInput, c);

        passwordField = new JLabel("Password: ");
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        this.add(passwordField, c);
        passwordInput = new JPasswordField();
        c.gridx = 1;
        c.gridwidth = 2;
        this.add(passwordInput, c);

        loginUserButton = new JButton("Log In");
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 3;
        this.add(loginUserButton, c);

        registerField = new JLabel("If you do not have a StuTor account, register for one instead here:");
        c.gridy = 5;
        c.gridx = 0;
        this.add(registerField, c);

        registerPageButton = new JButton("Register a StuTor Account");
        c.gridy = 6;
        c.gridx = 0;
        this.add(registerPageButton, c);

        loginUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });

        registerPageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadRegisterPage();
            }
        });
    }
    private void login() {
        String username = usernameInput.getText();
        String password = passwordInput.getText();
        String jsonObj = "{ \"userName\": \"" + username +
                "\", \"password\": \"" + password + "\"}";
        try {
            response = ApiRequest.post("/user/login", jsonObj);
            if (response.statusCode() == 200) {
                loadDashboardPage(username);
            } else if (response.statusCode() == 400) {
                JOptionPane.showMessageDialog(new JFrame(), "The username you have entered is invalid. Please try again.",
                        "Username Invalid", JOptionPane.ERROR_MESSAGE);
            } else {
                System.out.println(response.statusCode());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadRegisterPage() {
        Application.loadPage(Application.REGISTRATION_PAGE);
    }

//    private void loadDashboardPage(String userId) {
//        String jsonObj = "{ \"userName\": \"" + userId + "\"}";
//        try {
//            response = ApiRequest.get("/user");
//            if (response.statusCode() == 200) {
//
//                System.out.println(response.headers().map());
////                System.out.println(response.body());
//                String res = response.body();
//            } else {
//                System.out.println(response.statusCode());
//            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Application.loadPage(Application.DASHBOARD_PAGE);
//
//    }

}
