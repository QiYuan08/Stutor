package views;

import services.ViewManagerService;
import services.ExpireBidService;
import controller.Controller;
import views.student_bids.SeeAllBids;
import views.tutor_responds.*;
import views.main_pages.*;
import views.student_bids.CreateBidPage;
import views.student_bids.SeeBidDetails;
import views.student_bids.SeeTutorResponse;
import controller.ObserverOutputInterface;
import links.*;
import listeners.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Application extends JFrame{
    private static JPanel rootPanel;
    private static CardLayout cardLayout;
    Controller loginController, contractController, findBidController,seeBidController, responseOpenBidController, dashboardController, bidClosingController;
    ActionListener contractListener, loginListener, messageListener, responseOpenBidListener, findBidListener, seeBidListener, createBidListener, bidClosingListener, dashboardListener;
    FindBidDetailLink findBidDetailLink;
    SeeBidDetailLink seeBidDetailLink;
    SeeBidderDetailLink seeBidderDetailLink;
    FindBidderDetailLink findBidderDetailLink;
    SeeMessageLink seeMessageLink;
//    private Controller loginController, bidUpdateController, bidClosingController;
//    private ActionListener loginListener, responseBidLink, createBidListener, bidClosingListener, bidUpdateListener;
//    private FindBidDetailLink findBidDetailLink;
//    private SeeBidDetailLink seeBidDetailLink;

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
        FindAllBids findAllBids = new FindAllBids();
        FindBidDetails findBidDetails = new FindBidDetails();
        SeeAllBids seeAllBids = new SeeAllBids();
        SeeBidDetails seeBidDetails = new SeeBidDetails();
        ResponseOpenBid responseOpenBid = new ResponseOpenBid();
        ClosedBidResponse closedBidResponse = new ClosedBidResponse();
        FindTutorResponse findTutorResponse = new FindTutorResponse();
        MessagesPage messagesPage = new MessagesPage();
        SeeTutorResponse seeTutorResponse = new SeeTutorResponse();

        rootPanel.add(loginPage, ViewManagerService.LOGIN_PAGE);
        rootPanel.add(registrationPage, ViewManagerService.REGISTRATION_PAGE);
        rootPanel.add(dashboardPage, ViewManagerService.DASHBOARD_PAGE);
        rootPanel.add(profilePage, ViewManagerService.PROFILE_PAGE);
        rootPanel.add(createBidPage, ViewManagerService.CREATE_BID_PAGE);
        rootPanel.add(findAllBids, ViewManagerService.FIND_BID);
        rootPanel.add(findBidDetails, ViewManagerService.FIND_BID_DETAIL);
        rootPanel.add(seeAllBids, ViewManagerService.SEE_BIDS_PAGE);
        rootPanel.add(seeBidDetails, ViewManagerService.SEE_BID_DETAIL);
        rootPanel.add(responseOpenBid, ViewManagerService.RESPONSE_OPEN_BID);
        rootPanel.add(closedBidResponse, ViewManagerService.RESPONSE_CLOSE_BID);
        rootPanel.add(findTutorResponse, ViewManagerService.FIND_TUTOR_BID_DETAIL);
        rootPanel.add(messagesPage, ViewManagerService.MESSAGES_PAGE);
        rootPanel.add(seeTutorResponse, ViewManagerService.SEE_TUTOR_BID_DETAIL);

        // create a expireBidService class
        ExpireBidService expireBidService = new ExpireBidService();
        expireBidService.setDuration(30); //set the interval before closing automatically
        expireBidService.expireBidService();

        /*
         Split up different process into different process
         to remove unnecessary controller observer call
         */
        // listener for closing bid
        // TODO: update findAllBids after closing
        bidClosingController = new Controller();
        bidClosingListener = new BidClosingListener(bidClosingController);
        findBidDetails.addActionListener(bidClosingListener);
        seeTutorResponse.addActionListener(bidClosingListener);
        expireBidService.addActionListener(bidClosingListener);
        bidClosingController.subscribe(findAllBids);
        bidClosingController.subscribe(seeAllBids);
        bidClosingController.subscribe(dashboardPage);

        // passing userId to view classes and services that require it
        loginController = new Controller();
        loginListener = new LoginListener(loginPage, loginController);
        loginController.subscribe(profilePage);
        loginController.subscribe(dashboardPage);
        loginController.subscribe(createBidPage);
        loginController.subscribe(seeAllBids);
        loginController.subscribe(findAllBids);
        loginController.subscribe(createBidPage);
        loginController.subscribe(expireBidService);
        loginController.subscribe((ObserverOutputInterface) bidClosingListener); // get the userId to update other bidding page

//        // linking findbiddetails page to findtutorbiddetail page
        findBidderDetailLink = new FindBidderDetailLink(findBidDetails, findTutorResponse);

//        // passing bidId between FindAllBids and FindBidDetails page and add actionListener to view detail button
        findBidListener = new FindBidListener(findBidDetails);

        // link to redirect student to reply to a tutor message
        seeMessageLink = new SeeMessageLink(seeTutorResponse, messagesPage);

        // linking seebiddetails page to seetutorbiddetail page
        seeBidderDetailLink = new SeeBidderDetailLink(seeBidDetails, seeTutorResponse);

        // passing bidId between SeeBidPage and SeeBidsDetail page and add actionListener to view detail button
        seeBidListener = new SeeBidListener(seeBidDetails);

        // link findbidpage and findbiddetail page
        findBidDetailLink = new FindBidDetailLink(findAllBids, findBidDetails, findBidderDetailLink);

        // link seebidpage and seebiddetail page
        seeBidDetailLink = new SeeBidDetailLink(seeAllBids, seeBidDetails, seeBidderDetailLink);
        // link findbiddetailpage and tutorbiddetail page

        // dashboardController needed for findbid and seebid pages to add event listener for all of its button
        // this controller is called when user click on findBid Button and seeBid button in dashboard
        // from dashboard to see bid or find bid
        Controller dashboardController = new Controller();
        BidUpdateListener bidUpdateListener = new BidUpdateListener(dashboardPage, dashboardController); // userId are updated from here
        dashboardController.subscribe(findAllBids);
        dashboardController.subscribe((ObserverOutputInterface) findBidListener);
        dashboardController.subscribe(findBidDetailLink);
        dashboardController.subscribe(seeAllBids);
        dashboardController.subscribe(seeBidDetailLink);

        // bid to update data between findbidpage, message and response page
        responseOpenBidListener = new ResponseBidLink(findBidDetails, responseOpenBid, closedBidResponse, messagesPage);

        // listener for submit message button
        messageListener = new MessageListener(messagesPage);

        // controller for user to open bid
        // TODO: refactor createbid listener so that constructor nonid controller if no other class subscribing it
        // createBidController = new Controller();
        createBidListener = new BidCreateListener(createBidPage);

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
