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

        // create a expireBidService class
        ExpireBidService expireBidService = new ExpireBidService();
        expireBidService.setDuration(30); //set the interval before closing open bid automatically in (minutes)
        expireBidService.expireOpenBidService();
        expireBidService.expireCloseBidService();

        /*
         Split up different process into different process
         to remove unnecessary controller observer call
         */
        // listener for closing bid
        // TODO: update findAllBids after closing
        Controller bidClosingController = new Controller();
        BidClosingListener bidClosingListener = new BidClosingListener(bidClosingController);
        findBidDetails.addActionListener(bidClosingListener);
        seeTutorResponse.addActionListener(bidClosingListener);
        expireBidService.addActionListener(bidClosingListener);
        bidClosingController.subscribe(findAllBids);
        bidClosingController.subscribe(seeAllBids);
        bidClosingController.subscribe(dashboardPage);

        // passing userId to view classes and services that require it
        Controller loginController = new Controller();
        LoginListener loginListener = new LoginListener(loginPage, loginController);
        loginController.subscribe(profilePage);
        loginController.subscribe(dashboardPage);
        loginController.subscribe(createBid);
        loginController.subscribe(seeAllBids);
        loginController.subscribe(findAllBids);
        loginController.subscribe(createBid);
        loginController.subscribe(expireBidService);
        loginController.subscribe(bidClosingListener); // get the userId to update other bidding page


//        // linking findbiddetails page to findtutorbiddetail page
        FindBidderDetailLink findBidderDetailLink = new FindBidderDetailLink(findBidDetails, findTutorResponse);

//        // passing bidId between FindAllBids and FindBidDetails page and add actionListener to view detail button
        FindBidListener findBidListener = new FindBidListener(findBidDetails);

        // link to redirect student to reply to a tutor message
        SeeMessageLink seeMessageLink = new SeeMessageLink(seeTutorResponse, messagesPage);

        // linking seebiddetails page to seetutorbiddetail page
        SeeBidderDetailLink seeBidderDetailLink = new SeeBidderDetailLink(seeBidDetails, seeTutorResponse);

        // passing bidId between SeeBidPage and SeeBidsDetail page and add actionListener to view detail button
        SeeBidListener seeBidListener = new SeeBidListener(seeBidDetails);

        // link findbidpage and findbiddetail page
        FindBidDetailLink findBidDetailLink = new FindBidDetailLink(findAllBids, findBidDetails, findBidderDetailLink);

        // link seebidpage and seebiddetail page
        SeeBidDetailLink seeBidDetailLink = new SeeBidDetailLink(seeAllBids, seeBidDetails, seeBidderDetailLink);
        // link findbiddetailpage and tutorbiddetail page

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

        // bid to update data between findbidpage, message and response page
        ResponseBidLink responseOpenBidListener = new ResponseBidLink(findBidDetails, openBidResponse, closedBidResponse, messagesPage);

        // listener for submit message button
        MessageListener messageListener = new MessageListener(messagesPage);

        // controller for user to open bid
        // TODO: refactor createbid listener so that constructor nonid controller if no other class subscribing it
        // createBidController = new Controller();
        BidCreateListener createBidListener = new BidCreateListener(createBid);

        ViewManagerService.setRootPanel(rootPanel);

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
