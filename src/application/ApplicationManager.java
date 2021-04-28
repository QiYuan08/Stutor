package application;

import javax.swing.*;
import java.awt.*;

public class ApplicationManager {

    public static final String LOGIN_PAGE = "LoginPage";
    public static final String REGISTRATION_PAGE = "RegistrationPage";
    public static final String DASHBOARD_PAGE = "DashboardPage";
    public static final String PROFILE_PAGE = "ProfilePage";
    public static final String OPEN_BID_PAGE = "OpenBidPage";
    public static final String FIND_BID = "FindBidPage";
    public static final String VIEW_BID = "ViewBidPage";
    public static final String USER_BIDS = "UserBidsPage";

    private static JPanel rootPanel;

    public static void setRootPanel(JPanel rootPanel) {
        ApplicationManager.rootPanel = rootPanel;

    }

    public static void loadPage(String pageName) {
        CardLayout cardLayout = (CardLayout) ApplicationManager.rootPanel.getLayout();
        cardLayout.show(ApplicationManager.rootPanel, pageName);
    }

}
