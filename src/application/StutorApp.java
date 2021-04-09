package application;

import api.ApiRequest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.http.HttpResponse;

public class StutorApp {
    private JFrame frame;
    private CardLayout cardLayout;
//    private LoginPage loginPage;
    private JPanel rootPanel, loginPage, dashboardPage, registrationPage, profilePage;
    private JLabel activityTitle, usernameField, passwordField, username, name, accountType;
    private JTextField usernameInput, regUsernameInput, gNameInput, fNameInput;
    private JPasswordField passwordInput, regPasswordInput;
    private JButton loginUserButton, registerUserButton, editProfileButton, registerPageButton, dashboardPageButton;
    private JButton tutorial1Button, tutorial2Button, tutorial3Button, tutorial4Button, tutorial5Button;
    private JList competenciesList, qualificationsList, initiatedBidsList;
    private JCheckBox studentCheckBox, tutorCheckBox;
    private HttpResponse<String> response;

    public StutorApp() {
        frame = new JFrame("StuTor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
//        loginPage = new LoginPage();
//        rootPanel = new JPanel();
//        loginPage = new JPanel();
//        dashboardPage = new JPanel();
//
//        loginUserButton = new JButton("log in");
//        loginPage.add(loginUserButton);
//        cardLayout = new CardLayout();
//        rootPanel.setLayout(cardLayout);
//        rootPanel.add(loginPage, "Login Page");
//        rootPanel.add(dashboardPage, "Dashboard");
//        dashboardPage.setName("dashboardPage");
//        loginUserButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {login();}
//        });

        editProfileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) rootPanel.getLayout();
                cl.next(rootPanel);
            }
        });

        registerPageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) rootPanel.getLayout();
                cl.next(rootPanel);
            }
        });

        dashboardPageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) rootPanel.getLayout();
                cl.previous(rootPanel);
            }
        });

//        registerUserButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                register();
//            }
//        });

        frame.add(rootPanel);
        frame.setVisible(true);
    }

//    private void login() {
//        String username = usernameInput.getText();
//        String password = passwordInput.getText();
//        String jsonObj = "{ \"userName\": \"" + username +
//                "\", \"password\": \"" + password + "\"}";
//        try {
//            response = ApiRequest.post("/user/login", jsonObj);
//            if (response.statusCode() == 200) {
//                CardLayout cl = (CardLayout) rootPanel.getLayout();
//                cl.show(rootPanel, "dashboardPage");
//                cl.next(rootPanel);
//                cl.next(rootPanel);
//            } else {
//                System.out.println(response.statusCode());
//            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void register() {
//        String username = regUsernameInput.getText();
//        String password = regPasswordInput.getText();
//        String gName = gNameInput.getText();
//        String fName = fNameInput.getText();
//        boolean isStudent = studentCheckBox.isSelected();
//        boolean isTutor = tutorCheckBox.isSelected();
//        String jsonObj = "{ \"givenName\": \"" + gName + "\", \"familyName\": \"" + fName +
//                "\", \"userName\": \"" + username + "\", \"password\": \"" + password +
//                "\", \"isStudent\": \"" + isStudent + ", \"isTutor\": " + isTutor + "}";
//        try {
//            response = ApiRequest.post("/user", jsonObj);
//            if (response.statusCode() == 201) {
//                CardLayout cl = (CardLayout) rootPanel.getLayout();
//                cl.show(rootPanel, "dashboardPage");
////                cl.next(rootPanel);
//            } else if (response.statusCode() == 409) {
//                JOptionPane.showMessageDialog(new JFrame(), "This username has already been taken. Please try again.",
//                        "Username Taken", JOptionPane.ERROR_MESSAGE);
//            } else {
//                System.out.println(response.statusCode());
//            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    private void loadProfile() {
        response.body();
        System.out.println(response.body());
        java.util.Map<String, java.util.List<String>> data = response.headers().map();
    }


    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    StutorApp stutorApp = new StutorApp();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
