package views.student_bids;

import abstractions.ListenerLinkInterface;
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
import java.time.Instant;
import java.util.ArrayList;

// TODO: refactor time function into a class
// TODO: view all latest 5 contract not closed contract
// TODO: refactor createPanel function into a class for findallbid, seeallbid, viewcontract
public class ViewContract extends JPanel implements ObserverOutputInterface, ListenerLinkInterface {

    private JPanel contentPanel;
    private JScrollPane scrollPane;
    private JLabel activityTitle;
    private GridBagConstraints c;
    private JButton viewBidBtn, backBtn;
    private ArrayList<JButton> buttonArr;
    public String userId;
    private boolean isTutor;

    public  ViewContract() {
        this.setBorder(new EmptyBorder(2, 2, 2, 2));
        this.setLayout(new GridBagLayout());
        this.setBackground(new Color(245, 245, 220));
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

        // wrap contentPanel inside a scrollpane
        scrollPane = new JScrollPane();
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
        System.out.println(contracts.length());
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
                JLabel peopleLabel = new JLabel();
                if (isTutor) {
                    peopleLabel.setText( "Initiator: " + contract.getJSONObject("secondParty").get("givenName") + " " + contract.getJSONObject("secondParty").get("familyName"));

                } else {
                    peopleLabel.setText( "Tutor: " + contract.getJSONObject("firstParty").get("givenName") + " " + contract.getJSONObject("firstParty").get("familyName"));

                }
                contractPanelConstraint.gridy = 1;
                contractPanel.add(peopleLabel, contractPanelConstraint);

                JLabel additionalLabel = new JLabel();
                // for tutor to view all renewed contract by student that is pending signing
                if (contract.isNull("dateSigned")) {
                    additionalLabel.setText("Rate: " + contract.getJSONObject("lessonInfo").getString("rate"));
                } else { // for student to view latest 5 signed contract
                    additionalLabel.setText("Signed On: " + contract.getString("dateSigned"));
                }

                contractPanelConstraint.gridy = 2;
                contractPanel.add(additionalLabel, contractPanelConstraint);

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

        contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.setBackground(Color.lightGray);

        createContractPanels(contracts);
        scrollPane.setViewportView(contentPanel);

    }

    /**
     * Function to - filter contract to the latest 5 contract signed for student
     *             - filter renewed contract for tutor that hasn't been signed
     * @param contracts JSONArray containing all the contract
     * @return JSONArray of the latest 5 contract
     */
    private JSONArray filterContracts(JSONArray contracts){

        // check if user is tutor or student to filter the contract differently
        HttpResponse<String> response = ApiRequest.get("/user/" + this.userId);
        JSONObject user = new JSONObject(response.body());
        isTutor = user.getBoolean("isTutor");

        JSONArray returnArr = new JSONArray();

        // if tutor show all contract renewed by student that hasn't been signed by tutor
        if (isTutor){
            JSONArray activeContract = new JSONArray(user.getJSONObject("additionalInfo").optJSONArray("activeContract"));

            for(int i=0; i < activeContract.length(); i++) {
                JSONObject contract = new JSONObject(ApiRequest.get("/contract/" + activeContract.get(i)).body());
                returnArr.put(contract);
            }


        } else { // if student show latest 5 contract signed

            // get all the bid signed by the user
            for (int i=0; i < contracts.length(); i++){
                JSONObject contract = contracts.getJSONObject(i);
                if (contract.getJSONObject("secondParty").getString("id").equals(this.userId) && (!contract.isNull("dateSigned"))){
                    returnArr.put(contracts.get(i));
                }

            }

            // if less that 5 contract
            if (returnArr.length() <= 5){
                return  returnArr;
            }

            // if more than 5 filter the latest bid signed by user
            returnArr = InsertionSort(contracts);
            while (returnArr.length() > 5){
                returnArr.remove(0);
            }
        }


        return  returnArr;
    }

    /**
     * Using insertion sort to sort contracts by date
     * @param contracts The arrayList of contract
     */
    private JSONArray InsertionSort(JSONArray contracts) {

        for (int i = 1; i < contracts.length(); ++i) {
            JSONObject key = contracts.getJSONObject(i);
            Instant currDate = Instant.parse(key.getString("dateSigned"));
            int j = i - 1;

            // while date[] > currDate, move the date to left
            while (j >= 0 &&  currDate.compareTo(Instant.parse(contracts.getJSONObject(j).getString("dateSigned"))) > 0) {
                contracts.put(j+1, contracts.get(j));
                j = j - 1;
            }
            contracts.put(j + 1, key);
        }

        return contracts;
    }

    @Override
    public void addLinkListener(ActionListener actionListener) {
        if(buttonArr != null) {
            for (JButton btn: buttonArr) {
                btn.addActionListener(actionListener);
            }
        }
    }
}
