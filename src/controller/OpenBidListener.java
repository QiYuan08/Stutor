package controller;

import api.ApiRequest;
import application.Application;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.http.HttpResponse;
import java.time.Instant;

public class OpenBidListener implements ActionListener {

    private ObserverInputInterface inputPage;
    private ApplicationController applicationController;

    public OpenBidListener(ObserverInputInterface inputPage, ApplicationController applicationController) {
        this.inputPage = inputPage;
        this.applicationController = applicationController;
        inputPage.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JSONObject bidDetails = inputPage.retrieveInputs();
        createBid(bidDetails);
    }

    protected void createBid(JSONObject bidDetails){

        HttpResponse<String> response;
        System.out.println(bidDetails);

        response = ApiRequest.post("/bid", bidDetails.toString());

        if (response.statusCode() == 201){
            JOptionPane.showMessageDialog(new JFrame(), "Success", "Bid Send Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            String msg = "Error: " + new JSONObject(response.body()).get("message");
            JOptionPane.showMessageDialog(new JFrame(), msg, "Bad Request", JOptionPane.ERROR_MESSAGE);
        }
    }
}
