package application;

import application.bid.CreateOpenBid;
import controller.LoginController;
import event_manager.EventManager;

import javax.swing.*;
import java.awt.*;

public class Application extends JFrame{
    public static final String LOGIN_PAGE = "LoginPage";
    public static final String REGISTRATION_PAGE = "RegistrationPage";
    public static final String DASHBOARD_PAGE = "DashboardPage";
    public static final String PROFILE_PAGE = "ProfilePage";
    public static final String OPEN_BID = "OpenBid";
    private static JPanel rootPanel;
    private static CardLayout cardLayout;
//    private static EventManager eventManager;
//    private static model.User user;

    private Application() {
        super("StuTor");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500, 400);
        rootPanel = new JPanel();
        cardLayout = new CardLayout();
        rootPanel.setLayout(cardLayout);

        LoginPage loginPage = new LoginPage();
        RegistrationPage registrationPage = new RegistrationPage();
        DashboardPage dashboardPage = new DashboardPage();
        ProfilePage profilePage = new ProfilePage();

        rootPanel.add(loginPage, LOGIN_PAGE);
        rootPanel.add(registrationPage, REGISTRATION_PAGE);
        rootPanel.add(dashboardPage, DASHBOARD_PAGE);
        rootPanel.add(profilePage, PROFILE_PAGE);
//        rootPanel.add(new CreateOpenBid());

//        eventManager = new EventManager();
//        eventManager.subscribe(eventManager.USER, profilePage);
//        eventManager.subscribe(eventManager.CONTRACT, dashboardPage);

        LoginController loginController = new LoginController(loginPage);
        loginController.subscribe(profilePage);
        loginController.subscribe(dashboardPage);

        this.add(rootPanel);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    Application application = new Application();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void loadPage(String pageName) {
        cardLayout.show(rootPanel, pageName);
    }

//    public static EventManager getEventManager() {return eventManager;}
}
