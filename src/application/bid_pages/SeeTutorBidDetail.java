package application.bid_pages;

import api.ApiRequest;
import application.ApplicationManager;
import controller.ObserverOutputInterface;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.http.HttpResponse;
import java.util.ArrayList;

public class SeeTutorBidDetail extends JPanel implements ObserverOutputInterface {

    private String messageId, userId;
    private JLabel title, name, rate, competency, duration, startTime, day, preferredSession;
    private JButton backBtn, viewBidBtn;
    private JPanel detailPane, btnPane;
    private JScrollPane scrollPane;
    ArrayList<JButton> buttonArr;
    private GridBagConstraints mainConst;

    public SeeTutorBidDetail() {
        this.setLayout(new GridBagLayout());
        mainConst = new GridBagConstraints();

    }

    /**
     * Create the content to display the detail of the message after user enter this page
     * @param message the message to display
     */
    void createContent(JSONObject message){

        JSONObject initiator = message.getJSONObject("poster");
        JSONObject additionalInfo = message.getJSONObject("additionalInfo");

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

        title = new JLabel("Bid Detail");
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setVerticalAlignment(JLabel.TOP);
        title.setFont(new Font("Bahnschrift", Font.BOLD, 20));
        c.gridy = detailPane.getComponentCount();
        c.gridwidth = 3;
        c.gridx = 1;
        c.anchor = GridBagConstraints.PAGE_START;
        detailPane.add(title, c);

        name = new JLabel("Name: " + initiator.get("givenName") +" " + initiator.get("familyName"));
        c.gridx = 0;
        c.gridwidth = 3;
        c.gridy = detailPane.getComponentCount();
        c.anchor = GridBagConstraints.PAGE_START;
        detailPane.add(name, c);

        // if rate is provided in the message
        if (additionalInfo.has("rate")){
            rate = new JLabel("Rate: " + additionalInfo.get("rate"));
        } else {
            rate = new JLabel("Rate not provided");
        }
        c.gridy = detailPane.getComponentCount();
        detailPane.add(rate, c);

        // if competency is provided in the message
        if (additionalInfo.has("minCompetency")){
            competency = new JLabel("Minimum competency: " + additionalInfo.get("minCompetency"));
        } else {
            competency = new JLabel("Competency not provided");
        }
        c.gridy = detailPane.getComponentCount();
        detailPane.add(competency, c);

        // if day is provided in the message
        if (additionalInfo.has("day")){
            day = new JLabel("Preferred Day(s): " + additionalInfo.get("day"));
        } else {
            day = new JLabel("Day not provided");
        }
        c.gridy = detailPane.getComponentCount();
        detailPane.add(day, c);

        // if preferred session is provided in the message
        if (additionalInfo.has("preferredSession")){
            preferredSession = new JLabel("Preferred no of lessons: " + additionalInfo.get("preferredSession") + " lessons per week");
        } else {
            preferredSession = new JLabel("Preferred sessions not provided");
        }
        c.gridy = detailPane.getComponentCount();
        detailPane.add(preferredSession, c);

        // if duration is provided in the message
        if (additionalInfo.has("duration")){
            duration = new JLabel("Duration: " + additionalInfo.get("duration") + " hours per lesson");
        } else {
            duration = new JLabel("Duration not provided");
        }
        c.gridy = detailPane.getComponentCount();
        detailPane.add(duration, c);

        // if start time is provided in the message
        if (additionalInfo.has("startTime")){
            startTime = new JLabel("Start Time: " + additionalInfo.get("startTime"));
        } else {
            startTime = new JLabel("Start Time not provided");
        }
        c.gridy = detailPane.getComponentCount();
        detailPane.add(startTime, c);

        // add scrollPane into mainPanel
        mainConst.weighty = 1;
        mainConst.weightx = 1;
        mainConst.gridheight = 8;
        mainConst.gridx = 0;
        c.gridwidth = 10;
        mainConst.gridy = 0;
        mainConst.fill = GridBagConstraints.BOTH;
//        this.add(detailPane, mainConst);

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

        // button Pane
        btnPane = new JPanel();

        btnPane.setBorder(new EmptyBorder(10, 10,10,10));

        // check whether this message is buy-ed out and add confirm message button
//        HttpResponse<String> response = ApiRequest.get("/message/" + this.messageId);
//        JSONObject message = new JSONObject(response.body());

        HttpResponse<String> response = ApiRequest.get("/bid/" + message.getString("bidId"));
        JSONObject bid = new JSONObject(response.body());

        if (bid.getString("type").equals("open")){  // if its open message
            btnPane.setLayout(new GridLayout(1,1));

            if (bid.isNull("dateClosedDown")){ // if message have not yet been bought out
                JButton confirmBtn = new JButton("Confirm Bid");
                btnPane.add(confirmBtn);
            }
        } else if (bid.getString("type").equals("close")){  // if its close message

            if (bid.isNull("dateClosedDown")){
                // if the message is not close yet add view message and confirm message button
                btnPane.setLayout(new GridLayout(1,2));

                JButton messageBtn = new JButton("Message");
                btnPane.add(messageBtn);

                JButton confirmBtn = new JButton("Confirm Bid");
                btnPane.add(confirmBtn);
            }
        }
        
        // add btnPanel into this
        mainConst.weighty = 0.2;
        mainConst.weightx = 0.2;
        mainConst.gridheight = 2;
        mainConst.gridx = 1;
        mainConst.gridy = 30;
        c.gridwidth = 10;
        detailPane.add(btnPane, mainConst);

        // TODO: can talk about this in design rationale, nonid to create a listener for this cuz very simple and wont change forever
        // TODO: fix view not updated when student message on it
        backBtn = new JButton("Back");
        mainConst.gridy = 0;
        mainConst.gridx = 0;
        mainConst.gridheight = 1;
        mainConst.gridwidth = 1;
        detailPane.add(backBtn, mainConst);

        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ApplicationManager.loadPage(ApplicationManager.DASHBOARD_PAGE);
            }
        });

    }



    /**
     * Get the messageId from Find Bid page one user click on view bid to retrieve data from db
     * @param data any data that is crucial to the pages for them to request the information that they need from the database
     */
    @Override
    public void update(String data) {

        this.messageId = new JSONObject(data).getString("messageId");
        this.userId = new JSONObject(data).getString("userId");
        HttpResponse<String> response = ApiRequest.get("/message/"+ this.messageId + "?fields=messages");

        // if retrieve success
        if (response.statusCode() == 200){

            this.removeAll();
            this.repaint();
            this.revalidate();
            createContent(new JSONObject(response.body()));

        } else {
            String msg = "Error: " + new JSONObject(response.body()).get("message");
            JOptionPane.showMessageDialog(new JFrame(), msg, "Bad request", JOptionPane.ERROR_MESSAGE);
        }


    }
}
