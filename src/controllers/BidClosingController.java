package controllers;

import org.json.JSONArray;
import services.ApiRequest;
import abstractions.Publisher;
import services.ViewManagerService;
import abstractions.ObserverInputInterface;
import abstractions.ObserverOutputInterface;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.http.HttpResponse;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Notifies view components when a bid is closed, making them update themselves to show the correct bids and tutorials (contracts)
 */
public class BidClosingController extends Publisher implements ObserverOutputInterface, ActionListener {

    private String userId; // needed to update other bid view
    private String bidId, tutorId, messageId;

    public BidClosingController() {
        super();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ObserverInputInterface inputPage;
        if (e.getSource() instanceof JButton) { // triggered by a button
            JButton btn = (JButton) e.getSource();
            inputPage = (ObserverInputInterface) btn.getParent();
        } else { // triggered by ExpireBidService
            inputPage = (ObserverInputInterface) e.getSource();
        }
        JSONObject jsonBid = inputPage.retrieveInputs();
        closeBid(jsonBid);
        if (this.userId == null) {notifySubscribers(this.userId);}
    }

    /***
     * Function for a tutor to close a bid immediately if he agree to the student's bid
     */
    private void closeBid(JSONObject bidInfo) {
        bidId = bidInfo.getString("bidId");
        tutorId = bidInfo.getString("tutorId");
        messageId = bidInfo.getString("messageId");
        boolean hasExpired = bidInfo.getBoolean("hasExpired");

        HttpResponse<String> bidResponse = ApiRequest.get("/bid/" + bidId + "?fields=messages");
        JSONObject bid = new JSONObject(bidResponse.body());

        Timestamp ts = Timestamp.from(ZonedDateTime.now().toInstant());
        Instant now = ts.toInstant();

        JSONObject closeDate = new JSONObject();
        closeDate.put("dateClosedDown", now);
        HttpResponse<String> bidCloseDownResponse = ApiRequest.post("/bid/" + bidId + "/close-down", closeDate.toString()); // pass empty json object since this API call don't need it

        if (bidCloseDownResponse.statusCode() == 200) {
            if (hasExpired) {
                if (bid.getJSONObject("initiator").getString("id").equals(userId)) { // only shows expire message to the correct user
                    JOptionPane.showMessageDialog(new JFrame(), "Bid expired at " + now, "Bid Expired", JOptionPane.INFORMATION_MESSAGE);
                }
                return;
            } else {
                postContract(bid);
            }
        } else {
            String msg = "Bid not closed down: Error " + bidCloseDownResponse.statusCode();
            JOptionPane.showMessageDialog(new JFrame(), msg, "Bad request", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void signContract(JSONObject contract) {
        JSONObject dateSigned = new JSONObject();
        Timestamp ts = Timestamp.from(ZonedDateTime.now().toInstant());
        Instant now = ts.toInstant();
        dateSigned.put("dateSigned", now);
        HttpResponse<String> contractSignResponse = ApiRequest.post("/contract/" + contract.getString("id") + "/sign", dateSigned.toString());
        String msg;

        if (contractSignResponse.statusCode() == 200) {
            msg = "Bid closed successfully and contract created at " + now;
            JOptionPane.showMessageDialog(new JFrame(), msg, "Bid Closed Successfully", JOptionPane.INFORMATION_MESSAGE);
            if (tutorId.equals("")) {
                ViewManagerService.loadPage(ViewManagerService.DASHBOARD_PAGE);
            }
        } else {
            msg = "Contract not signed: Error " + contractSignResponse.statusCode();
            JOptionPane.showMessageDialog(new JFrame(), msg, "Bad request", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void postContract(JSONObject bid) {
        JSONObject contract = new JSONObject();
        if (tutorId.equals("")) { // buyout action (there could be no responses for the bid when the tutor buys it out)
            contract.put("firstPartyId", userId);
            contract.put("secondPartyId", bid.getJSONObject("initiator").getString("id"));
            contract.put("lessonInfo", bid.getJSONObject("additionalInfo"));
        } else { // a confirm bid action from the user or ExpireBidService chooses the last tutor as the winner (has response)
            contract.put("firstPartyId", tutorId);
            contract.put("secondPartyId", bid.getJSONObject("initiator").getString("id"));
            JSONObject message = new JSONObject(ApiRequest.get("/message/" + messageId).body());
            contract.put("lessonInfo", message.getJSONObject("additionalInfo"));
        }
        Timestamp ts = Timestamp.from(ZonedDateTime.now().toInstant());
        Instant now = ts.toInstant();
        contract.put("dateCreated", now);
        LocalDateTime time = LocalDateTime.ofInstant(ts.toInstant(), ZoneOffset.ofHours(0));
        time = time.plus(1, ChronoUnit.YEARS); // contract expires after a year
        Instant output = time.atZone(ZoneOffset.ofHours(0)).toInstant();
        Timestamp expiryDate = Timestamp.from(output);
        contract.put("subjectId", bid.getJSONObject("subject").getString("id"));
        contract.put("expiryDate", expiryDate);
        contract.put("paymentInfo", new JSONObject());
        contract.put("additionalInfo", new JSONObject());
        HttpResponse<String> contractResponse = ApiRequest.post("/contract", contract.toString());

        if (contractResponse.statusCode() == 201) {
            contract = new JSONObject(contractResponse.body());
            patchUser(contract.getJSONObject("secondParty").getString("id"), contract.getString("id"), true);
            signContract(contract);
        } else {
            String msg = "Contract not posted: Error " + contractResponse.statusCode();
            JOptionPane.showMessageDialog(new JFrame(), msg, "Bad request", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Method to add pending contract into tutor/student additionalInfo to sign later on
     * @param userId id of the tutor
     */
    private void patchUser(String userId, String contractId, boolean isStudent){
        JSONObject user = new JSONObject(ApiRequest.get("/user/" + userId).body());
        JSONObject additionalInfo = user.getJSONObject("additionalInfo");

        // if user has previously pending contract
        if (additionalInfo.has("activeContract")){

            JSONArray activeContract = additionalInfo.getJSONArray("activeContract");

            if (isStudent) {
                if (activeContract.length() == 5){ // only save latest 5 signed contract
                    activeContract.remove(0); // remove the oldest contract
                    activeContract.put(contractId); // add latest contract
                }
            } else { //tutor save as many unsigned contract
                activeContract.put(contractId);

            }
            // update additionalInfo with new active Contract
            additionalInfo.remove("activeContract");
            additionalInfo.put("activeContract", activeContract);

        } else {
            JSONArray activeContract = new JSONArray();
            activeContract.put(contractId);

            // update additionalInfo with new active Contract
            additionalInfo.put("activeContract", activeContract);
        }
        ApiRequest.put("/user/" + userId, user.toString());

    }

    @Override
    public void update(String data) {
        this.userId = data;
    }

}

