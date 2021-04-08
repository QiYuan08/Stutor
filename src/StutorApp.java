import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class StutorApp {
    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel rootPanel, loginPage, dashboardPage;
    private JLabel activityTitle, usernameField, passwordField;
    private JTextField usernameInput;
    private JButton loginUserButton;
    private JPanel registrationPage;
    private JPanel profilePage;
    private JButton tutorial1Button;
    private JButton tutorial2Button;
    private JList competenciesList;
    private JList qualificationsList;
    private JList initiatedBidsList;
    private JTextField regUsernameInput;
    private JButton registerUserButton;
    private JLabel username;
    private JLabel name;
    private JLabel accountType;
    private JPasswordField regPasswordInput;
    private JPasswordField passwordInput;
    private JButton tutorial3Button;
    private JButton tutorial4Button;
    private JButton tutorial5Button;
    private JButton editProfileButton;
    private JButton registerPageButton;
    private JButton DashboardPageButton;

    public StutorApp() {
        frame = new JFrame("StuTor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
//        rootPanel = new JPanel();
//        loginPage = new JPanel();
//        dashboardPage = new JPanel();
//
//        loginUserButton = new JButton("log in");
//        loginPage.add(loginUserButton);
//        cardLayout = new CardLayout();
//        rootPanel.setLayout(cardLayout);
//        rootPanel.add(loginPage, "Login Page");
//        rootPanel.add(dashboardPage, "Dashboard");
//        dashboardPage.setName("dashboardPage");

        loginUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {login();}
        });

        editProfileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) rootPanel.getLayout();
                cl.next(rootPanel);
            }
        });

        registerPageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) rootPanel.getLayout();
                cl.next(rootPanel);
            }
        });

        DashboardPageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) rootPanel.getLayout();
                cl.previous(rootPanel);
            }
        });

        registerUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) rootPanel.getLayout();
                cl.next(rootPanel);
            }
        });


        frame.add(rootPanel);
        frame.setVisible(true);
    }

    private void login() {
//        cl.layoutContainer(rootPanel);
//        System.out.println(dashboardPage.getName());
//        cl.show(rootPanel, "dashboardPage");
        CardLayout cl = (CardLayout) rootPanel.getLayout();
        cl.next(rootPanel);
        cl.next(rootPanel);
    }


    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    StutorApp stutorApp = new StutorApp();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
