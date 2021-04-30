package application;

import javax.swing.*;
import java.awt.*;

public class ApplicationManager {

    public static final String LOGIN_PAGE = "LoginPage";
    public static final String REGISTRATION_PAGE = "RegistrationPage";
    public static final String DASHBOARD_PAGE = "DashboardPage";
    public static final String PROFILE_PAGE = "ProfilePage";
    public static final String CREATE_BID_PAGE = "OpenBidPage";
    public static final String FIND_BID = "FindBidPage";
    public static final String FIND_BID_DETAIL = "ViewBidPage";
    public static final String SEE_BIDS_PAGE = "UserBidsPage";
    public static final String MESSAGES_PAGE = "MessagesPage";
    public static final String RESPONSE_CLOSE_BID = "ResponseCloseBidPage";
    public static final String RESPONSE_OPEN_BID = "ResponseOpenBidPage";
    public static final String SEE_BID_DETAIL = "SeeBidDetailPage";
    public static final String FIND_TUTOR_BID_DETAIL = "TutorBidDetailPage";
    public static final String SEE_TUTOR_BID_DETAIL = "SeeTutorBidDetail";

    private static JPanel rootPanel;

    public static void setRootPanel(JPanel rootPanel) {
        ApplicationManager.rootPanel = rootPanel;

    }

    public static void loadPage(String pageName) {
        CardLayout cardLayout = (CardLayout) ApplicationManager.rootPanel.getLayout();
        cardLayout.show(ApplicationManager.rootPanel, pageName);
    }

}
