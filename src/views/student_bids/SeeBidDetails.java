package views.student_bids;

import api.ApiRequest;
import interfaces.ListenerLinkInterface;
import services.ViewManagerService;
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

/**
 * Class for user to see the detail of each bids that they have opened
 */
public class SeeBidDetails extends JPanel implements ObserverOutputInterface, ListenerLinkInterface {

    private String bidId, userId;
    private JLabel title, subjectLabel, name, rate, competency, duration, startTime, day, preferredSession, bidderLabel;
    private JButton closeBtn = new JButton("Buy Out");
    private JButton replyBtn = new JButton("Bid");
    private JButton backBtn, viewBidBtn;
    private JPanel detailPane;
    private JScrollPane scrollPane;
    ArrayList<JButton> buttonArr;
    private GridBagConstraints mainConst;

    public SeeBidDetails(){
        this.setLayout(new GridBagLayout());
        mainConst = new GridBagConstraints();
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

        backBtn = new JButton("Back");
        c.gridy = 0;
        c.weightx = 0.0;
        c.gridwidth = 1;
        c.gridx = 0;
        c.anchor = GridBagConstraints.PAGE_START;
        detailPane.add(backBtn, c);

        backBtn.addActionListener(new ActionListener() {
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
            preferredSession = new JLabel("Preferred no of lessons: " + additionalInfo.get("preferredSession") + " lessons per week");
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

        // add scrollPane into mainPanel
        mainConst.weighty = 1;
        mainConst.weightx = 1;
        mainConst.gridheight = 8;
        mainConst.gridx = 0;
        c.gridwidth = 10;
        mainConst.gridy = 0;
        mainConst.fill = GridBagConstraints.BOTH;
//        this.add(detailPane, mainConst);

        buttonArr = new ArrayList<>();
        System.out.println(bid);
        // create a Panel to show each message replied by tutor
        if (messages.length() > 0){

            bidderLabel = new JLabel("Bidders");
            bidderLabel.setHorizontalAlignment(JLabel.CENTER);
            bidderLabel.setFont(new Font("Bahnschrift", Font.BOLD, 20));
            detailPane.add(bidderLabel, c);

            for (int i=0; i < messages.length(); i++){
                JSONObject message = messages.getJSONObject(i);

                if (!(message.getJSONObject("additionalInfo").isEmpty())){ // show only non 'message' message
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
                    System.out.println(message);
                    rate.setText("Rate: " + message.getJSONObject("additionalInfo").get("rate") + " dollars per hour");
                    bidPanelConstraint.gridy = 1;
                    bidPanel.add(rate, bidPanelConstraint);

                    // add view detail button
                    bidPanelConstraint.gridy = 0;
                    bidPanelConstraint.gridx = 6;
                    bidPanelConstraint.gridwidth = 1;
                    bidPanelConstraint.gridheight = 2;
                    bidPanelConstraint.weightx = 0.2;
                    viewBidBtn = new JButton("View Bid");

                    // set button name to bidId and userId for ClosedBidResponse class to close Bid
                    JSONObject btnData = new JSONObject();
                    btnData.put("messageId", message.get("id"));
                    btnData.put("userId", this.userId);
                    viewBidBtn.setName(btnData.toString());
                    buttonArr.add(viewBidBtn); // add the button into button array
                    bidPanel.add(viewBidBtn, bidPanelConstraint);

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

    }



    /**
     * Get the bidId from See Bid page one user click on view bid to retrieve data from db
     * @param data userId and bidId
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
            createContent(new JSONObject(response.body()));

        } else {
            String msg = "Error: " + new JSONObject(response.body()).get("message");
            JOptionPane.showMessageDialog(new JFrame(), msg, "Bad request", JOptionPane.ERROR_MESSAGE);
        }


    }

    @Override
    public void addLinkListener(ActionListener actionListener) {
        if (buttonArr != null){ // check when the page first load during apps startup
            for (JButton btn: buttonArr){
                btn.addActionListener(actionListener);
            }
        }

    }
}
