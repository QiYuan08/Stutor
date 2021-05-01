package views;

import services.ViewManagerService;
import services.ExpireBidService;
import controller.Controller;
import views.student_bids.CreateBid;
import views.student_bids.SeeAllBids;
import views.tutor_responds.*;
import views.main_pages.*;
import views.student_bids.SeeBidDetails;
import views.student_bids.SeeTutorResponse;
import controller.ObserverOutputInterface;
import links.*;
import listeners.*;

import javax.swing.*;
import java.awt.*;

public class Application extends JFrame{
    private JPanel rootPanel;
    private CardLayout cardLayout;

    private Application() {
        super("StuTor");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500, 400);
        rootPanel = new JPanel();
        cardLayout = new CardLayout();
        rootPanel.setLayout(cardLayout);

        // initialising all the view components
        LoginPage loginPage = new LoginPage();
        RegistrationPage registrationPage = new RegistrationPage();
        DashboardPage dashboardPage = new DashboardPage();
        ProfilePage profilePage = new ProfilePage();
        CreateBid createBid = new CreateBid();
        FindAllBids findAllBids = new FindAllBids();
        FindBidDetails findBidDetails = new FindBidDetails();
        SeeAllBids seeAllBids = new SeeAllBids();
        SeeBidDetails seeBidDetails = new SeeBidDetails();
        OpenBidResponse openBidResponse = new OpenBidResponse();
        ClosedBidResponse closedBidResponse = new ClosedBidResponse();
        FindTutorResponse findTutorResponse = new FindTutorResponse();
        MessagesPage messagesPage = new MessagesPage();
        SeeTutorResponse seeTutorResponse = new SeeTutorResponse();

        // adding all the views into the rootPanel so that they can be accessed via the cardLayout
        rootPanel.add(loginPage, ViewManagerService.LOGIN_PAGE);
        rootPanel.add(registrationPage, ViewManagerService.REGISTRATION_PAGE);
        rootPanel.add(dashboardPage, ViewManagerService.DASHBOARD_PAGE);
        rootPanel.add(profilePage, ViewManagerService.PROFILE_PAGE);
        rootPanel.add(createBid, ViewManagerService.CREATE_BID);
        rootPanel.add(findAllBids, ViewManagerService.FIND_ALL_BIDS);
        rootPanel.add(findBidDetails, ViewManagerService.FIND_BID_DETAILS);
        rootPanel.add(seeAllBids, ViewManagerService.SEE_ALL_BIDS);
        rootPanel.add(seeBidDetails, ViewManagerService.SEE_BID_DETAILS);
        rootPanel.add(openBidResponse, ViewManagerService.OPEN_BID_RESPONSE);
        rootPanel.add(closedBidResponse, ViewManagerService.CLOSED_BID_RESPONSE);
        rootPanel.add(findTutorResponse, ViewManagerService.FIND_TUTOR_RESPONSE);
        rootPanel.add(messagesPage, ViewManagerService.MESSAGES_PAGE);
        rootPanel.add(seeTutorResponse, ViewManagerService.SEE_TUTOR_RESPONSE);


        // SERVICES

        ExpireBidService expireBidService = new ExpireBidService();
        //sets the interval before deactivating an open bid and closed bid automatically in minutes and days
        expireBidService.setDuration(30, 7);
        expireBidService.expireOpenBidService();
        expireBidService.expireCloseBidService();

        ViewManagerService.setRootPanel(rootPanel);


        // LISTENERS

        // passing bidId between SeeBidPage and SeeBidsDetail page and add actionListener to view detail button
        SeeBidListener seeBidListener = new SeeBidListener(seeBidDetails);

        // passing bidId between FindAllBids and FindBidDetails page and add actionListener to view detail button
        FindBidListener findBidListener = new FindBidListener(findBidDetails);

        // listener for submit message button
        MessageListener messageListener = new MessageListener(messagesPage);

        // controller for user to open bid
        BidCreateListener createBidListener = new BidCreateListener(createBid);


        // LINKS

        // links seeBidDetails page to seeTutorResponse page
        SeeBidderDetailLink seeBidderDetailLink = new SeeBidderDetailLink(seeBidDetails, seeTutorResponse);

        // link seeAllBids and seeBidDetails page
        SeeBidDetailLink seeBidDetailLink = new SeeBidDetailLink(seeAllBids, seeBidDetails, seeBidderDetailLink);

        // link to redirect student to reply to a tutor message
        SeeMessageLink seeMessageLink = new SeeMessageLink(seeTutorResponse, messagesPage);

        // links the buttons for each tutor that responded in findBidDetails page to findTutorResponse page and update it with the correct data
        FindBidderDetailLink findBidderDetailLink = new FindBidderDetailLink(findBidDetails, findTutorResponse);

        // link findbidpage and findbiddetail page
        FindBidDetailLink findBidDetailLink = new FindBidDetailLink(findAllBids, findBidDetails, findBidderDetailLink);

        // bid to update data between findbidpage, message and response page
        ResponseBidLink responseBidLink = new ResponseBidLink(findBidDetails, openBidResponse, closedBidResponse, messagesPage);


        // CONTROLLERS

        // listener for for when a bid closes (and a contract is created) so that views wont display old inactive bids
        Controller bidClosingController = new Controller();
        BidClosingListener bidClosingListener = new BidClosingListener(bidClosingController);
        findBidDetails.addActionListener(bidClosingListener);
        seeTutorResponse.addActionListener(bidClosingListener);
        expireBidService.addActionListener(bidClosingListener);
        bidClosingController.subscribe(findAllBids);
        bidClosingController.subscribe(seeAllBids);
        bidClosingController.subscribe(dashboardPage);

        // dashboardController needed for findbid and seebid pages to add event listener for all of its button
        // this controller is called when user click on findBid Button and seeBid button in dashboard
        // from dashboard to see bid or find bid
        Controller dashboardController = new Controller();
        BidUpdateListener bidUpdateListener = new BidUpdateListener(dashboardPage, dashboardController); // userId are updated from here
        dashboardController.subscribe(findAllBids);
        dashboardController.subscribe(findBidListener);
        dashboardController.subscribe(findBidDetailLink);
        dashboardController.subscribe(seeAllBids);
        dashboardController.subscribe(seeBidDetailLink);

        // passing the userId to view classes and services that require it
        Controller loginController = new Controller();
        LoginListener loginListener = new LoginListener(loginPage, loginController);
        loginController.subscribe(profilePage);
        loginController.subscribe(dashboardPage);
        loginController.subscribe(createBid);
        loginController.subscribe(seeAllBids);
        loginController.subscribe(findAllBids);
        loginController.subscribe(createBid);
        loginController.subscribe(bidClosingListener); // uses the userId to update views when a bid closes
//        loginController.subscribe(bidUpdateListener);

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
}
