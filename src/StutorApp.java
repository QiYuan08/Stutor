import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StutorApp {
    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel rootPanel, loginPage, dashboardPage;
    private JLabel activityTitle, usernameField, passwordField;
    private JTextField usernameInput, passwordInput;
    private JButton loginUser;

    public StutorApp() {
        frame = new JFrame("StuTor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
//        rootPanel = new JPanel();
//        loginPage = new JPanel();
//        dashboardPage = new JPanel();
//
//        loginUser = new JButton("log in");
        loginUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {login();}
        });
//        loginPage.add(loginUser);

//        cardLayout = new CardLayout();
//        rootPanel.setLayout(cardLayout);
//        rootPanel.add(loginPage, "Login Page");
//        rootPanel.add(dashboardPage, "Dashboard");

        frame.add(rootPanel);
        frame.setVisible(true);
    }

    private void login() {
        CardLayout cl = (CardLayout) rootPanel.getLayout();
        cl.next(rootPanel);
    }


    public static void main(String[] args) {
//        JFrame frame = new JFrame("StutorApp");
//        frame.setContentPane(new StutorApp().rootPanel);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.pack();
//        frame.setVisible(true);
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
