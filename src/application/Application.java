package application;

import application.bid_pages.*;
import controller.*;
import links.FindBidDetailLink;
import links.SeeBidDetailLink;
import listeners.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Application extends JFrame{
    private static JPanel rootPanel;
    private static CardLayout cardLayout;
    ApplicationController loginController, contractController, findBidController,seeBidController, dashboardController, bidClosingController;
    ActionListener contractListener, loginListener, findBidListener, seeBidListener, createBidListener, bidClosingListener, dashboardListener;

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
        CreateBidPage createBidPage = new CreateBidPage();
        FindBidPage findBidPage = new FindBidPage();
        FindBidsDetail findBidsDetail = new FindBidsDetail();
        SeeBidsPage seeBidsPage = new SeeBidsPage();
        MessagesPage messagesPage = new MessagesPage();

        rootPanel.add(loginPage, ApplicationManager.LOGIN_PAGE);
        rootPanel.add(registrationPage, ApplicationManager.REGISTRATION_PAGE);
        rootPanel.add(dashboardPage, ApplicationManager.DASHBOARD_PAGE);
        rootPanel.add(profilePage, ApplicationManager.PROFILE_PAGE);
        rootPanel.add(createBidPage, ApplicationManager.CREATE_BID_PAGE);
        rootPanel.add(findBidPage, ApplicationManager.FIND_BID);
        rootPanel.add(findBidsDetail, ApplicationManager.FIND_BID_DETAIL);
        rootPanel.add(seeBidsPage, ApplicationManager.SEE_BIDS_PAGE);
        rootPanel.add(messagesPage, ApplicationManager.MESSAGES_PAGE);


        // listener for closing bid
        // TODO: update findBidPage after closing
        bidClosingController = new ApplicationController();
        bidClosingListener = new BidClosingListener(findBidsDetail, bidClosingController);
        bidClosingController.subscribe(findBidPage);
        bidClosingController.subscribe(seeBidsPage);

        // passing studentId between classes
        loginController = new ApplicationController();
        loginListener = new LoginListener(loginPage, loginController);
        loginController.subscribe(profilePage);
        loginController.subscribe(dashboardPage);
        loginController.subscribe(createBidPage);
        loginController.subscribe(seeBidsPage);
        loginController.subscribe(findBidPage);
        loginController.subscribe((ObserverOutputInterface) bidClosingListener); // get the userId to update other bidding page
        loginController.subscribe(createBidPage);

        // passing bidId between FindBidPage and FindBidsDetail page
//        findBidController = new ApplicationController();
//        findBidListener = new FindBidListener(findBidPage, findBidController);
//        findBidController.subscribe(findBidsDetail);
        FindBidDetailLink findBidDetailLink = new FindBidDetailLink(findBidPage, findBidsDetail);

        // passing bidId between SeeBidPage and ViewBid page
//        seeBidController = new ApplicationController();
//        seeBidListener = new SeeBidListener(seeBidsPage, seeBidController);
//        seeBidController.subscribe(findBidsDetail);
        SeeBidDetailLink seeBidDetailLink = new SeeBidDetailLink(seeBidsPage, findBidsDetail);

        // dashboardController needed for find bid pages to add event listener for all of its button
        // this controller is called when user click on findBid Button and seeBid button in dashboard
        dashboardController = new ApplicationController();
        dashboardListener = new DashBoardListener(dashboardPage, dashboardController); // userId are updated from here
        dashboardController.subscribe(findBidPage);
        dashboardController.subscribe(findBidDetailLink);
//        dashboardController.subscribe((ObserverOutputInterface) findBidListener); // for find bid page to update all its button
        dashboardController.subscribe(seeBidsPage);
        dashboardController.subscribe(seeBidDetailLink);
//        dashboardController.subscribe((ObserverOutputInterface) seeBidListener); // for see bid page to update all its button


        contractController = new ApplicationController();
        contractListener = new ContractListener(createBidPage, contractController);
        contractController.subscribe(dashboardPage);

        // controller for user to open bid
        // TODO: refactor openbid listener so that constructor nonid controller if no other class subscribing it
//        createBidController = new ApplicationController();
        createBidListener = new CreateBidListener(createBidPage);

        ApplicationManager.setRootPanel(rootPanel);

        this.add(rootPanel);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {

                    // create a service class
                    CloseBidService service = new CloseBidService();
                    service.setDuration(30); //set the interval before closing automatically
                    service.closeOpenBidService();

                    Application application = new Application();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
