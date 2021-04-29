package application.bid_pages;

import api.ApiRequest;
import application.Application;
import application.ApplicationManager;
import controller.ObserverInputInterface;
import controller.ObserverOutputInterface;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.http.HttpResponse;

// TODO: show all bidder in open bid
// TODO: show check message instead of bid button if tutor already reply to a close bid

public class FindBidsDetail extends JPanel implements ObserverOutputInterface, ObserverInputInterface {

    private String bidId;
    private JLabel title, subjectLabel, name, rate, competency, noOfLesson, duration, startTime, day, preferredSession;
    private JButton closeBtn = new JButton("Buy Out");
    private JButton replyBtn = new JButton("Bid");
    private JButton backBtn;

    public FindBidsDetail() {}

    /**
     * Create the content to display the detail of the bid after user enter this page
     * @param bid the bid to display
     */
    void createContent(JSONObject bid){

        JSONObject initiator = bid.getJSONObject("initiator");
        JSONObject subject = bid.getJSONObject("subject");
        JSONObject additionalInfo = bid.getJSONObject("additionalInfo");

        this.setBorder(new EmptyBorder(15, 15,15,15));
        this.setLayout(new GridBagLayout());
        this.setBackground(Color.lightGray);
        this.setMinimumSize(new Dimension(this.getWidth(), this.getHeight()));
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
        c.gridy = this.getComponentCount();
        c.gridwidth = 3;
        c.gridx = 1;
        c.anchor = GridBagConstraints.PAGE_START;
        this.add(title, c);

        backBtn = new JButton("Back");
        c.gridy = 0;
        c.weightx = 0.0;
        c.gridwidth = 1;
        c.gridx = 0;
        c.anchor = GridBagConstraints.PAGE_START;
        this.add(backBtn, c);

        // TODO: can talk about this in design rationale, nonid to create a listener for this cuz very simple and wont change forever
        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ApplicationManager.loadPage(ApplicationManager.FIND_BID);
            }
        });

        subjectLabel = new JLabel("Subject: " + subject.get("name"));
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridwidth = 3;
        c.gridy = this.getComponentCount();
        c.anchor = GridBagConstraints.PAGE_START;
        this.add(subjectLabel, c);

        name = new JLabel("Name: " + initiator.get("givenName") +" " + initiator.get("familyName"));
        c.gridx = 0;
        c.gridwidth = 3;
        c.gridy = this.getComponentCount();
        c.anchor = GridBagConstraints.PAGE_START;
        this.add(name, c);

        // if rate is provided in the bid
        if (additionalInfo.has("rate")){
            rate = new JLabel("Rate: " + additionalInfo.get("rate"));
        } else {
            rate = new JLabel("Rate not provided");
        }
        c.gridy = this.getComponentCount();
        this.add(rate, c);

        // if competency is provided in the bid
        if (additionalInfo.has("rate")){
            competency = new JLabel("Competency: " + additionalInfo.get("minCompetency"));
        } else {
            competency = new JLabel("Competency not provided");
        }
        c.gridy = this.getComponentCount();
        this.add(competency, c);

        // if day is provided in the bid
        if (additionalInfo.has("day")){
            day = new JLabel("Preferred Day(s): " + additionalInfo.get("day"));
        } else {
            day = new JLabel("Preferred day(s) not provided");
        }
        c.gridy = this.getComponentCount();
        this.add(day, c);

        // if preferred session is provided in the bid
        if (additionalInfo.has("day")){
            preferredSession = new JLabel("Preferred no of lessons: " + additionalInfo.get("preferredSession") + " lessons per week");
        } else {
            preferredSession = new JLabel("Preferred sessions not provided");
        }
        c.gridy = this.getComponentCount();
        this.add(preferredSession, c);

        // if duration is provided in the bid
        if (additionalInfo.has("day")){
            duration = new JLabel("Duration: " + additionalInfo.get("duration") + " hours per lesson");
        } else {
            duration = new JLabel("Duration not provided");
        }
        c.gridy = this.getComponentCount();
        this.add(duration, c);

        // if preferred time is provided in the bid
        if (additionalInfo.has("day")){
            startTime = new JLabel("Preferred Time: " + additionalInfo.get("startTime"));
        } else {
            startTime = new JLabel("Preferred Time not provided");
        }
        c.gridy = this.getComponentCount();
        this.add(startTime, c);

        // add closeBid Button
        c.weighty = 0;
        c.weightx = 0.2;
        c.gridwidth = 2;
        c.gridx = 2;
        c.gridy = this.getComponentCount();
        c.anchor = GridBagConstraints.PAGE_END;
        // putting the bidId and tutorId for calling the BidClosingListener
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("bidId", bidId);
        jsonObject.put("tutorId", "");
        closeBtn.setName(jsonObject.toString());
        this.add(closeBtn, c);

        // add replyBid Button
        c.weighty = 0;
        c.weightx = 0.2;
        c.gridwidth = 2;
        c.gridx = 0;
        c.anchor = GridBagConstraints.PAGE_START;
        String data = new JSONObject().put("bidId", this.bidId).put("userId", initiator.get("id")).toString();
//        System.out.println(data);
        replyBtn.setName(data);
        this.add(replyBtn, c);
    }



    /**
     * Get the bidId from Find Bid page one user click on view bid to retrieve data from db
     * @param data any data that is crucial to the pages for them to request the information that they need from the database
     */
    @Override
    public void update(String data) {
        this.bidId = data;
        HttpResponse<String> response = ApiRequest.get("/bid/"+ this.bidId);

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

    @Override
    public JSONObject retrieveInputs() {
        return null;
    }

    @Override
    public void addActionListener(ActionListener actionListener) {
        this.closeBtn.addActionListener(actionListener);
    }


    public void addReplyBidListener(ActionListener listener) {
        this.replyBtn.addActionListener(listener);
    }
}
