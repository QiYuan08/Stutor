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
    public static final String FIND_BID = "FindBidPage";
    public static final String VIEW_BID = "ViewBidPage";
    public static final String USER_BIDS = "UserBidsPage";
    private static JPanel rootPanel;
//    private static JPanel loginPage, registrationPage, dashboardPage, profilePage, openBidPage, viewBidPage, userBidsPage;
    private static CardLayout cardLayout;
    ApplicationController loginController, contractController, findBidController, userBidController, openBidController, dashboardController;
    ActionListener contractListener, loginListener, findBidListener, viewBidListener, openBidListener, closeBidListener, dashboardListener;

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
        FindBidPage findBidPage = new FindBidPage();
        ViewBidPage viewBidPage = new ViewBidPage();
        SeeBidsPage userBidsPage = new SeeBidsPage();

        rootPanel.add(loginPage, LOGIN_PAGE);
        rootPanel.add(registrationPage, REGISTRATION_PAGE);
        rootPanel.add(dashboardPage, DASHBOARD_PAGE);
        rootPanel.add(profilePage, PROFILE_PAGE);
        rootPanel.add(openBidPage, OPEN_BID_PAGE);
        rootPanel.add(findBidPage, FIND_BID);
        rootPanel.add(viewBidPage, VIEW_BID);
        rootPanel.add(userBidsPage, USER_BIDS);

        // passing studentId between classes
        loginController = new ApplicationController();
        loginListener = new LoginListener(loginPage, loginController);
        loginController.subscribe(profilePage);
        loginController.subscribe(dashboardPage);
        loginController.subscribe(openBidPage);
        loginController.subscribe(userBidsPage);
        loginController.subscribe(findBidPage);

        // passing bidId between AllBid page and ViewBid page
        findBidController = new ApplicationController();
        findBidListener = new FindBidListener(findBidPage, findBidController);
        findBidController.subscribe(viewBidPage);

        // needed for find bid pages to add event listener for all of its button
        dashboardController = new ApplicationController();
        dashboardListener = new DashBoardListener(dashboardPage, dashboardController); // userId are updated from here
        dashboardController.subscribe((ObserverOutputInterface) findBidListener); // for all bid page to update all its button
        dashboardController.subscribe(userBidsPage);


        // passing
//        dashboardController.subscribe(openBidPage);

        contractController = new ApplicationController();
        contractListener = new ContractListener(openBidPage, contractController);
        contractController.subscribe(dashboardPage);

        // controller for user to open bid
        // TODO: refactor openbid listener so that constructor nonid controller if no other class subscribing it
        openBidController = new ApplicationController();
        openBidListener = new OpenBidListener(openBidPage, openBidController);



        // listener for closing bid
        // TODO: update findBidPage after closing
        closeBidListener = new CloseBidListener(viewBidPage);
//        viewBidController = new ApplicationController();
//        viewBidListener = new CloseBidListener(viewBidPage, viewBidController);

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

    public static void loadPage(String pageName, String context) {
        ObserverOutputInterface page = (ObserverOutputInterface) rootPanel.getClientProperty(pageName);
        page.update(context);
        cardLayout.show(rootPanel, pageName);
    }

}
