package application;

import event_manager.EventSubscriber;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.http.HttpResponse;

public class DashboardPage extends JPanel implements EventSubscriber {

    private JLabel activityTitle, tutorials;
    private JButton tutorial1Button, tutorial2Button, tutorial3Button, tutorial4Button, tutorial5Button, viewProfileButton;
    private HttpResponse<String> response;

    DashboardPage() {
        this.setBorder(new EmptyBorder(15, 15, 15, 15));
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1;
        c.weighty = 1;
        c.insets = new Insets(5, 5, 0, 5);

        activityTitle = new JLabel("StuTor");
        activityTitle.setHorizontalAlignment(JLabel.CENTER);
        activityTitle.setVerticalAlignment(JLabel.TOP);
        activityTitle.setFont(new Font("Bahnschrift", Font.BOLD, 20));
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        this.add(activityTitle, c);

        tutorials = new JLabel("Tutorials: ");
        c.gridy = 1;
        c.gridwidth = 1;
        this.add(tutorials, c);

        tutorial1Button = new JButton("Add New Tutorial");
        c.gridx = 0;
        c.gridy = 2;
        this.add(tutorial1Button, c);

        tutorial2Button = new JButton("Add New Tutorial");
        c.gridx = 1;
        c.gridy = 2;
        this.add(tutorial2Button, c);

        tutorial3Button = new JButton("Add New Tutorial");
        c.gridx = 0;
        c.gridy = 3;
        this.add(tutorial3Button, c);

        tutorial4Button = new JButton("Add New Tutorial");
        c.gridx = 1;
        c.gridy = 3;
        this.add(tutorial4Button, c);

        tutorial5Button = new JButton("Add New Tutorial");
        c.gridx = 0;
        c.gridy = 4;
        this.add(tutorial5Button, c);

        viewProfileButton = new JButton("View Profile");
        c.gridx = 1;
        c.gridy = 5;
        this.add(viewProfileButton, c);

        viewProfileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Application.loadPage(Application.PROFILE_PAGE);
            }
        });
    }

    @Override
    public void update(String data) {

    }
}
