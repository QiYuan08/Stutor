package listeners;

import api.ApiRequest;
import application.ApplicationManager;
import application.bid_pages.FindBidsDetail;
import controller.ApplicationController;
import controller.ObserverOutputInterface;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.http.HttpResponse;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZonedDateTime;

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
        HttpResponse<String> response =  ApiRequest.post("/bid/" + bidId +"/close-down", closeDate.toString()); // pass empty json object since this API call don't need it
        String msg;

        if (response.statusCode() == 200){
            msg = "Bid closed successfully at: " + closeDate.get("dateClosedDown");
            JOptionPane.showMessageDialog(new JFrame(), msg, "Bid Closed Success", JOptionPane.INFORMATION_MESSAGE);
            applicationController.notifySubscribers(this.userId);
            ApplicationManager.loadPage(ApplicationManager.DASHBOARD_PAGE);
        } else {
            msg = "Error: " + new JSONObject(response.body()).get("message");
            JOptionPane.showMessageDialog(new JFrame(), msg, "Bad request", JOptionPane.ERROR_MESSAGE);
        }

        // TODO: add contract
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
