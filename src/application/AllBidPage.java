package application;

import api.ApiRequest;
import controller.ObserverInputInterface;
import controller.ObserverOutputInterface;
import interfaces.EventSubscriber;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.http.HttpResponse;
import java.util.ArrayList;

// TODO: refactor all bid into controller class
public class AllBidPage extends JPanel implements EventSubscriber, ObserverInputInterface, ObserverOutputInterface {

    JPanel contentPanel = new JPanel();
    JScrollPane scrollPane;
    JLabel activityTitle;
    JSONArray bids;
    GridBagConstraints c;
    JButton viewBidBtn;
    ArrayList<JButton> buttonArr;
    private String userId;

    public AllBidPage() {
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

        activityTitle = new JLabel("Request List");
        activityTitle.setHorizontalAlignment(JLabel.CENTER);
        activityTitle.setVerticalAlignment(JLabel.TOP);
        activityTitle.setFont(new Font("Bahnschrift", Font.BOLD, 20));
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
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

                // add view detail button
                bidPanelConstraint.gridx = 6;
                bidPanelConstraint.gridwidth = 1;
                viewBidBtn = new JButton("View Bid");
                System.out.println(bid);
                System.out.println(bid.get("id"));
                viewBidBtn.setName(bid.get("id").toString()); // give a unique name to a button to distinguish the
                buttonArr.add(viewBidBtn); // add the button into button array
                bidPanel.add(viewBidBtn, bidPanelConstraint);

                c.gridx = 0;
                c.gridy = contentPanel.getComponentCount();
                c.gridwidth = 1;
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
            contentPanel.add(bidPanel);
        }
    }

    /**
     * Update the bids inside this panels whenever this page is load
     * @param data The user id that are currently using this page
     */
    @Override
    public void update(String data) {

        this.userId = data;
        JSONObject user;

        // get all bid
        HttpResponse<String> response = ApiRequest.get("/bid");
        bids = new JSONArray(response.body());

        // get the detail of the user
        response = ApiRequest.getUser("/user/" + this.userId, new String[] {"competencies", "competencies.subject"});
        user = new JSONObject(response.body());

        if (user.get("isTutor").equals(true)){

            // filter the bid based on the user competency and userId
            for (int i=0; i < bids.length(); i++){

                JSONObject bid = bids.getJSONObject(i);
                System.out.println(bid);

                // if bid is still open
                if (bid.get("dateClosedDown").equals(null)) {
                    JSONObject initiator = bid.getJSONObject("initiator");

                    // loop through every competency of the user
                    JSONArray userCompetencies = new JSONArray(user.getJSONArray("competencies"));
                    for (int j=0; j < userCompetencies.length(); j++){

                        JSONObject userCompetency = userCompetencies.getJSONObject(j);
                        int unknownCount = 0; //counter to see if the tutor has userCompetency in this subject

                        // if tutor knows current subject
                        if (userCompetency.getJSONObject("subject").get("id").equals(bid.getJSONObject("subject").get("id"))) {

                            // if min userCompetency level is higher than user don't show the bid
                            if (userCompetency.getInt("level") < bid.getInt("minCompetency")){
                                bids.remove(i);
                            }
                        } else {
                            unknownCount += 1;
                        }

                        // if tutor don't know about this subject don't show the bid
                        if (unknownCount == userCompetencies.length()){
                            bids.remove(i);
                        }
                    }

                    // if the poster of the bid is the tutor itself don't show the bid
                    if (initiator.get("id").equals(this.userId) ){
                        bids.remove(i);
                    }
                }

            }
        }


        // remake the jpanel
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
            for (JButton button: buttonArr){
                button.addActionListener(actionListener);
            }
        }

    }
}
