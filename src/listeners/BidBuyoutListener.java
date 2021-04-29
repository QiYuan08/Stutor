package listeners;

import api.ApiRequest;
import application.ApplicationManager;
import application.bid_pages.FindBidsDetail;
import controller.ApplicationController;
import controller.ObserverOutputInterface;
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

public class BidBuyoutListener implements ActionListener, ObserverOutputInterface {

    private FindBidsDetail inputPage;
    private ApplicationController applicationController;
    private String userId; // needed to update other bid view

    public BidBuyoutListener(FindBidsDetail inputPage, ApplicationController applicationController) {
        this.inputPage = inputPage;
        this.applicationController = applicationController;
        inputPage.addCloseBidListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton thisBtn = (JButton) e.getSource();
        String bidId = thisBtn.getName();
        closeBid(bidId);
    }

    /***
     * Function for a tutor to close a bid immideately if he agree to the student's bid
     */
    public void closeBid(String bidId){

        JSONObject closeDate = new JSONObject();

        Timestamp ts = Timestamp.from(ZonedDateTime.now().toInstant());
        Instant now = ts.toInstant();

        closeDate.put("dateClosedDown", now);
        HttpResponse<String> bidBuyoutResponse =  ApiRequest.post("/bid/" + bidId +"/close-down", closeDate.toString()); // pass empty json object since this API call don't need it
        String msg;

        HttpResponse<String> bidResponse = ApiRequest.get("/bid/" + bidId);
        JSONObject bid = new JSONObject(bidResponse.body());
        if (bidBuyoutResponse.statusCode() == 200) {
            JSONObject contract = new JSONObject();
            contract.put("firstPartyId", this.userId);
            contract.put("secondPartyId", bid.getJSONObject("initiator").getString("id"));
            contract.put("subjectId", bid.getJSONObject("subject").getString("id"));
            contract.put("dateCreated", now);
            LocalDateTime time = LocalDateTime.ofInstant(ts.toInstant(), ZoneOffset.ofHours(0));
            time = time.plus(1, ChronoUnit.YEARS);
            Instant output = time.atZone(ZoneOffset.ofHours(0)).toInstant();
            Timestamp savedTimestamp = Timestamp.from(output);
            contract.put("expiryDate", savedTimestamp);
            contract.put("paymentInfo", new JSONObject());
            contract.put("lessonInfo", bid.getJSONObject("additionalInfo"));
            contract.put("additionalInfo", new JSONObject());
            HttpResponse<String> contractResponse =  ApiRequest.post("/contract", contract.toString());

            if (contractResponse.statusCode() == 200) {
                contract = new JSONObject(contractResponse.body());
                JSONObject dateSigned = new JSONObject();
                ts = Timestamp.from(ZonedDateTime.now().toInstant());
                now = ts.toInstant();
                dateSigned.put("dateSigned", now);
                HttpResponse<String> contractSignResponse =  ApiRequest.post("/contract/" + contract.getString("id") + "/sign", dateSigned.toString());
                if (contractSignResponse.statusCode() == 200) {
                    msg = "Bid closed successfully at: " + closeDate.get("dateClosedDown");
                    JOptionPane.showMessageDialog(new JFrame(), msg, "Bid Closed Success", JOptionPane.INFORMATION_MESSAGE);
                    applicationController.notifySubscribers(this.userId);
                    ApplicationManager.loadPage(ApplicationManager.DASHBOARD_PAGE);
                }
            }
        } else {
            msg = "Error " + bidBuyoutResponse.statusCode();
            JOptionPane.showMessageDialog(new JFrame(), msg, "Bad request", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Receive userId from loginController
     * @param data userId required to update other bidding view
     */
    @Override
    public void update(String data) {
        this.userId = data;
    }
}
