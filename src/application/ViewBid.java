package application;

import api.ApiRequest;
import controller.ObserverInputInterface;
import controller.ObserverOutputInterface;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.http.HttpResponse;

public class ViewBid extends JPanel implements ObserverOutputInterface, ObserverInputInterface {

    private String bidId;
    private JLabel name, rate, competency, session;
    private JButton closeBtn, replyBtn;

    ViewBid() {

    }

    /***
     * Create the content to display the detail of the bid after user enter this page
     */
    void createContent(JSONObject bid){

        this.setBorder(new EmptyBorder(2, 2, 2, 2));
        JSONObject initiator = bid.getJSONObject("initiator");
        JSONObject subject = bid.getJSONObject("subject");
        JSONObject additionalInfo = bid.getJSONObject("additionalInfo");

        this.setLayout(new GridBagLayout());
        this.setBackground(Color.cyan);
        this.setMinimumSize(new Dimension(this.getWidth(), this.getHeight()));
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1;
//        c.weighty = 1;
        c.insets = new Insets(1, 1, 1, 1);
        c.fill = GridBagConstraints.HORIZONTAL;
        // innner panel for detail
        c.weightx = 0.5;
        c.weighty = 0.5;

        name = new JLabel("Name: " + initiator.get("givenName") +" " + initiator.get("familyName"));
        c.gridx = 0;
        c.gridwidth = 3;
        c.gridy = 0;
        c.anchor = GridBagConstraints.PAGE_START;
        name.setBackground(Color.BLUE);
        this.add(name, c);

        // if rate is provided in the bid
        if (additionalInfo.has("rate")){
            rate = new JLabel("Rate: " + additionalInfo.get("rate"));
        } else {
            rate = new JLabel("Rate not provided");
        }
        c.gridy = 1;
        this.add(rate, c);

        // if competency is provided in the bid
        if (additionalInfo.has("rate")){
            competency = new JLabel("Rate: " + additionalInfo.get("competency"));
        } else {
            competency = new JLabel("Competency not provided");
        }
        c.gridy = 2;
        this.add(competency, c);

        // if session is provided in the bid
        if (additionalInfo.has("rate")){
            session = new JLabel("Rate: " + additionalInfo.get("session"));
        } else {
            session = new JLabel("Session not provided");
        }
        c.gridy = 3;
        this.add(session, c);
    }



    /**
     * Get the bidId from All Bid page one user click on view bid to retrieve data from db
     * @param data any data that is crucial to the pages for them to request the information that they need from the database
     */
    @Override
    public void update(String data) {
        this.bidId = data;
        HttpResponse<String> response = ApiRequest.get("/bid/"+ this.bidId);

        // if retrieve success
        if (response.statusCode() == 200){

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

    /***
     * Add action listener for close bid and reply bid button
     * @param actionListener
     */
    @Override
    public void addActionListener(ActionListener actionListener) {

    }
}
