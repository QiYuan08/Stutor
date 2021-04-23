package application;

import api.ApiRequest;
import controller.ObserverOutputInterface;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.http.HttpResponse;

public class DashboardPage extends JPanel implements ObserverOutputInterface {

    private JLabel activityTitle, tutorialsTaken, tutorialsTaught;
    private JButton viewProfileButton, seeBidsButton, openBidButton, findBidsButton;
    private JScrollPane tutorialsTakenList, tutorialsTaughtList;
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
        c.gridwidth = 3;
        c.fill = GridBagConstraints.BOTH;
        this.add(activityTitle, c);

        tutorialsTaken = new JLabel("Tutorials you are taking: ");
        c.gridy = 1;
        c.gridwidth = 3;
        this.add(tutorialsTaken, c);

        tutorialsTaught = new JLabel("Tutorials you are teaching: ");
        c.gridy = 8;
        c.gridwidth = 3;
        this.add(tutorialsTaught, c);

        seeBidsButton = new JButton("See Your Bids");
        c.gridx = 1;
        c.gridy = 6;
        c.gridwidth = 1;
        this.add(seeBidsButton, c);

        openBidButton = new JButton("Open New Bid");
        c.gridx = 2;
        this.add(openBidButton, c);

        findBidsButton = new JButton("Find Bids");
        c.gridy = 13;
        this.add(findBidsButton, c);

        tutorialsTakenList = new JScrollPane();
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 3;
        c.gridheight = 4;
        this.add(tutorialsTakenList, c);

        tutorialsTaughtList = new JScrollPane();
        c.gridy = 9;
        c.gridheight = 4;
        this.add(tutorialsTaughtList, c);

        viewProfileButton = new JButton("View Profile");
        c.gridx = 1;
        c.gridy = 15;
        this.add(viewProfileButton, c);

        openBidButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {Application.loadPage(Application.OPEN_BID_PAGE);}
        });

        viewProfileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Application.loadPage(Application.PROFILE_PAGE);
            }
        });
    }

    @Override
    public void update(String data) {
        HttpResponse<String> response = ApiRequest.get("/contract");
        if (response.statusCode() == 200) {
            JSONArray contracts = new JSONArray(response.body());
            JPanel tutorialsTakenPanel = new JPanel();
            tutorialsTakenPanel.setLayout(new BoxLayout(tutorialsTakenPanel, BoxLayout.Y_AXIS));
            JPanel tutorialsTaughtPanel = new JPanel();
            tutorialsTaughtPanel.setLayout(new BoxLayout(tutorialsTaughtPanel, BoxLayout.Y_AXIS));
            for (int i = 0; i < contracts.length(); i++) {
                JSONObject contract = (JSONObject) contracts.get(i);
                if (contract.optJSONObject("firstParty").get("id").equals(ProfilePage.userId)) {
                    JPanel componentPanel = new JPanel();
                    componentPanel.add(new JLabel(contract.optJSONObject("subject").optString("name") +
                            " - " + contract.optJSONObject("subject").optString("description")));
                    tutorialsTakenPanel.add(componentPanel);
                } else if (contract.optJSONObject("secondParty").get("id").equals(ProfilePage.userId)) {
                    JPanel componentPanel = new JPanel();
                    componentPanel.add(new JLabel(contract.optJSONObject("subject").optString("name") +
                            " - " + contract.optJSONObject("subject").optString("description")));
                    tutorialsTaughtPanel.add(componentPanel);
                }
            }
            this.tutorialsTakenList.setViewportView(tutorialsTakenPanel);
            this.tutorialsTaughtList.setViewportView(tutorialsTaughtPanel);
        }
    }
}
