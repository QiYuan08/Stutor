package views.tutor_responds;

import api.ApiRequest;
import services.ViewManagerService;
import interfaces.ListenerLinkInterface;
import interfaces.ObserverInputInterface;
import interfaces.ObserverOutputInterface;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.http.HttpResponse;
import java.util.ArrayList;

public class FindBidDetails extends JPanel implements ObserverOutputInterface, ObserverInputInterface, ListenerLinkInterface {

    private String bidId, userId;
    private JLabel title, subjectLabel, name, rate, competency, duration, startTime, day, preferredSession, bidderLabel;
    private JButton buyoutButton, respondButton, backButton, viewBidButton;
    private JPanel detailPane;
    private JScrollPane scrollPane;
    ArrayList<JButton> buttonArr;
    private GridBagConstraints mainConst;

    public FindBidDetails() {
        this.setLayout(new GridBagLayout());
        mainConst = new GridBagConstraints();
        buyoutButton = new JButton("Buy Out");
        respondButton = new JButton("Bid");
    }

    /**
     * Create the content to display the detail of the bid after user enter this page
     * @param bid the bid to display
     */
    void createContent(JSONObject bid){

        JSONObject initiator = bid.getJSONObject("initiator");
        JSONObject subject = bid.getJSONObject("subject");
        JSONObject additionalInfo = bid.getJSONObject("additionalInfo");
        JSONArray messages = bid.getJSONArray("messages");

        detailPane = new JPanel();
        detailPane.setBorder(new EmptyBorder(15, 15,15,15));
        detailPane.setLayout(new GridBagLayout());
        detailPane.setBackground(new Color(255, 252, 252));
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1;
        c.weighty = 0.2;
        c.insets = new Insets(2, 2, 2, 2);
        c.fill = GridBagConstraints.HORIZONTAL;
        // innner panel for detail
        c.weightx = 0.5;
        c.weighty = 0.5;

        title = new JLabel("Bid Details");
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setVerticalAlignment(JLabel.TOP);
        title.setFont(new Font("Bahnschrift", Font.BOLD, 20));
        c.gridy = detailPane.getComponentCount();
        c.gridwidth = 3;
        c.gridx = 1;
        c.anchor = GridBagConstraints.PAGE_START;
        detailPane.add(title, c);

        backButton = new JButton("Back");
        c.gridy = 0;
        c.weightx = 0.0;
        c.gridwidth = 1;
        c.gridx = 0;
        c.anchor = GridBagConstraints.PAGE_START;
        detailPane.add(backButton, c);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ViewManagerService.loadPage(ViewManagerService.DASHBOARD_PAGE);
            }
        });

        subjectLabel = new JLabel("Subject: " + subject.get("name"));
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridwidth = 3;
        c.gridy = detailPane.getComponentCount();
        c.anchor = GridBagConstraints.PAGE_START;
        detailPane.add(subjectLabel, c);

        name = new JLabel("Name: " + initiator.get("givenName") +" " + initiator.get("familyName"));
        c.gridx = 0;
        c.gridwidth = 3;
        c.gridy = detailPane.getComponentCount();
        c.anchor = GridBagConstraints.PAGE_START;
        detailPane.add(name, c);

        // if rate is provided in the bid
        if (additionalInfo.has("rate")){
            rate = new JLabel("Rate: " + additionalInfo.get("rate"));
        } else {
            rate = new JLabel("Rate not provided");
        }
        c.gridy = detailPane.getComponentCount();
        detailPane.add(rate, c);

        // if competency is provided in the bid
        if (additionalInfo.has("minCompetency")){
            competency = new JLabel("Minimum competency: " + additionalInfo.get("minCompetency"));
        } else {
            competency = new JLabel("Competency not provided");
        }
        c.gridy = detailPane.getComponentCount();
        detailPane.add(competency, c);

        // if day is provided in the bid
        if (additionalInfo.has("day")){
            day = new JLabel("Preferred Day(s): " + additionalInfo.get("day"));
        } else {
            day = new JLabel("Preferred day(s) not provided");
        }
        c.gridy = detailPane.getComponentCount();
        detailPane.add(day, c);

        // if preferred session is provided in the bid
        if (additionalInfo.has("preferredSession")){
            preferredSession = new JLabel("Preferred no of sessions: " + additionalInfo.get("preferredSession") + " sessions per week");
        } else {
            preferredSession = new JLabel("Preferred sessions not provided");
        }
        c.gridy = detailPane.getComponentCount();
        detailPane.add(preferredSession, c);

        // if duration is provided in the bid
        if (additionalInfo.has("duration")){
            duration = new JLabel("Duration: " + additionalInfo.get("duration") + " hours per lesson");
        } else {
            duration = new JLabel("Duration not provided");
        }
        c.gridy = detailPane.getComponentCount();
        detailPane.add(duration, c);

        // if start time is provided in the bid
        if (additionalInfo.has("startTime")){
            startTime = new JLabel("Start Time: " + additionalInfo.get("startTime"));
        } else {
            startTime = new JLabel("Start Time not provided");
        }
        c.gridy = detailPane.getComponentCount();
        detailPane.add(startTime, c);

        // if bid type is open
        if (bid.getString("type").equals("open")){

            buttonArr = new ArrayList<>();

            // create a Panel to show each message replied by tutor
            if (messages.length() > 0){

                bidderLabel = new JLabel("Bidders");
                bidderLabel.setHorizontalAlignment(JLabel.CENTER);
                bidderLabel.setFont(new Font("Bahnschrift", Font.BOLD, 20));
                detailPane.add(bidderLabel, c);

                for (int i=0; i < messages.length(); i++){
                    JSONObject message = messages.getJSONObject(i);

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
                    JSONObject bidder = message.getJSONObject("poster");
                    bidLabel.setText(bidder.get("givenName") + " " + bidder.get("familyName"));
                    bidPanel.add(bidLabel, bidPanelConstraint);

                    // type jlabel
                    JLabel rate = new JLabel();
                    rate.setText("Rate: " + message.getJSONObject("additionalInfo").get("rate") + " dollars per hour");
                    bidPanelConstraint.gridy = 1;
                    bidPanel.add(rate, bidPanelConstraint);

                    // add view detail button
                    bidPanelConstraint.gridy = 0;
                    bidPanelConstraint.gridx = 6;
                    bidPanelConstraint.gridwidth = 1;
                    bidPanelConstraint.gridheight = 2;
                    bidPanelConstraint.weightx = 0.2;
                    viewBidButton = new JButton("View Bid");

                    // TODO: test out this code without findBidListener
                    viewBidButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            ViewManagerService.loadPage(ViewManagerService.FIND_TUTOR_RESPONSE);
                        }
                    });

                    // set button name to bidId and userId for ClosedBidResponse class to close Bid
                    JSONObject btnData = new JSONObject();
                    btnData.put("bidId", message.get("id"));
                    btnData.put("userId", this.userId);
                    viewBidButton.setName(btnData.toString());
                    buttonArr.add(viewBidButton); // add the button into button array
                    bidPanel.add(viewBidButton, bidPanelConstraint);

                    c.gridy = detailPane.getComponentCount();
                    detailPane.add(bidPanel, c);
                }
            }

        }
        // wrap detailPane with a scrollPane
        scrollPane = new JScrollPane(detailPane);

        // add scrollPane into this
        mainConst.weighty = 1;
        mainConst.weightx = 1;
        mainConst.gridheight = 20;
        mainConst.gridx = 0;
        mainConst.gridy = 0;
        c.gridwidth = 10;
        mainConst.fill = GridBagConstraints.HORIZONTAL;
        this.setOpaque(false);
        this.add(scrollPane, mainConst);

        // add closeBid Button
        // if its a a open bid add buy out button

        if (bid.getString("type").equals("open")){
            buyoutButton.setName(this.bidId);
            mainConst.weighty = 1;
            mainConst.weightx = 1;
            mainConst.gridheight = 2;
            mainConst.gridx = 0;
            mainConst.gridy = 22;
            mainConst.gridwidth = 1;
//            mainConst.anchor = GridBagConstraints.LAST_LINE_START;
            mainConst.fill = GridBagConstraints.HORIZONTAL;
            this.add(buyoutButton, mainConst);
        }


        // add replyBid Button
        if (bid.get("type").equals("close") && hasReplied(messages)){ // check if tutor reply to this bid before for close bid
            respondButton.setText("Message");
            this.repaint();
        }
        String data = new JSONObject().put("bidId", this.bidId).put("userId", this.userId).toString();
        respondButton.setName(data);
        mainConst.weighty = 1;
        mainConst.weightx = 1;
        mainConst.gridheight = 2;
        mainConst.gridx = 0;
        mainConst.gridy = 21;
        mainConst.gridwidth = 1;
