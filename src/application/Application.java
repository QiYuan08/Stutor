package application;

import javax.swing.*;
import java.awt.*;

public class Application extends JFrame{
    private static JPanel rootPanel;
    private CardLayout cardLayout;
    private LoginPage loginPage;
    public static final String LOGIN_PAGE = "LoginPage";
    public static final String REGISTRATION_PAGE = "RegistrationPage";
    public static final String DASHBOARD_PAGE = "DashboardPage";
    public static final String PROFILE_PAGE = "ProfilePage";

    Application() {
        super("StuTor");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500, 400);
        rootPanel = new JPanel();
        cardLayout = new CardLayout();
        rootPanel.setLayout(cardLayout);

        rootPanel.add(new LoginPage(), "LoginPage");
        rootPanel.add(new RegistrationPage(), "RegistrationPage");
        rootPanel.add(new DashboardPage(), "DashboardPage");
        rootPanel.add(new ProfilePage(), "ProfilePage");

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
        CardLayout cl = (CardLayout) rootPanel.getLayout();
        cl.show(rootPanel, pageName);
    }
}
