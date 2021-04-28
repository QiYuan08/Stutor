package application;

import application.bid_pages.FindBidDetail;
import application.bid_pages.FindBidPage;
import application.bid_pages.OpenBidPage;
import application.bid_pages.SeeBidsPage;
import controller.*;
import listeners.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Application extends JFrame{
    private static JPanel rootPanel;
//    private static JPanel loginPage, registrationPage, dashboardPage, profilePage, openBidPage, viewBidPage, userBidsPage;
    private static CardLayout cardLayout;
    ApplicationController loginController, contractController, findBidController,seeBidController, openBidController, dashboardController, updateViewController;
    ActionListener contractListener, loginListener, findBidListener, seeBidListener, openBidListener, closeBidListener, dashboardListener;

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
        FindBidDetail findBidDetail = new FindBidDetail();
        SeeBidsPage seeBidsPage = new SeeBidsPage();

        rootPanel.add(loginPage, ApplicationManager.LOGIN_PAGE);
        rootPanel.add(registrationPage, ApplicationManager.REGISTRATION_PAGE);
        rootPanel.add(dashboardPage, ApplicationManager.DASHBOARD_PAGE);
        rootPanel.add(profilePage, ApplicationManager.PROFILE_PAGE);
        rootPanel.add(openBidPage, ApplicationManager.OPEN_BID_PAGE);
        rootPanel.add(findBidPage, ApplicationManager.FIND_BID);
        rootPanel.add(findBidDetail, ApplicationManager.VIEW_BID);
        rootPanel.add(seeBidsPage, ApplicationManager.USER_BIDS);


        // listener for closing bid
        // TODO: update findBidPage after closing
        updateViewController = new ApplicationController();
        closeBidListener = new CloseBidListener(findBidDetail, updateViewController);
        updateViewController.subscribe(findBidPage);
        updateViewController.subscribe(seeBidsPage);

        // passing studentId between classes
        loginController = new ApplicationController();
        loginListener = new LoginListener(loginPage, loginController);
        loginController.subscribe(profilePage);
        loginController.subscribe(dashboardPage);
        loginController.subscribe(openBidPage);
        loginController.subscribe(seeBidsPage);
        loginController.subscribe(findBidPage);
        loginController.subscribe((ObserverOutputInterface) closeBidListener); // get the userId to update other bidding page

        // passing bidId between FindBidPage and ViewBid page
        findBidController = new ApplicationController();
        findBidListener = new FindBidListener(findBidPage, findBidController);
        findBidController.subscribe(findBidDetail);

        // passing bidId between SeeBidPage and ViewBid page
        seeBidController = new ApplicationController();
        seeBidListener = new SeeBidListener(seeBidsPage, seeBidController);
        seeBidController.subscribe(findBidDetail);

        // dashboardController needed for find bid pages to add event listener for all of its button
        // this controller is called when user click on findBid Button and seeBid button in dashboard
        dashboardController = new ApplicationController();
        dashboardListener = new DashBoardListener(dashboardPage, dashboardController); // userId are updated from here
        dashboardController.subscribe(findBidPage);
        dashboardController.subscribe((ObserverOutputInterface) findBidListener); // for find bid page to update all its button
        dashboardController.subscribe(seeBidsPage);
        dashboardController.subscribe((ObserverOutputInterface) seeBidListener); // for see bid page to update all its button


        contractController = new ApplicationController();
        contractListener = new ContractListener(openBidPage, contractController);
        contractController.subscribe(dashboardPage);

        // controller for user to open bid
        // TODO: refactor openbid listener so that constructor nonid controller if no other class subscribing it
        openBidController = new ApplicationController();
        openBidListener = new OpenBidListener(openBidPage, openBidController);


//        viewBidController = new ApplicationController();
//        viewBidListener = new CloseBidListener(, viewBidController);

        ApplicationManager.setRootPanel(rootPanel);

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

//    public static void loadPage(String pageName) {
//        cardLayout.show(rootPanel, pageName);
//    }
//
//    public static void loadPage(String pageName, String context) {
//        for (Component component: rootPanel.getComponents()) {
//            if (component instanceof ObserverOutputInterface) {
//                ObserverOutputInterface page = (ObserverOutputInterface) component;
//                page.update(context);
//            }
//        }
//        cardLayout.show(rootPanel, pageName);
//    }

}