//        mainConst.anchor = GridBagConstraints.LAST_LINE_START;
        mainConst.fill = GridBagConstraints.HORIZONTAL;
        this.add(respondButton, mainConst);
    }

    /**
     * Method to check if the tutor replied to this bid before
     * @return true if tutor replied to this message before and false otherwise
     */
    private Boolean hasReplied(JSONArray messages){

        if (messages.isEmpty()){ // if this bis has no messages
            return false;
        } else {
            for (int i=0; i< messages.length(); i++){
                JSONObject message = messages.getJSONObject(i);
                if (message.getJSONObject("poster").getString("id").equals(this.userId)){ // if user replied to this bid before
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * Get the bidId from Find Bid page one user click on view bid to retrieve data from db
     * @param data any data that is crucial to the pages for them to request the information that they need from the database
     */
    @Override
    public void update(String data) {

        this.bidId = new JSONObject(data).getString("bidId");
        this.userId = new JSONObject(data).getString("userId");
        HttpResponse<String> response = ApiRequest.get("/bid/"+ this.bidId + "?fields=messages");

        // if retrieve success
        if (response.statusCode() == 200){

            this.removeAll();
            this.repaint();
            this.revalidate();

            // set the default value of reply button to respond
            respondButton.setText("Respond");
            createContent(new JSONObject(response.body()));

        } else {
            String msg = "Error: " + new JSONObject(response.body()).get("message");
            JOptionPane.showMessageDialog(new JFrame(), msg, "Bad request", JOptionPane.ERROR_MESSAGE);
        }

    }

    /**
     * Links the responseBtn with the appropriate page depending on whether the current bid is an open or closed bid
     */
    @Override
    public void addLinkListener(ActionListener listener) {
        this.respondButton.addActionListener(listener);
    }

    public  void addViewBidListener(ActionListener listener) {

        if (buttonArr != null){ // check when the page first load during apps startup
            for (JButton btn: buttonArr){
                btn.addActionListener(listener);
            }
        }
    }

    /**
     * called by BidClosingController, which is activate by buyoutBtn
     */
    @Override
    public JSONObject retrieveInputs() {
        JSONObject bidInfo = new JSONObject();
        bidInfo.put("bidId", bidId);
        bidInfo.put("messageId", "");
        bidInfo.put("tutorId", "");
        bidInfo.put("hasExpired", false);
        return bidInfo;
    }

    /**
     * adds the action listener from a BidClosingController object
     */
    @Override
    public void addActionListener(ActionListener actionListener) {
        this.buyoutButton.addActionListener(actionListener);
    }
}
