package views.student_bids;

import api.ApiRequest;
import interfaces.ListenerLinkInterface;
import services.ViewManagerService;
import interfaces.ObserverInputInterface;
import interfaces.ObserverOutputInterface;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.http.HttpResponse;

public class SeeTutorResponse extends JPanel implements ObserverOutputInterface, ObserverInputInterface, ListenerLinkInterface {

    private String messageId, userId, bidId, tutorId;
    private JLabel title, name, rate, competency, duration, startTime, day, preferredSession;
    private JButton backBtn;
    private  JButton messageBtn = new JButton("Message");
    private JButton confirmBtn = new JButton("Confirm Bid");

    public SeeTutorResponse() {
        this.setBorder(new EmptyBorder(15, 15,15,15));
        this.setLayout(new GridBagLayout());
        this.setBackground(new Color(255, 252, 252));
    }

    /**
     * Create the content to display the detail of the message after user enter this page
     * @param message the message to display
     */
    void createContent(JSONObject message){

        JSONObject initiator = message.getJSONObject("poster");
        JSONObject additionalInfo = message.getJSONObject("additionalInfo");
        this.bidId = message.getString("bidId");

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
        c.gridy = 0;
        c.gridwidth = 3;
        c.gridx = 1;
        c.anchor = GridBagConstraints.PAGE_START;
        this.add(title, c);

        name = new JLabel("Name: " + initiator.get("givenName") +" " + initiator.get("familyName"));
        c.gridx = 0;
        c.gridwidth = 3;
        c.gridy = 1;
        c.anchor = GridBagConstraints.PAGE_START;
        this.add(name, c);

        // if rate is provided in the message
        if (additionalInfo.has("rate")){
            rate = new JLabel("Rate: " + additionalInfo.get("rate"));
        } else {
            rate = new JLabel("Rate not provided");
        }
        c.gridy = 2;
        this.add(rate, c);

        // if competency is provided in the message
        if (additionalInfo.has("minCompetency")){
            competency = new JLabel("Minimum competency: " + additionalInfo.get("minCompetency"));
        } else {
            competency = new JLabel("Competency not provided");
        }
        c.gridy = 3;
        this.add(competency, c);

        // if day is provided in the message
        if (additionalInfo.has("day")){
            day = new JLabel("Preferred Day(s): " + additionalInfo.get("day"));
        } else {
            day = new JLabel("Day not provided");
        }
        c.gridy = 4;
        this.add(day, c);

        // if preferred session is provided in the message
        if (additionalInfo.has("preferredSession")){
            preferredSession = new JLabel("Preferred no of lessons: " + additionalInfo.get("preferredSession") + " lessons per week");
        } else {
            preferredSession = new JLabel("Preferred sessions not provided");
        }
        c.gridy = 5;
        this.add(preferredSession, c);

        // if duration is provided in the message
        if (additionalInfo.has("duration")){
            duration = new JLabel("Duration: " + additionalInfo.get("duration") + " hours per lesson");
        } else {
            duration = new JLabel("Duration not provided");
        }
        c.gridy = 6;
        this.add(duration, c);

        // if start time is provided in the message
        if (additionalInfo.has("startTime")){
            startTime = new JLabel("Start Time: " + additionalInfo.get("startTime"));
        } else {
            startTime = new JLabel("Start Time not provided");
        }
        c.gridy = 7;
        this.add(startTime, c);

        HttpResponse<String> response = ApiRequest.get("/bid/" + message.getString("bidId"));
        JSONObject bid = new JSONObject(response.body());

        c.gridheight = 1;
        c.gridx = 2;
        c.gridy = 8;
        c.gridwidth = 1;

        if (bid.isNull("dateClosedDown")) {
            if (bid.getString("type").equals("open")){  // if its open message
                confirmBtn.setName(this.bidId);
                this.add(confirmBtn, c);

            } else if (bid.getString("type").equals("close")) {
                confirmBtn.setName(this.bidId);
                this.add(confirmBtn, c);
                JSONObject data = new JSONObject().put("userId", this.userId).put("bidId", this.bidId);
                messageBtn.setName(data.toString());
                c.gridx = 1;
                this.add(messageBtn, c);

            }

        }

        backBtn = new JButton("Back");
        c.gridy = 0;
        c.gridx = 0;
        c.gridheight = 1;
        c.gridwidth = 1;
        this.add(backBtn, c);

        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ViewManagerService.loadPage(ViewManagerService.DASHBOARD_PAGE);
            }
        });

    }

//    public void addMessageBtnListener (ActionListener listener){
//        this.messageBtn.addActionListener(listener);
//    }


    /**
     * Get the messageId from Find Bid page one user click on view bid to retrieve data from db
     * @param data any data that is crucial to the pages for them to request the information that they need from the database
     */
    @Override
    public void update(String data) {

        this.messageId = new JSONObject(data).getString("messageId");
        this.userId = new JSONObject(data).getString("userId");
        HttpResponse<String> response = ApiRequest.get("/message/"+ this.messageId);
        tutorId = new JSONObject(response.body()).getJSONObject("poster").getString("id");

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
        JSONObject bidInfo = new JSONObject();
        bidInfo.put("bidId", bidId);
        bidInfo.put("messageId", messageId);
        bidInfo.put("tutorId", tutorId);
        bidInfo.put("hasExpired", false);
        return bidInfo;
    }

    @Override
    public void addActionListener(ActionListener actionListener) {
        this.confirmBtn.addActionListener(actionListener);
    }

    @Override
    public void addLinkListener(ActionListener actionListener) {
        this.messageBtn.addActionListener(actionListener);
    }
}
