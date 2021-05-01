package listeners;

import api.ApiRequest;
import application.ApplicationManager;
import org.json.JSONArray;
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

public class BidClosingListener implements ObserverOutputInterface, ActionListener {

    private ApplicationController applicationController;
    private String userId; // needed to update other bid view
    String bidId, tutorId, messageId;

    public BidClosingListener(ApplicationController applicationController) {
        this.applicationController = applicationController;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ObserverInputInterface inputPage;
        if (e.getSource() instanceof JButton) {
            JButton btn = (JButton) e.getSource();
            System.out.println(btn.getParent().getClass());
            inputPage = (ObserverInputInterface) btn.getParent();
        } else {
            inputPage = (ObserverInputInterface) e.getSource();
        }
        JSONObject jsonBid = inputPage.retrieveInputs();
        closeBid(jsonBid);
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
//        if (!bid.isNull("dateClosedDown")) {return;} // robustness check to not close the same bid twice

        Timestamp ts = Timestamp.from(ZonedDateTime.now().toInstant());
        Instant now = ts.toInstant();

        JSONObject closeDate = new JSONObject();
        closeDate.put("dateClosedDown", now);
        HttpResponse<String> bidCloseDownResponse = ApiRequest.post("/bid/" + bidId + "/close-down", closeDate.toString()); // pass empty json object since this API call don't need it

        if (bidCloseDownResponse.statusCode() == 200) {
            if (hasExpired) {
                JOptionPane.showMessageDialog(new JFrame(), "Bid expired at " + now, "Bid Expired", JOptionPane.INFORMATION_MESSAGE);
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
            msg = "Bid closed successfully at " + now;
            JOptionPane.showMessageDialog(new JFrame(), msg, "Bid Closed Successfully", JOptionPane.INFORMATION_MESSAGE);
            applicationController.notifySubscribers(this.userId);
            if (tutorId.equals("")) {
                ApplicationManager.loadPage(ApplicationManager.DASHBOARD_PAGE);
            }
        } else {
            msg = "Contract not signed: Error " + contractSignResponse.statusCode();
            JOptionPane.showMessageDialog(new JFrame(), msg, "Bad request", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void postContract(JSONObject bid) {
        JSONObject contract = new JSONObject();
        if (tutorId.equals("")) { // buyout action
            contract.put("firstPartyId", userId);
            contract.put("secondPartyId", bid.getJSONObject("initiator").getString("id"));
            contract.put("lessonInfo", bid.getJSONObject("additionalInfo"));
        } else {
            contract.put("firstPartyId", tutorId);
            contract.put("secondPartyId", bid.getJSONObject("initiator").getString("id"));

            JSONObject message;
            if (messageId.equals("")) { // bid closes automatically from timer with a winning bidder
                JSONArray messages = bid.getJSONArray("messages");
                message = new JSONObject(messages.getJSONObject(messages.length() - 1));
            } else { // student confirms the winning response (message)
                message = new JSONObject(ApiRequest.get("/message/" + messageId).body());
            }
            contract.put("lessonInfo", message.getJSONObject("additionalInfo"));
        }
        Timestamp ts = Timestamp.from(ZonedDateTime.now().toInstant());
        Instant now = ts.toInstant();
        LocalDateTime time = LocalDateTime.ofInstant(ts.toInstant(), ZoneOffset.ofHours(0));
        time = time.plus(1, ChronoUnit.YEARS);
        Instant output = time.atZone(ZoneOffset.ofHours(0)).toInstant();
        Timestamp expiryDate = Timestamp.from(output);
        contract.put("subjectId", bid.getJSONObject("subject").getString("id"));
        contract.put("expiryDate", expiryDate);
        contract.put("dateCreated", now);
        contract.put("paymentInfo", new JSONObject());
        contract.put("additionalInfo", new JSONObject());
        HttpResponse<String> contractResponse = ApiRequest.post("/contract", contract.toString());
        contract = new JSONObject(contractResponse.body());

        if (contractResponse.statusCode() == 201) {
            signContract(contract);
        } else {
            String msg = "Contract not posted: Error " + contractResponse.statusCode();
            JOptionPane.showMessageDialog(new JFrame(), msg, "Bad request", JOptionPane.ERROR_MESSAGE);
        }
    }


    /**
     * Receive userId from loginController
     *
     * @param data userId required to update other bidding view
     */
    @Override
    public void update(String data) {
        this.userId = data;
    }

}

