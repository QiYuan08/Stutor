package application.bid;

import event_manager.EventSubscriber;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.AllBidUtil;
import utils.ResponseCloseBidUtil;
import utils.ResponseOpenBidUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AllBid extends JPanel implements EventSubscriber {

    JPanel contentPanel = new JPanel();
    JScrollPane scrollPane;
    JLabel activityTitle;
    JSONArray bids;
    GridBagConstraints c;

    public AllBid() {
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

//        // add content panel into main panel
//        contentPanel = new JPanel();
//        contentPanel.setPreferredSize(new Dimension(100, 200));
//        contentPanel.setLayout(new GridBagLayout());
//        contentPanel.setBackground(Color.RED);
//        c.gridx = 0;
//        c.gridy = 1;
//        c.gridheight = 3;
//        c.gridwidth = 1;
//        c.anchor = GridBagConstraints.PAGE_END;
//        c.fill = GridBagConstraints.HORIZONTAL;
////        c.fill = GridBagConstraints.VERTICAL;
////        this.add(contentPanel, c);

        // wrap contentPanel inside a scrollpane

        scrollPane = new JScrollPane(contentPanel);
        this.add(scrollPane, c);
//        this.add(scrollPane, c);

        // add all the bids into content pane
        // need a controller class to sync all the bid

        bids = new JSONArray();
        bids.put(new JSONObject().put("type", "open").put("id", "123456"));
        bids.put(new JSONObject().put("type", "close").put("id", "987654"));

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
            bidPanelConstraint.gridwidth = 3;
            bidPanelConstraint.anchor = GridBagConstraints.WEST;
            JLabel bidLabel = new JLabel();
            bidLabel.setText("Description");
            bidPanel.add(bidLabel, bidPanelConstraint);

            // add view detail button
            bidPanelConstraint.gridx = 3;
            bidPanelConstraint.gridwidth = 1;
            JButton viewBidBtn = new JButton("View Bid");
            viewBidBtn.setName(bid.get("id").toString()); // give a unique name to a button to distinguish them
            viewBidBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JButton thisBtn = (JButton) e.getSource();
                    JOptionPane.showMessageDialog(new JFrame(), "Testing","Bid ID:" + thisBtn.getName(), JOptionPane.INFORMATION_MESSAGE);
                }
            });
            bidPanel.add(viewBidBtn, bidPanelConstraint);

            // if this is an open bid add close bid button
            if (bid.get("type").equals("open")){
                bidPanelConstraint.anchor = GridBagConstraints.LINE_END;
                bidPanelConstraint.gridx = 4;
                bidPanelConstraint.gridwidth = 1;
                JButton closeBidBtn = new JButton("Close Bid");
                closeBidBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JButton thisBtn = (JButton) e.getSource();
                        AllBidUtil util = new AllBidUtil();
                        util.closeBid(thisBtn.getName());
                    }
                });
                closeBidBtn.setName(bid.get("id").toString()); // give a unique name to a button to distinguish them
                bidPanel.add(closeBidBtn, bidPanelConstraint);
            }

            c.gridx = 0;
            c.gridy = contentPanel.getComponentCount();
            c.gridwidth = 1;
            c.gridheight = 1;
            contentPanel.add(bidPanel, c);

        }
    }



    @Override
    public void update() {
//        this.bids = bids;
//        contentPanel.removeAll();
//        contentPanel.revalidate();
//        contentPanel.revalidate();
//        createContent();
    }
}
