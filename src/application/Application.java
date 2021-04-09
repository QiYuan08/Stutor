package application;

import javax.swing.*;
import java.awt.*;

public class Application extends JFrame{
    private JPanel rootPanel;
    private CardLayout cardLayout;
    private LoginPage loginPage;

    Application() {
        super("StuTor");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500, 400);
        rootPanel = new JPanel();
        cardLayout = new CardLayout();
        rootPanel.setLayout(cardLayout);
        DashboardPage dashboardPage = new DashboardPage();

        rootPanel.add(new LoginPage(), "LoginPage");
        rootPanel.add(new RegistrationPage(), "RegistrationPage");
        rootPanel.add(dashboardPage, "DashboardPage");
        rootPanel.add(new ProfilePage(), "ProfilePage");
        System.out.println(dashboardPage.getName());

        this.add(rootPanel);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        Application application = new Application();
    }
}
