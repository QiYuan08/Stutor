package views.main_pages;

import services.ApiRequest;
import services.ViewManagerService;
import abstractions.ListenerLinkInterface;
import abstractions.ObserverOutputInterface;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.http.HttpResponse;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZonedDateTime;

public class DashboardPage extends JPanel implements ObserverOutputInterface, ListenerLinkInterface {

    private JLabel activityTitle, tutorialsTaken, tutorialsTaught;
    private JButton viewProfileButton, seeBidsButton, createBidButton, findBidsButton, viewContractButton, monitoredBidsButton;
    private JScrollPane tutorialsTakenList, tutorialsTaughtList;
    private String userId;

    public DashboardPage() {
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

        tutorialsTakenList = new JScrollPane();
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 3;
        c.gridheight = 4;
        this.add(tutorialsTakenList, c);

        createBidButton = new JButton("Create New Bid");
        c.gridy = 6;
        c.gridx = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        this.add(createBidButton, c);

        seeBidsButton = new JButton("See Your Bids");
        c.gridx = 2;
        this.add(seeBidsButton, c);

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
        c.gridheight = 1;
        c.weightx = 0.3;
        c.weighty = 0.5;
        c.gridwidth = 1;
        c.gridy = 12;
        this.add(findBidsButton, c);

        monitoredBidsButton = new JButton("Monitored Bids");
        c.gridx = 1;
        this.add(monitoredBidsButton, c);

        viewContractButton = new JButton("View Contracts");
        c.gridy = 13;
        c.gridx = 1;
        c.gridwidth = 2;
        this.add(viewContractButton, c);

        viewProfileButton = new JButton("View Profile");
        c.gridy = 14;
        c.gridx = 1;
        c.gridwidth = 2;
        this.add(viewProfileButton, c);

        createBidButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ViewManagerService.loadPage(ViewManagerService.CREATE_BID);
            }
        });

        viewProfileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ViewManagerService.loadPage(ViewManagerService.PROFILE_PAGE);
            }
        });
    }

    @Override
    public void update(String userId) {
        this.userId = userId;
        HttpResponse<String> response = ApiRequest.get("/contract");
        if (response.statusCode() == 200) {
            JSONArray contracts = new JSONArray(response.body());

            response = ApiRequest.get("/user/" + userId + "?fields=initiatedBids");
            if (response.statusCode() == 200) {

                JSONObject user = new JSONObject(response.body());
                GridBagConstraints c = new GridBagConstraints();
                c.weightx = 1;
                c.weighty = 1;
                c.fill = GridBagConstraints.BOTH;
                c.insets = new Insets(5, 5, 0, 5);

                this.remove(createBidButton);

                if (!user.getBoolean("isStudent")) {
                    this.remove(tutorialsTakenList);
                    this.remove(tutorialsTaken);
                    this.remove(seeBidsButton);

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

                    if (checkContractsBidsCount(contracts, user)) {
                        c.gridy = 6;
                        c.gridx = 1;
                        c.gridwidth = 1;
                        c.gridheight = 1;
                        this.add(createBidButton, c);

                    }

                    c.gridy = 6;
                    c.gridx = 2;
                    c.gridwidth = 1;
                    c.gridheight = 1;
                    this.add(seeBidsButton, c);

                }

                if (!user.getBoolean("isTutor")) {
                    this.remove(tutorialsTaughtList);
                    this.remove(tutorialsTaught);
                    this.remove(findBidsButton);
                    this.remove(monitoredBidsButton);
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
                    this.add(tutorialsTaughtList, c);

                    c.gridx = 2;
                    c.gridy = 12;
                    c.gridwidth = 1;
                    c.gridheight = 1;
                    this.add(findBidsButton, c);

                    c.gridx = 1;
                    c.gridy = 12;
                    this.add(monitoredBidsButton, c);

                }

                JPanel tutorialsTakenPanel = new JPanel();
                tutorialsTakenPanel.setLayout(new BoxLayout(tutorialsTakenPanel, BoxLayout.Y_AXIS));
                JPanel tutorialsTaughtPanel = new JPanel();
                tutorialsTaughtPanel.setLayout(new BoxLayout(tutorialsTaughtPanel, BoxLayout.Y_AXIS));
                for (int i = 0; i < contracts.length(); i++) {
                    JSONObject contract = (JSONObject) contracts.get(i);
                    if (!contract.isNull("dateSigned")) {
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
                }
                this.tutorialsTakenList.setViewportView(tutorialsTakenPanel);
                this.tutorialsTaughtList.setViewportView(tutorialsTaughtPanel);
            }
        }
    }

    /**
     * checks if the student has 5 or more active contracts and bids, and disallows them from creating more bids
      */
    private boolean checkContractsBidsCount(JSONArray contracts, JSONObject user) {

        Timestamp ts = Timestamp.from(ZonedDateTime.now().toInstant());
        Instant now = ts.toInstant();
        int counter = 0;
        for (int i = 0; i < contracts.length(); i++) {
            JSONObject contract = (JSONObject) contracts.get(i);
            if (contract.getJSONObject("secondParty").getString("id").equals(userId)) {
                Instant expiryDate = Instant.parse(contract.getString("expiryDate"));
                if (now.compareTo(expiryDate) < 0) {
                    counter++;
                }
            }
        }
        JSONArray bids = user.getJSONArray("initiatedBids");
        for (int i = 0; i < bids.length(); i++) {
            JSONObject bid = (JSONObject) bids.get(i);
            if (bid.isNull("dateClosedDown")) {
                counter++;
            }
        }
        if (counter < 5) {
            return true;
        }
        return false;
    }

    @Override
    public void addLinkListener(ActionListener actionListener) {
        this.findBidsButton.addActionListener(actionListener);
        this.seeBidsButton.addActionListener(actionListener);
        this.viewContractButton.addActionListener(actionListener);
        monitoredBidsButton.addActionListener(actionListener);
    }
}
