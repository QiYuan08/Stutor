package services;

import javax.swing.*;
import java.awt.*;

public class ViewManagerService {

    public static final String LOGIN_PAGE = "LoginPage";
    public static final String REGISTRATION_PAGE = "RegistrationPage";
    public static final String DASHBOARD_PAGE = "DashboardPage";
    public static final String PROFILE_PAGE = "ProfilePage";
    public static final String MESSAGES_PAGE = "MessagesPage";

    public static final String CREATE_BID = "CreateBid";
    public static final String SEE_ALL_BIDS = "SeeAllBids";
    public static final String SEE_BID_DETAILS = "SeeBidDetails";
    public static final String SEE_TUTOR_RESPONSE = "SeeTutorResponse";

    public static final String FIND_ALL_BIDS = "FindAllBids";
    public static final String FIND_BID_DETAILS = "FindBidDetails";
    public static final String FIND_TUTOR_RESPONSE = "FindTutorResponse";
    public static final String OPEN_BID_RESPONSE = "OpenBidResponse";
    public static final String CLOSED_BID_RESPONSE = "ClosedBidResponse";

    private static JPanel rootPanel;

    public static void setRootPanel(JPanel rootPanel) {
        ViewManagerService.rootPanel = rootPanel;
    }

    public static void loadPage(String pageName) {
        CardLayout cardLayout = (CardLayout) ViewManagerService.rootPanel.getLayout();
        cardLayout.show(ViewManagerService.rootPanel, pageName);
    }

}
