package views;

import controllers.*;
import controllers.RenewContractController;
import listeners.SendMessageListener;
import services.ExpireBidService;
import listeners.ExpireContractListener;
import services.UpdateBidService;
import services.ViewManagerService;
import views.student_bids.*;
import views.tutor_responds.*;
import views.main_pages.*;
import links.*;

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
        FindTutorResponse findTutorResponse = new FindTutorResponse();
        MessagesPage messagesPage = new MessagesPage();
        SeeTutorResponse seeTutorResponse = new SeeTutorResponse();
        BidResponse bidResponse = new BidResponse();
        ViewContracts viewContracts = new ViewContracts();
        ViewContractDetails viewContractDetails = new ViewContractDetails();
        MonitoredBids monitoredBids = new MonitoredBids();

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
        rootPanel.add(bidResponse, ViewManagerService.BID_RESPONSE);
        rootPanel.add(findTutorResponse, ViewManagerService.FIND_TUTOR_RESPONSE);
        rootPanel.add(messagesPage, ViewManagerService.MESSAGES_PAGE);
        rootPanel.add(seeTutorResponse, ViewManagerService.SEE_TUTOR_RESPONSE);
        rootPanel.add(viewContracts, ViewManagerService.VIEW_CONTRACTS);
        rootPanel.add(monitoredBids, ViewManagerService.MONITORED_BIDS);
        rootPanel.add(viewContractDetails, ViewManagerService.VIEW_CONTRACT_DETAILS);



        // LISTENERS - process button presses and just go to the next page

        ExpireContractListener expireContractListener = new ExpireContractListener(viewContractDetails);

        // listener for submit message button to send a message
        SendMessageListener sendMessageListener = new SendMessageListener(messagesPage); // TODO: test with closed bids

        // LINKS - process buttons and updates the next page before it loads it

        // bid to update data between findbidpage, message and response page
        BidResponseDirector bidResponseDirector = new BidResponseDirector(findBidDetails, bidResponse, messagesPage);

        // link to redirect student to reply to a tutor message
        ControllerLink seeMessageLink = new ControllerLink(seeTutorResponse, ViewManagerService.MESSAGES_PAGE);
        seeMessageLink.subscribe(messagesPage);

        // links the buttons for each tutor that responded in findBidDetails page to findTutorResponse page
        ControllerLink findTutorResponseLink = new ControllerLink(findBidDetails, ViewManagerService.FIND_TUTOR_RESPONSE);
        findTutorResponseLink.subscribe(findTutorResponse);

        // links seeBidDetails page to seeTutorResponse page
        ControllerLink seeTutorResponseLink = new ControllerLink(seeBidDetails, ViewManagerService.SEE_TUTOR_RESPONSE);
        seeTutorResponseLink.subscribe(seeTutorResponse);

        ControllerLink viewContractDetailController = new ControllerLink(viewContracts, ViewManagerService.VIEW_CONTRACT_DETAILS);
        viewContractDetailController.subscribe(viewContractDetails);

        // link seeAllBids and seeBidDetails page
        ControllerLink seeBidDetailsController = new ControllerLink(seeAllBids, ViewManagerService.SEE_BID_DETAILS);
        seeBidDetailsController.subscribe(seeBidDetails);
        seeBidDetailsController.subscribe(seeTutorResponseLink);

        // link findbidpage and findbiddetail page
        ControllerLink findBidDetailsController = new ControllerLink(findAllBids, ViewManagerService.FIND_BID_DETAILS);
        findBidDetailsController.subscribe(findBidDetails);
        findBidDetailsController.subscribe(findTutorResponseLink);

        // links from the monitored bids to the bid details
        ControllerLink monitorBidsController = new ControllerLink(monitoredBids, ViewManagerService.FIND_BID_DETAILS);
        monitorBidsController.subscribe(findBidDetails);
        monitorBidsController.subscribe(findTutorResponseLink);


        // SERVICES

        // initialises the service that expires bids after a certain time interval
        ExpireBidService expireBidService = new ExpireBidService();
        //sets the interval before deactivating an open bid and closed bid automatically in minutes and days
        expireBidService.setDuration(720, 7); // only activate when user has logged in

        UpdateBidService updateBidService = new UpdateBidService();
        updateBidService.subscribe(monitoredBids); // the page need to be updated first with the bid buttons before adding the listeners
        updateBidService.subscribe(monitorBidsController);
        updateBidService.subscribe(viewContracts);
        updateBidService.subscribe(viewContractDetailController);
        updateBidService.subscribe(findAllBids);
        updateBidService.subscribe(findBidDetailsController);
        updateBidService.subscribe(seeAllBids);
        updateBidService.subscribe(seeBidDetailsController);

        // configures the service that allows the switching of pages within the card layout
        ViewManagerService.setRootPanel(rootPanel);


        // CONTROLLERS - notifies multiple subscribers of new information and proceeds to the next page

        // links CreateBid to DashboardPage for user to create a bid and limit the number of contracts/bids made
        BidCreateController bidCreateController = new BidCreateController(createBid);
        bidCreateController.subscribe(dashboardPage);
        bidCreateController.subscribe(seeAllBids);

        // controller to update dashboardPage and viewContracts when tutor/student signed a renewed contract
        RenewContractController renewContractController = new RenewContractController();
        renewContractController.subscribe(dashboardPage);
        renewContractController.subscribe(viewContracts);

        // controller for for when a bid closes (and a contract is created) so that views wont display old inactive bids
        BidClosingController bidClosingController = new BidClosingController();
        findBidDetails.addActionListener(bidClosingController);
        seeTutorResponse.addActionListener(bidClosingController);
        expireBidService.addActionListener(bidClosingController);
        bidClosingController.subscribe(findAllBids);
        bidClosingController.subscribe(seeAllBids);
        bidClosingController.subscribe(dashboardPage);
        bidClosingController.subscribe(viewContracts);

        // passing the userId to view classes and services that require it
        LoginController loginController = new LoginController(loginPage);
        loginController.subscribe(dashboardPage);
        loginController.subscribe(profilePage);
        loginController.subscribe(findAllBids);
        loginController.subscribe(seeAllBids);
        loginController.subscribe(createBid); // uses userId to check constraints before creating bid
        loginController.subscribe(bidClosingController); // uses the userId to update views when a bid closes
        loginController.subscribe(expireContractListener);
        loginController.subscribe(updateBidService);
        loginController.subscribe(expireBidService);
        loginController.subscribe(renewContractController);

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
