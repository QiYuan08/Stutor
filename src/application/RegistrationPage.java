package application;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.http.HttpResponse;

public class RegistrationPage extends JPanel {

    private JLabel activityTitle, passwordField, usernameField, gnameField, fnameField, accType;
    private JTextField usernameInput, gnameInput, fnameInput;
    private JPasswordField passwordInput;
    private JButton registerUserButton;
    private JCheckBox studentCheckBox, tutorCheckBox;
    private HttpResponse<String> response;

    RegistrationPage() {
        this.setBorder(new EmptyBorder(15, 15, 15, 15));
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1;
        c.weighty = 1;
        c.insets = new Insets(5, 5, 0, 5);

        activityTitle = new JLabel("User Registration");
        activityTitle.setHorizontalAlignment(JLabel.CENTER);
        activityTitle.setVerticalAlignment(JLabel.TOP);
        activityTitle.setFont(new Font("Bahnschrift", Font.BOLD, 20));
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        c.fill = GridBagConstraints.HORIZONTAL;
        this.add(activityTitle, c);


        usernameField = new JLabel("Username: ");
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        this.add(usernameField, c);
        usernameInput = new JTextField();
        c.gridx = 1;
        c.gridwidth = 2;
        this.add(usernameInput, c);

        passwordField = new JLabel("Password: ");
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        this.add(passwordField, c);
        passwordInput = new JPasswordField();
        c.gridx = 1;
        c.gridwidth = 2;
        this.add(passwordInput, c);

        gnameField = new JLabel("Given Name: ");
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 1;
        this.add(gnameField, c);
        gnameInput = new JTextField();
        c.gridx = 1;
        c.gridwidth = 2;
        this.add(gnameInput, c);

        fnameField = new JLabel("First Name: ");
        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 1;
        this.add(fnameField, c);
        fnameInput = new JTextField();
        c.gridx = 1;
        c.gridwidth = 2;
        this.add(fnameInput, c);

        accType = new JLabel("Account Type: ");
        c.gridx = 0;
        c.gridy = 5;
        c.gridwidth = 1;
        this.add(accType, c);
        studentCheckBox = new JCheckBox("Student");
        c.gridx = 1;
        this.add(studentCheckBox, c);
        tutorCheckBox = new JCheckBox("Tutor");
        c.gridx = 2;
        this.add(tutorCheckBox, c);

        registerUserButton = new JButton("Register");
        c.gridx = 0;
        c.gridy = 6;
        c.gridwidth = 3;
        this.add(registerUserButton, c);

    }
}
