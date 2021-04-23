package application;

import controller.ApplicationController;
import controller.ContractListener;
import controller.LoginListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Application extends JFrame{
    public static final String LOGIN_PAGE = "LoginPage";
    public static final String REGISTRATION_PAGE = "RegistrationPage";
    public static final String DASHBOARD_PAGE = "DashboardPage";
    public static final String PROFILE_PAGE = "ProfilePage";
    public static final String OPEN_BID_PAGE = "OpenBidPage";
    private static JPanel rootPanel;
    private static CardLayout cardLayout;
    ApplicationController loginController, contractController;
    ActionListener contractListener, loginListener;

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
        OpenBidPage openBidPage = new OpenBidPage();

        rootPanel.add(loginPage, LOGIN_PAGE);
        rootPanel.add(registrationPage, REGISTRATION_PAGE);
        rootPanel.add(dashboardPage, DASHBOARD_PAGE);
        rootPanel.add(profilePage, PROFILE_PAGE);
        rootPanel.add(openBidPage, OPEN_BID_PAGE);

        loginController = new ApplicationController();
        loginListener = new LoginListener(loginPage, loginController);
        loginController.subscribe(profilePage);
        loginController.subscribe(dashboardPage);

        contractController = new ApplicationController();
        contractListener = new ContractListener(openBidPage, contractController);
        contractController.subscribe(dashboardPage);

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

}
