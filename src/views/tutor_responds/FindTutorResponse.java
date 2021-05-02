package views.tutor_responds;

import api.ApiRequest;
import services.ViewManagerService;
import interfaces.ObserverOutputInterface;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.http.HttpResponse;

public class FindTutorResponse extends JPanel implements ObserverOutputInterface {
    private String bidId, userId;
    private JLabel title, name, rate, competency, duration, startTime, day, preferredSession;
    private JButton backBtn;
    private JPanel detailPane, btnPane;
    private JScrollPane scrollPane;
    private GridBagConstraints mainConst;

    public FindTutorResponse() {
        this.setLayout(new GridBagLayout());
        mainConst = new GridBagConstraints();

    }

    /**
     * Create the content to display the detail of the bid after user enter this page
     * @param bid the bid to display
     */
    void createContent(JSONObject bid){

        JSONObject initiator = bid.getJSONObject("poster");
        JSONObject additionalInfo = bid.getJSONObject("additionalInfo");

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
            day = new JLabel("Day not provided");
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
        btnPane.setLayout(new GridLayout(1,1));
        btnPane.setBorder(new EmptyBorder(10, 10,10,10));

        // add back Button
        backBtn = new JButton("Back");
        btnPane.add(backBtn);

        // add btnPanel into this
        mainConst.weighty = 0.2;
        mainConst.weightx = 0.2;
        mainConst.gridheight = 2;
        mainConst.gridx = 0;
        mainConst.gridy = 30;
        c.gridwidth = 10;
        this.add(btnPane, mainConst);

        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ViewManagerService.loadPage(ViewManagerService.DASHBOARD_PAGE);
            }
        });

    }

    /**
     * Get the bidId from Find Bid page one user click on view bid to retrieve data from db
     * @param data any data that is crucial to the pages for them to request the information that they need from the database
     */
    @Override
    public void update(String data) {

        this.bidId = new JSONObject(data).getString("bidId");
        this.userId = new JSONObject(data).getString("userId");
        HttpResponse<String> response = ApiRequest.get("/message/"+ this.bidId + "?fields=messages");

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
