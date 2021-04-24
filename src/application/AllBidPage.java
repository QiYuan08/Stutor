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

        activityTitle = new JLabel("User Login");
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

        createContent();
    }

    /***
     * Method to create all the bid available in the db and show it to the user inside the contentPanel
     */
    private void createContent() {

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

//            viewBidBtn.addActionListener(new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    JButton thisBtn = (JButton) e.getSource();
//                    Application.getBidEventManager().notify(thisBtn.getName());
//                    Application.loadPage(Application.RESPONSEOPENBID);
//                }
//            });
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

    public void setViewBidListener(ActionListener listener){
        viewBidBtn.addActionListener(listener);
    }

    /**
     * Update the bids inside this panels whenever this page is load
     * @param data The user id that are currently using this page
     */
    @Override
    public void update(String data) {

        // get all thid bid other than the one the user created
        HttpResponse<String> response = ApiRequest.get("/bid");
        bids = new JSONArray(response.body());
        for (int i=0; i < bids.length(); i++){
            JSONObject bid = bids.getJSONObject(i);
            JSONObject initiator = bid.getJSONObject("initiator");

            // if the poster of the bid is the tutor itself don't show the bid
            if (initiator.get("id").equals(data)){
                bids.remove(i);
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

    @Override
    public void addActionListener(ActionListener actionListener) {
        viewBidBtn.addActionListener(actionListener);
    }
}
