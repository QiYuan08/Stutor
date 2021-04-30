package application;

import application.bid_pages.*;
import controller.*;
import links.BidderDetailLink;
import links.FindBidDetailLink;
import links.ResponseBidLink;
import links.SeeBidDetailLink;
import listeners.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Application extends JFrame{
    private static JPanel rootPanel;
//    private static JPanel loginPage, registrationPage, dashboardPage, profilePage, openBidPage, viewBidPage, userBidsPage;
    private static CardLayout cardLayout;
    ApplicationController loginController, contractController, findBidController,seeBidController, responseOpenBidController, dashboardController, bidClosingController;
    ActionListener contractListener, loginListener, responseOpenBidListener, findBidListener, seeBidListener, createBidListener, bidClosingListener, dashboardListener;
    FindBidDetailLink findBidDetailLink;
    SeeBidDetailLink seeBidDetailLink;
    BidderDetailLink bidderDetailLink;

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
        SeeBidDetail seeBidDetail = new SeeBidDetail();
        ResponseOpenBid responseOpenBid = new ResponseOpenBid();
        ResponseCloseBid responseCloseBid = new ResponseCloseBid();
        TutorBidDetail tutorBidDetail = new TutorBidDetail();

        /***
         * Split up different process into different process
         * to remove unnecessary controller observer call
         */
        // listener for closing bid
        // TODO: update findBidPage after closing
        bidClosingController = new ApplicationController();
        bidClosingListener = new BidBuyoutListener(findBidsDetail, bidClosingController);
        bidClosingController.subscribe(findBidPage);
        bidClosingController.subscribe(seeBidsPage);

        rootPanel.add(loginPage, ApplicationManager.LOGIN_PAGE);
        rootPanel.add(registrationPage, ApplicationManager.REGISTRATION_PAGE);
        rootPanel.add(dashboardPage, ApplicationManager.DASHBOARD_PAGE);
        rootPanel.add(profilePage, ApplicationManager.PROFILE_PAGE);
        rootPanel.add(createBidPage, ApplicationManager.CREATE_BID_PAGE);
        rootPanel.add(findBidPage, ApplicationManager.FIND_BID);
        rootPanel.add(findBidsDetail, ApplicationManager.FIND_BID_DETAIL);
        rootPanel.add(seeBidsPage, ApplicationManager.SEE_BIDS_PAGE);
        rootPanel.add(seeBidDetail, ApplicationManager.SEE_BID_DETAIL);
        rootPanel.add(responseOpenBid, ApplicationManager.RESPONSE_OPEN_BID);
        rootPanel.add(responseCloseBid, ApplicationManager.RESPONSE_CLOSE_BID);
        rootPanel.add(tutorBidDetail, ApplicationManager.TUTOR_BID_DETAIL);

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

        bidderDetailLink = new BidderDetailLink(findBidsDetail, tutorBidDetail);

//        passing bidId between FindBidPage and FindBidsDetail page
        findBidController = new ApplicationController();
        findBidListener = new FindBidListener(findBidPage, findBidController);
        findBidController.subscribe(findBidsDetail);
        findBidController.subscribe(bidderDetailLink);

        // link findbidpage and findbiddetail page
        findBidDetailLink = new FindBidDetailLink(findBidPage, findBidsDetail);
        // link seebidpage and seebiddetail page
        seeBidDetailLink = new SeeBidDetailLink(seeBidsPage, findBidsDetail);
        // link findbiddetailpage and tutorbiddetail page


        // TODO: add controller for findbiddetail to bidderdetaillink
        ApplicationController bidderLinkController = new ApplicationController();
        bidderLinkController.subscribe(findBidsDetail);
        bidderLinkController.subscribe(bidderDetailLink);


        // dashboardController needed for findbid and seebid pages to add event listener for all of its button
        // this controller is called when user click on findBid Button and seeBid button in dashboard
        // from dashboard to see bid or find bid
        dashboardController = new ApplicationController();
        dashboardListener = new DashBoardListener(dashboardPage, dashboardController); // userId are updated from here
        dashboardController.subscribe(findBidPage);
        dashboardController.subscribe((ObserverOutputInterface) findBidListener);
        dashboardController.subscribe(findBidDetailLink);
        dashboardController.subscribe(seeBidsPage);
        dashboardController.subscribe(seeBidDetailLink);

        contractController = new ApplicationController();
        contractListener = new ContractListener(createBidPage, contractController);
        contractController.subscribe(dashboardPage);

        responseOpenBidListener = new ResponseBidLink(findBidsDetail, responseOpenBid, responseCloseBid);

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
                    ExpireBidService service = new ExpireBidService();
                    service.setDuration(60); //set the interval before closing automatically
                    service.expireBidService();

                    Application application = new Application();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
