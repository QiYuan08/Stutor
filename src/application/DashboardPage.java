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
    private JButton viewProfileButton, seeBidsButton, createBidButton, findBidsButton;
    private JScrollPane tutorialsTakenList, tutorialsTaughtList;
    private String userId;

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
        c.fill = GridBagConstraints.HORIZONTAL;
        this.add(activityTitle, c);

        tutorialsTaken = new JLabel("Tutorials you are taking: ");
        c.gridy = 1;
        c.gridwidth = 3;
        this.add(tutorialsTaken, c);

        tutorialsTakenList = new JScrollPane();
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 3;
        c.gridheight = 4;
        this.add(tutorialsTakenList, c);

        createBidButton = new JButton("Create New Bid");
        c.gridy = 6;
        c.gridx = 2;
        this.add(createBidButton, c);

        tutorialsTaught = new JLabel("Tutorials you are teaching: ");
        c.gridx = 0;
        c.gridy = 7;
        c.gridwidth = 3;
        this.add(tutorialsTaught, c);

        tutorialsTaughtList = new JScrollPane();
        c.gridy = 8;
        c.gridheight = 4;
        this.add(tutorialsTaughtList, c);

        findBidsButton = new JButton("Find Bids");
        c.gridx = 2;
        c.gridy = 12;
        this.add(findBidsButton, c);

        viewProfileButton = new JButton("View Profile");
        c.gridy = 13;
        c.gridwidth = 1;
        this.add(viewProfileButton, c);

        seeBidsButton = new JButton("See Your Bids");
        c.gridx = 1;
        this.add(seeBidsButton, c);

        createBidButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ApplicationManager.loadPage(ApplicationManager.OPEN_BID_PAGE);
            }
        });

        viewProfileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ApplicationManager.loadPage(ApplicationManager.PROFILE_PAGE);
            }
        });

        seeBidsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ApplicationManager.loadPage(ApplicationManager.USER_BIDS);
            }
        });
    }

    @Override
    public void update(String userId) {
        this.userId = userId;
        HttpResponse<String> response = ApiRequest.get("/contract");
        if (response.statusCode() == 200) {
            JSONArray contracts = new JSONArray(response.body());
            System.out.println(contracts);

            response = ApiRequest.get("/user/" + userId);
            if (response.statusCode() == 200) {

                JSONObject user = new JSONObject(response.body());
                GridBagConstraints c = new GridBagConstraints();
                c.weightx = 1;
                c.weighty = 1;
                c.fill = GridBagConstraints.HORIZONTAL;
                c.insets = new Insets(5, 5, 0, 5);

                if (!user.getBoolean("isStudent")) {
                    this.remove(tutorialsTakenList);
                    this.remove(tutorialsTaken);
                    this.remove(createBidButton);
                } else {
                    c.gridy = 1;
                    c.gridwidth = 3;
                    c.gridheight = 1;
                    this.add(tutorialsTaken, c);

                    c.gridx = 0;
                    c.gridy = 2;
                    c.gridwidth = 3;
                    c.gridheight = 4;
                    this.add(this.tutorialsTakenList, c);

                    c.gridy = 6;
                    c.gridx = 2;
                    c.gridwidth = 1;
                    c.gridheight = 1;
                    this.add(createBidButton, c);
                }

                if (!user.getBoolean("isTutor")) {
                    this.remove(tutorialsTaughtList);
                    this.remove(tutorialsTaught);
                    this.remove(findBidsButton);
                } else {
                    c.gridx = 0;
                    c.gridy = 7;
                    c.gridwidth = 3;
                    c.gridheight = 1;
                    this.add(tutorialsTaught, c);

                    c.gridx = 0;
                    c.gridy = 8;
                    c.gridwidth = 3;
                    c.gridheight = 4;
                    this.add(this.tutorialsTaughtList, c);

                    c.gridx = 2;
                    c.gridy = 12;
                    c.gridwidth = 1;
                    c.gridheight = 1;
                    this.add(findBidsButton, c);
                }

                JPanel tutorialsTakenPanel = new JPanel();
                tutorialsTakenPanel.setLayout(new BoxLayout(tutorialsTakenPanel, BoxLayout.Y_AXIS));
                JPanel tutorialsTaughtPanel = new JPanel();
                tutorialsTaughtPanel.setLayout(new BoxLayout(tutorialsTaughtPanel, BoxLayout.Y_AXIS));
                for (int i = 0; i < contracts.length(); i++) {
                    JSONObject contract = (JSONObject) contracts.get(i);
                    if (contract.optJSONObject("secondParty").get("id").equals(this.userId)) {
                        JPanel componentPanel = new JPanel();
                        componentPanel.add(new JLabel(contract.optJSONObject("subject").optString("name") +
                                " - " + contract.optJSONObject("subject").optString("description")));
                        tutorialsTakenPanel.add(componentPanel);
                    } else if (contract.optJSONObject("firstParty").get("id").equals(this.userId)) {
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

    public void addFindBidListener(ActionListener listener) {
        this.findBidsButton.addActionListener(listener);
    }

    public void addCreateBidListener(ActionListener listener) {
        this.createBidButton.addActionListener(listener);
    }

    public void addSeeBidListener(ActionListener listener) { this.seeBidsButton.addActionListener(listener);}

    public String getUserId() {
        return this.userId;
    }
}
