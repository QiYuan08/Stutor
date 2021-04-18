package application;

import application.bid.AllBid;
import application.bid.CreateOpenBid;

import javax.swing.*;
import java.awt.*;

public class Application extends JFrame{
    public static final String LOGIN_PAGE = "LoginPage";
    public static final String REGISTRATION_PAGE = "RegistrationPage";
    public static final String DASHBOARD_PAGE = "DashboardPage";
    public static final String PROFILE_PAGE = "ProfilePage";
    public static final String OPEN_BID = "OpenBid";
    private static JPanel rootPanel;
    private static CardLayout cardLayout;
//    private static User user;

    private Application() {
        super("StuTor");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500, 400);
        rootPanel = new JPanel();
        cardLayout = new CardLayout();
        rootPanel.setLayout(cardLayout);

//        rootPanel.add(new LoginPage(), LOGIN_PAGE);
//        rootPanel.add(new RegistrationPage(), REGISTRATION_PAGE);
//        rootPanel.add(new DashboardPage(), DASHBOARD_PAGE);
//        rootPanel.add(new ProfilePage(), PROFILE_PAGE);
        rootPanel.add(new CreateOpenBid(), OPEN_BID);

//          JScrollPane mainPane = new JScrollPane(new AllBid());
//          rootPanel.add(mainPane, "AllBid");


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

    public static void loadPage(String pageName) {
        cardLayout.show(rootPanel, pageName);
    }
}
