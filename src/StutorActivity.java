import project.Dashboard;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StutorActivity {
    public StutorActivity() {
        loginUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                activityTitle.setText("Login Successful!");
                Dashboard dashboard = new Dashboard();
                dashboard.setVisible(true);
//                StutorApp stutorApp = new StutorApp();
//                stutorApp.setVisi
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("StutorActivity");
        frame.setContentPane(new StutorActivity().rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private JPanel rootPanel;
    private JLabel activityTitle;
    private JButton loginUser;
    private JTextField usernameInput;
    private JLabel usernameField;
    private JTextField passwordInput;
    private JLabel passwordField;
}
