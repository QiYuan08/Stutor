package application;

import api.ApiRequest;
import controller.ObserverInputInterface;
import controller.ObserverOutputInterface;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.sound.midi.SysexMessage;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.http.HttpResponse;
import java.util.ArrayList;

public class FindBidPage extends JPanel implements ObserverInputInterface, ObserverOutputInterface {

    JPanel contentPanel = new JPanel();
    JScrollPane scrollPane;
    JLabel activityTitle;
    JSONArray bids;
    GridBagConstraints c;
    JButton viewBidBtn, backBtn;
    ArrayList<JButton> buttonArr;
    private String userId;

    public FindBidPage() {
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
        c.weightx = 1;
//        c.weighty = 1;
        c.insets = new Insets(1, 1, 1, 1);

        backBtn = new JButton("Back");
        c.gridy = 0;
        c.weightx = 0.0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = 0;
        c.anchor = GridBagConstraints.PAGE_START;
        contentPanel.add(backBtn, c);

        activityTitle = new JLabel("Request List");
        activityTitle.setHorizontalAlignment(JLabel.CENTER);
        activityTitle.setVerticalAlignment(JLabel.TOP);
        activityTitle.setFont(new Font("Bahnschrift", Font.BOLD, 20));
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 1;
        c.gridwidth = 3;
        c.anchor = GridBagConstraints.NORTH;
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
                bidLabel.setText(bid.getJSONObject("subject").get("name") + " (Level " +
                        bid.getJSONObject("additionalInfo").get("minCompetency") + ")");
                bidPanel.add(bidLabel, bidPanelConstraint);

                // add view detail button
                bidPanelConstraint.gridx = 6;
                bidPanelConstraint.gridwidth = 1;
                bidPanelConstraint.weightx = 0.2;
                viewBidBtn = new JButton("View Bid");
//                System.out.println(bid);
                viewBidBtn.setName(bid.get("id").toString()); // give a unique name to a button to distinguish the
                buttonArr.add(viewBidBtn); // add the button into button array
                bidPanel.add(viewBidBtn, bidPanelConstraint);

                c.gridx = 0;
                c.gridy = contentPanel.getComponentCount() - 1;
                c.gridwidth = 4;
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
                Application.loadPage(Application.DASHBOARD_PAGE);
            }
        });
    }

    /**
     * Update the bids inside this panels whenever this page is load
     * @param data The user id that are currently using this page
     */
    @Override
    public void update(String data) {

        System.out.println("update in findbidpage called");
        this.userId = data;
        JSONObject user;
        bids = new JSONArray();

        // get all bid
        HttpResponse<String> response = ApiRequest.get("/bid");
        JSONArray returnedBids = new JSONArray(response.body());

        // get the detail of the user
        response = ApiRequest.getUser("/user/" + this.userId, new String[] {"competencies", "competencies.subject"});
        user = new JSONObject(response.body());

        if (user.get("isTutor").equals(true)){

            // add every bid that is qualified to be teached by this user to bids
            for (int i =0; i < returnedBids.length(); i++){

                JSONObject bid = returnedBids.getJSONObject(i);

                // if the bid still open
                if (bid.get("dateClosedDown").equals(null) ) {
                    // for some bids that doesn't have min competency
                    if (bid.getJSONObject("additionalInfo").has("minCompetency") == false) {
                        bids.put(bid);

                    } else { // if that bid has competency
                        // check this subject with every competency of this user
                        JSONArray userCompetencies = user.getJSONArray("competencies");
                        for (int j = 0; j < userCompetencies.length(); j++){

                            // current competency
                            JSONObject competency = userCompetencies.getJSONObject(j);

                            // if user know this subject
                            if (competency.getJSONObject("subject").get("id").equals(bid.getJSONObject("subject").get("id"))) {

                                // compare the competency level
                                if (competency.getInt("level") >= (bid.getJSONObject("additionalInfo").getInt("minCompetency") + 2)) {
                                    bids.put(bid);
                                }
                            }
                        }
                    }
                }
            }
        }


        // remake the jpanel
        this.removeAll();
        this.repaint();
        this.revalidate();

        contentPanel.removeAll();
        contentPanel.repaint();
        contentPanel.revalidate();
        createContent();
    }

    @Override
    public JSONObject retrieveInputs() {
        return null;
    }

    /**
     * Method to set event listener for every view bid button
     * @param actionListener actionListener for the view bid button
     */
    @Override
    public void addActionListener(ActionListener actionListener) {

        if (buttonArr != null) {
            System.out.println("hello from findbidListener");
            for (JButton button: buttonArr){
                button.addActionListener(actionListener);
            }
        }

    }
}
