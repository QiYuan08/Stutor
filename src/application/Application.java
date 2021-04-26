package application;

import controller.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Application extends JFrame{
    public static final String LOGIN_PAGE = "LoginPage";
    public static final String REGISTRATION_PAGE = "RegistrationPage";
    public static final String DASHBOARD_PAGE = "DashboardPage";
    public static final String PROFILE_PAGE = "ProfilePage";
    public static final String OPEN_BID_PAGE = "OpenBidPage";
    public static final String ALL_BID = "AllBidPage";
    public static final String VIEW_BID = "ViewBidPage";
    private static JPanel rootPanel;
    private static CardLayout cardLayout;
    ApplicationController loginController, contractController, allBidController, viewBidController, openBidController, dashboardController;
    ActionListener contractListener, loginListener, allBidListener, viewBidListener, openBidListener, closeBidListener, dashboardListener;

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
        AllBidPage allBid = new AllBidPage();
        ViewBidPage viewBid = new ViewBidPage();

        rootPanel.add(loginPage, LOGIN_PAGE);
        rootPanel.add(registrationPage, REGISTRATION_PAGE);
        rootPanel.add(dashboardPage, DASHBOARD_PAGE);
        rootPanel.add(profilePage, PROFILE_PAGE);
        rootPanel.add(openBidPage, OPEN_BID_PAGE);
        rootPanel.add(allBid, ALL_BID);
        rootPanel.add(viewBid, VIEW_BID);

        loginController = new ApplicationController();
        loginListener = new LoginListener(loginPage, loginController);
        loginController.subscribe(profilePage);
        loginController.subscribe(dashboardPage);
        loginController.subscribe(openBidPage);

        // passing bidId between AllBid page and ViewBid page
        allBidController = new ApplicationController();
        allBidListener = new AllBidListener(allBid, allBidController);
        allBidController.subscribe(viewBid);

        dashboardController = new ApplicationController();
        dashboardListener = new DashBoardListener(dashboardPage, dashboardController);
        dashboardController.subscribe(allBid);
        dashboardController.subscribe((ObserverOutputInterface) allBidListener); // for all bid page to update all its button

        contractController = new ApplicationController();
        contractListener = new ContractListener(openBidPage, contractController);
        contractController.subscribe(dashboardPage);

        // controller for user to open bid
        // TODO: refactor openbid listener so that constructor nonid controller if no other class subscribing it
        openBidController = new ApplicationController();
        openBidListener = new OpenBidListener(openBidPage, openBidController);



        // listener for closing bid
        closeBidListener = new CloseBidListener(viewBid);
//        viewBidController = new ApplicationController();
//        viewBidListener = new CloseBidListener(viewBid, viewBidController);

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
