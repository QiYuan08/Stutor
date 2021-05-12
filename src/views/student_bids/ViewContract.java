package views.student_bids;

import abstractions.ObserverOutputInterface;
import org.json.JSONArray;
import org.json.JSONObject;
import services.ApiRequest;
import services.ViewManagerService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.http.HttpResponse;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

// TODO: refactor time function into a class
// TODO: refactor createPanel function into a class for findallbid, seeallbid, viewcontract
public class ViewContract extends JPanel implements ObserverOutputInterface {

    private JPanel contentPanel;
    private JScrollPane scrollPane;
    private JLabel activityTitle;
    private GridBagConstraints c;
    private JButton viewBidBtn, backBtn;
    private ArrayList<JButton> buttonArr;
    private String userId;

    public  ViewContract() {
        this.setBorder(new EmptyBorder(2, 2, 2, 2));
        this.setLayout(new GridBagLayout());
        this.setBackground(new Color(245, 245, 220));
        contentPanel = new JPanel();
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(1,3,1,3); //spacing between each bids

        backBtn = new JButton("Back");
        c.gridy = 0;
        c.gridx = 0;
        c.weightx = 0.1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.anchor = GridBagConstraints.PAGE_START;
        this.add(backBtn, c);

        activityTitle = new JLabel("Student Contracts");
        activityTitle.setHorizontalAlignment(JLabel.CENTER);
        activityTitle.setVerticalAlignment(JLabel.TOP);
        activityTitle.setFont(new Font("Bahnschrift", Font.BOLD, 20));
        c.gridx = 2;
        c.gridy = 0;
        c.gridheight = 1;
        c.weightx = 1;
        c.weighty = 0.2;
        c.gridwidth = 4;
        c.anchor = GridBagConstraints.NORTH;
        this.add(activityTitle, c);

        contentPanel.setLayout(new GridBagLayout());

        // wrap contentPanel inside a scrollpane
        scrollPane = new JScrollPane(contentPanel);
        contentPanel.setBackground(Color.lightGray);
        c.gridy = 2;
        c.weightx = 1;
        c.weighty = 1;
        c.gridwidth = 7;
        c.gridheight = 30;
        c.gridx = 0;
        c.anchor = GridBagConstraints.CENTER;
        this.add(scrollPane, c);

        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ViewManagerService.loadPage(ViewManagerService.DASHBOARD_PAGE);
            }
        });
    }

    /**
     * Function to create all the previous contract when update functions are called
     * @param contracts JSONArray of all the contract from the API
     */
    private void createContractPanels(JSONArray contracts){

        buttonArr = new ArrayList<>(); // array to store all button for each bidPanel

        // create a jPanel for each bids available
        if (contracts.length() > 0) {
            for (int i=0; i < contracts.length(); i++){

                JSONObject contract = contracts.getJSONObject(i);

                // create the panel for each contract item
                JPanel contractPanel = new JPanel();
                GridBagConstraints contractPanelConstraint = new GridBagConstraints();
                contractPanelConstraint.fill = GridBagConstraints.HORIZONTAL;
                contractPanelConstraint.weightx = 1;
                contractPanelConstraint.insets = new Insets(1,2,1,2);
                contractPanel.setLayout(new GridBagLayout());
                contractPanel.setBackground(Color.lightGray);
                contractPanel.setMinimumSize(new Dimension(100, 120));
                contractPanel.setMaximumSize(new Dimension(100, 120));

                // add a description jlabel
                contractPanelConstraint.gridx = 0;
                contractPanelConstraint.gridy = 0;
                contractPanelConstraint.gridwidth = 5;
                contractPanelConstraint.anchor = GridBagConstraints.WEST;
                JLabel bidLabel = new JLabel();
                bidLabel.setText(contract.getJSONObject("subject").getString("name"));
                contractPanel.add(bidLabel, contractPanelConstraint);

                // type jlabel
                JLabel tutorLabel = new JLabel();
                tutorLabel.setText( "Tutor: " + contract.getJSONObject("firstParty").get("givenName") + " " + contract.getJSONObject("firstParty").get("familyName"));
                contractPanelConstraint.gridy = 1;
                contractPanel.add(tutorLabel, contractPanelConstraint);

                // initiator jlabel
                JLabel expireLabel = new JLabel();
                expireLabel.setText("Expired On: " + contract.getString("expiryDate"));
                contractPanelConstraint.gridy = 2;
                contractPanel.add(expireLabel, contractPanelConstraint);

                // add view detail button
                contractPanelConstraint.gridy = 0;
                contractPanelConstraint.gridx = 6;
                contractPanelConstraint.gridwidth = 1;
                contractPanelConstraint.gridheight = 2;
                contractPanelConstraint.weightx = 0.2;
                viewBidBtn = new JButton("View Bid");

                // set button name to bidId and userId for ClosedBidResponse class to close Bid
                JSONObject btnData = new JSONObject();
                btnData.put("contractId", contract.get("id"));
                btnData.put("userId", this.userId);
                viewBidBtn.setName(btnData.toString());
                buttonArr.add(viewBidBtn); // add the button into button array
                contractPanel.add(viewBidBtn, contractPanelConstraint);

                c.gridx = 0;
                c.gridy = contentPanel.getComponentCount() - 1;
                c.gridwidth = 4;
                c.gridheight = 1;
                contentPanel.add(contractPanel, c);
            }

        } else { // if not relevant bid found
            JPanel contractPanel = new JPanel();
            JLabel noContract = new JLabel("No Contract Found");
            activityTitle.setHorizontalAlignment(JLabel.CENTER);
            activityTitle.setVerticalAlignment(JLabel.CENTER);
            activityTitle.setFont(new Font("Bahnschrift", Font.BOLD, 20));
            contractPanel.add(noContract);
            c.gridx = 1;
            c.gridwidth = 4;
            c.gridheight = 1;
            c.gridy = contentPanel.getComponentCount();
            c.anchor = GridBagConstraints.CENTER;
            contentPanel.add(contractPanel);
        }
    }

    /**
     *
     * @param data receive userId from dashboard page
     */
    @Override
    public void update(String data) {
        this.userId = data;

        HttpResponse<String> response = ApiRequest.get("/contract");
        JSONArray contracts = filterContracts(new JSONArray(response.body()));
        createContractPanels(contracts);

    }

    /**
     * Function to filter contract to the latest 5 contract expired/terminated by user
     * @param contracts JSONArray containing all the contract
     * @return JSONArray of the latest 5 contract
     */
    private JSONArray filterContracts(JSONArray contracts){

        JSONArray returnArr = new JSONArray();
        // get the latest expired 5 bids
        for (int i=0; i < contracts.length(); i++) {
            JSONObject contract = contracts.getJSONObject(i);
            Instant bidStart = Instant.parse(contract.getString("expiryDate"));
            Timestamp ts = Timestamp.from(ZonedDateTime.now().toInstant());
            Instant now = ts.toInstant();

            // if bid haven't expired
            if (!contract.isNull("terminationDate") || now.compareTo(bidStart) > 0) {
                // check if this contract belongs to this student
                if (contract.getJSONObject("secondParty").getString("id").equals(this.userId)) {
                    returnArr.put(contracts.get(i));
                }
            }
        }

        return  returnArr;
    }
}
