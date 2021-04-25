package application;

import api.ApiRequest;
import application.Application;
import controller.ObserverInputInterface;
import controller.ObserverOutputInterface;
import interfaces.EventSubscriber;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.AllBidUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.http.HttpResponse;

// TODO: refactor into composite design pattern such that separate bid in content panel into another class
// TODO: refactor all bid into controller class
public class AllBidPage extends JPanel implements EventSubscriber, ObserverInputInterface, ObserverOutputInterface {

    JPanel contentPanel = new JPanel();
    JScrollPane scrollPane;
    JLabel activityTitle;
    JSONArray bids;
    GridBagConstraints c;
    JButton viewBidBtn;
    JButton[] buttonArr;
    private String userId;

    public AllBidPage() {
        this.setBorder(new EmptyBorder(2, 2, 2, 2));
        this.setLayout(new GridLayout(1,1, 2, 2));

        contentPanel.setLayout(new GridBagLayout());
        contentPanel.setBackground(Color.cyan);
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
//        this.add(scrollPane, c);

        // add all the bids into content pane
        // need a controller class to sync all the bid
        bids = new JSONArray();
        bids.put(new JSONObject().put("type", "open").put("id", "bc06e9ad-5d20-4dce-a176-a6ac73b26b35"));
        bids.put(new JSONObject().put("type", "close").put("id", "bc06e9ad-5d20-4dce-a176-a6ac73b26b35"));
        bids.put(new JSONObject().put("type", "close").put("id", "bc06e9ad-5d20-4dce-a176-a6ac73b26b35"));
        bids.put(new JSONObject().put("type", "close").put("id", "bc06e9ad-5d20-4dce-a176-a6ac73b26b35"));
        bids.put(new JSONObject().put("type", "close").put("id", "bc06e9ad-5d20-4dce-a176-a6ac73b26b35"));
        bids.put(new JSONObject().put("type", "close").put("id", "bc06e9ad-5d20-4dce-a176-a6ac73b26b35"));
        bids.put(new JSONObject().put("type", "close").put("id", "bc06e9ad-5d20-4dce-a176-a6ac73b26b35"));
        bids.put(new JSONObject().put("type", "close").put("id", "bc06e9ad-5d20-4dce-a176-a6ac73b26b35"));
        bids.put(new JSONObject().put("type", "close").put("id", "bc06e9ad-5d20-4dce-a176-a6ac73b26b35"));
        bids.put(new JSONObject()
                .put("id", "96b93474-5b31-4383-829c-1274f7fef403")
                .put("duration", "1")
                .put("dateCreated", "2021-04-25T10:21:00.023328100Z")
                .put("noOfoLesson", "1")
                .put("rate", "10")
                .put("additionalInfo", new JSONObject())
                .put("preferredSession", 1)
                .put("initiatorId", "ecc52cc1-a3e4-4037-a80f-62d3799645f4")
                .put("minCompetency", 1)
                .put("startTime", "1PM")
                .put("type", "open")
                .put("day", "Monday")
                .put("subjectId", "88f6ee80-4e7b-49b2-847b-23612d8a6f2f"));

        createContent();
    }

    /***
     * Method to create all the bid available in the db and show it to the user inside the contentPanel
     */
    private void createContent() {

        // button array to add event listener to them later one
        buttonArr = new JButton[bids.length()];

        c.insets = new Insets(2,3,2,3); //spacing between each bids
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.PAGE_START;
        c.ipady = 50;

        // create a jPanel for each bids available
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
            bidLabel.setText("Description");
            bidPanel.add(bidLabel, bidPanelConstraint);

            // add view detail button
            bidPanelConstraint.gridx = 6;
            bidPanelConstraint.gridwidth = 1;
            viewBidBtn = new JButton("View Bid");
            viewBidBtn.setName(bid.get("id").toString()); // give a unique name to a button to distinguish the
            buttonArr[i] = viewBidBtn; // add the button into button array
            bidPanel.add(viewBidBtn, bidPanelConstraint);

            // if this is an open bid add close bid button
//            if (bid.get("type").equals("open")){
//                bidPanelConstraint.anchor = GridBagConstraints.LINE_END;
//                bidPanelConstraint.gridx = 5;
//                bidPanelConstraint.gridwidth = 1;
//                closeBidBtn = new JButton("Buy Out Bid");
//                closeBidBtn.setName(bid.get("id").toString()); // give a unique name to a button to distinguish them
////                closeBidBtn.addActionListener(new ActionListener() {
////                    @Override
////                    public void actionPerformed(ActionEvent e) {
////                        JButton thisBtn = (JButton) e.getSource();
////                        AllBidUtil util = new AllBidUtil();
////                        util.closeBid(thisBtn.getName());
////                    }
////                });
//                bidPanel.add(closeBidBtn, bidPanelConstraint);
//            }

            c.gridx = 0;
            c.gridy = contentPanel.getComponentCount();
            c.gridwidth = 1;
            c.gridheight = 1;
            contentPanel.add(bidPanel, c);
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
        response = ApiRequest.get("/user/" + this.userId);
        user = new JSONObject(response.body());

        // filter the bid based on the user competency and userId
        for (int i=0; i < bids.length(); i++){

            JSONObject bid = bids.getJSONObject(i);

            // if bid is still open
            if (bid.get("dateClosedDown").equals(null)) {
                JSONObject initiator = bid.getJSONObject("initiator");

                // loop through every competency of the user
                JSONArray userCompetencies = new JSONArray(user.get("competencies"));
                for (int j=0; j < userCompetencies.length(); j++){

                    JSONObject competency = userCompetencies.getJSONObject(j);
                    int unknownCount = 0; //counter to see if the tutor has competency in this subject

                    // if tutor knows current subject
                    if (competency.getJSONObject("subject").get("id").equals(bid.get("subjectId"))) {

                        // if min competency level is higher than user don't show the bid
                        if (competency.getInt("level") < bid.getInt("minCompetency")){
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
        for (JButton button: buttonArr){
            button.addActionListener(actionListener);
        }
    }
}
