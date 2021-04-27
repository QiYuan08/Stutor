package controller;

import api.ApiRequest;
import application.Application;
import application.FindBidDetail;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.http.HttpResponse;
import java.time.Instant;

public class CloseBidListener implements ActionListener, ObserverOutputInterface {

    private FindBidDetail inputPage;
    private ApplicationController applicationController;
    private String userId; // needed to update other bid view

    public CloseBidListener(FindBidDetail inputPage, ApplicationController applicationController) {
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
     * Function for a tutor to close a bid immideately if he agree to a tutor bid
     */
    public void closeBid(String bidId){

        JSONObject closeDate = new JSONObject();
        closeDate.put("dateClosedDown", Instant.now());
        HttpResponse<String> response =  ApiRequest.post("/bid/" + bidId +"/close-down", closeDate.toString()); // pass empty json object since this API call don't need it
        String msg;

        if (response.statusCode() == 200){
            msg = "Bid closed successfully at: " + closeDate.get("dateClosedDown");
            JOptionPane.showMessageDialog(new JFrame(), msg, "Bid Closed Success", JOptionPane.INFORMATION_MESSAGE);
            applicationController.notifySubscribers(this.userId);
            Application.loadPage(Application.DASHBOARD_PAGE);
        } else {
            msg = "Error: " + new JSONObject(response.body()).get("message");
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
