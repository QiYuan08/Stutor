package application.bid_pages;

import api.ApiRequest;
import application.Application;
import application.ApplicationManager;
import controller.ObserverInputInterface;
import controller.ObserverOutputInterface;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.http.HttpResponse;
import java.util.ArrayList;

// TODO: fix the layout =.=
public class SeeBidsPage extends JPanel implements ObserverOutputInterface, ObserverInputInterface {

    JPanel contentPanel = new JPanel();
    JScrollPane scrollPane;
    JLabel activityTitle;
    JSONArray bids;
    GridBagConstraints c;
    JButton viewBidBtn, backBtn;
    ArrayList<JButton> buttonArr;
    private String userId;

    public SeeBidsPage() {
        this.setBorder(new EmptyBorder(2, 2, 2, 2));
        this.setLayout(new GridLayout(1,1, 2, 2));
    }

    /***
     * Method to create all the bid available in the db and show it to the user inside the contentPanel
     */
    private void createContent() {

        buttonArr = new ArrayList<>();

        contentPanel.setLayout(new GridBagLayout());
        contentPanel.setBackground(new Color(153, 255, 255));
        contentPanel.setMinimumSize(new Dimension(this.getWidth(), this.getHeight()));
        c = new GridBagConstraints();
//        c.weighty = 1;
        c.insets = new Insets(1, 1, 1, 1);

        backBtn = new JButton("Back");
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.weightx = 0.2;
        c.anchor = GridBagConstraints.PAGE_START;
        contentPanel.add(backBtn, c);

        activityTitle = new JLabel("Your Bid");
        activityTitle.setHorizontalAlignment(JLabel.CENTER);
        activityTitle.setVerticalAlignment(JLabel.TOP);
        activityTitle.setFont(new Font("Bahnschrift", Font.BOLD, 20));
        c.weightx = 1;
        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 2;
        contentPanel.add(activityTitle, c);

        // wrap contentPanel inside a scrollpane
        scrollPane = new JScrollPane(contentPanel);
        this.add(scrollPane, c);

        c.insets = new Insets(2,3,2,3); //spacing between each bids
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.PAGE_START;
        c.ipady = 50;


        // create a jPanel for each bids available
        if (bids.length() > 0) {
            for (int i=0; i < bids.length(); i++){

                JSONObject bid = bids.getJSONObject(i);

                // create the panel for each bid item
                JPanel bidPanel = new JPanel();
                GridBagConstraints bidPanelConstraint = new GridBagConstraints();
                bidPanelConstraint.fill = GridBagConstraints.HORIZONTAL;
                bidPanelConstraint.weightx = 1;
                bidPanelConstraint.insets = new Insets(1,2,1,2);
                bidPanel.setLayout(new GridBagLayout());
                bidPanel.setBackground(Color.lightGray);
                bidPanel.setMinimumSize(new Dimension(100, 120));
                bidPanel.setMaximumSize(new Dimension(100, 120));

                // add a description jlabel
                bidPanelConstraint.gridx = 0;
                bidPanelConstraint.gridy = 0;
                bidPanelConstraint.gridwidth = 5;
                bidPanelConstraint.anchor = GridBagConstraints.WEST;
                JLabel bidLabel = new JLabel();
                bidLabel.setText("Subject: " + bid.getJSONObject("subject").get("name"));
                bidPanel.add(bidLabel, bidPanelConstraint);

                // type jlabel
                JLabel bidType = new JLabel();
                bidType.setText("Type: " + bid.getString("type"));
                bidPanelConstraint.gridy = 1;
                bidPanel.add(bidType, bidPanelConstraint);

                // add view detail button
                bidPanelConstraint.gridx = 6;
                bidPanelConstraint.gridwidth = 1;
                bidPanelConstraint.weightx = 0.2;
                viewBidBtn = new JButton("View Bid");

                // set button name to bidId and userId for ResponseCloseBid class to close Bid
                JSONObject btnData = new JSONObject();
                btnData.put("bidId", bid.get("id"));
                btnData.put("userId", this.userId);
                viewBidBtn.setName(btnData.toString()); // give a unique name to a button to distinguish the
                buttonArr.add(viewBidBtn); // add the button into button array
                bidPanel.add(viewBidBtn, bidPanelConstraint);

                c.gridx = 0;
                c.weightx = 1;
                c.gridy = contentPanel.getComponentCount();
                c.gridwidth = 5;
                c.gridheight = 1;
                contentPanel.add(bidPanel, c);
            }

        } else { // if not relevant bid found
            JPanel bidPanel = new JPanel();
            JLabel noBid = new JLabel("No Bid Found");
            activityTitle.setHorizontalAlignment(JLabel.CENTER);
            activityTitle.setVerticalAlignment(JLabel.CENTER);
            activityTitle.setFont(new Font("Bahnschrift", Font.BOLD, 20));
            bidPanel.add(noBid);
            c.gridx = 0;
            c.gridy = contentPanel.getComponentCount();
            c.gridwidth = 4;
            c.gridheight = 1;
            contentPanel.add(bidPanel);
        }

        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ApplicationManager.loadPage(ApplicationManager.DASHBOARD_PAGE);
            }
        });
    }

    @Override
    public JSONObject retrieveInputs() {
        return null;
    }

    /**
     * Update every view detail button in this page
     * @param actionListener action listener class for each button
     */
    @Override
    public void addActionListener(ActionListener actionListener) {
        if (buttonArr != null) {
            for (JButton button: buttonArr){
                button.addActionListener(actionListener);
            }
        }
    }

    /**
     * Update the panels with all the bids that are openend by the user
     * @param data The user id that are currently using this page
     */
    @Override
    public void update(String data) {
        if (this.userId == null) {
            this.userId = data;
        }

        // get all bid
        HttpResponse<String> response = ApiRequest.get("/user/" + this.userId + "?fields=initiatedBids");
        bids = new JSONArray(new JSONObject(response.body()).getJSONArray("initiatedBids"));

        // remake the jpanel
        this.removeAll();
        this.repaint();
        this.revalidate();

        contentPanel.removeAll();
        contentPanel.repaint();
        contentPanel.revalidate();
        createContent();
    }


}
