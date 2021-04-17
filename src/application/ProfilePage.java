package application;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.http.HttpResponse;

public class ProfilePage extends JPanel {

    private JLabel activityTitle, usernameField, nameField, accTypeField, competenciesField, qualificationsField, initBidsField;
    private JLabel username, name, accType;
    private JList competenciesList, qualificationsList, initBidsList;
    private JButton dashboardPageButton;
    private HttpResponse<String> response;

    ProfilePage() {
        this.setBorder(new EmptyBorder(15, 15, 15, 15));
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1;
        c.weighty = 1;
        c.insets = new Insets(5, 5, 0, 5);

        activityTitle = new JLabel("Profile");
        activityTitle.setHorizontalAlignment(JLabel.CENTER);
        activityTitle.setVerticalAlignment(JLabel.TOP);
        activityTitle.setFont(new Font("Bahnschrift", Font.BOLD, 20));
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        c.fill = GridBagConstraints.HORIZONTAL;
        this.add(activityTitle, c);

        usernameField = new JLabel("Username: ");
        c.gridy = 1;
        c.gridwidth = 1;
        this.add(usernameField, c);

        nameField = new JLabel("Name: ");
        c.gridy = 2;
        c.gridwidth = 1;
        this.add(nameField, c);

        accTypeField = new JLabel("Account Type: ");
        c.gridy = 3;
        c.gridwidth = 1;
        this.add(accTypeField, c);

        competenciesField = new JLabel("Competencies: ");
        c.gridy = 4;
        c.gridwidth = 1;
        this.add(competenciesField, c);

        qualificationsField = new JLabel("Qualifications: ");
        c.gridy = 5;
        c.gridwidth = 1;
        this.add(qualificationsField, c);

        initBidsField = new JLabel("Initiated Bids: ");
        c.gridy = 6;
        c.gridwidth = 1;
        this.add(initBidsField, c);

        username = new JLabel("");
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 2;
        this.add(username, c);

        name = new JLabel("");
        c.gridy = 2;
        c.gridwidth = 2;
        this.add(name, c);

        accType = new JLabel("");
        c.gridy = 3;
        c.gridwidth = 2;
        this.add(accType, c);

        competenciesList = new JList();
        c.gridy = 4;
        c.gridwidth = 2;
        this.add(competenciesList, c);

        qualificationsList = new JList();
        c.gridy = 5;
        c.gridwidth = 2;
        this.add(qualificationsList, c);

        initBidsList = new JList();
        c.gridy = 6;
        c.gridwidth = 2;
        this.add(initBidsList, c);

        dashboardPageButton = new JButton("Back to Dashboard");
        c.gridx = 2;
        c.gridy = 7;
        c.gridwidth = 1;
        this.add(dashboardPageButton, c);

        dashboardPageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Application.loadPage(Application.DASHBOARD_PAGE);
            }
        });
    }
}
