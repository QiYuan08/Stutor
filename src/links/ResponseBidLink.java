package links;

import api.ApiRequest;
import application.ApplicationManager;
import application.bid_pages.FindBidsDetail;
import application.bid_pages.ResponseCloseBid;
import application.bid_pages.ResponseOpenBid;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.http.HttpResponse;

/**
 * A listener that listen to two classes
 * To avoid having another controller with only one subsriber in application.java
 * Main function is to pass data
 */
public class ResponseBidLink implements ActionListener {

    private FindBidsDetail inputPage;
    private ResponseOpenBid responseOpenBid;
    private ResponseCloseBid responseCloseBid;

    public ResponseBidLink(FindBidsDetail inputPage, ResponseOpenBid responseOpenBid, ResponseCloseBid responseCloseBid){
        this.inputPage = inputPage;
        this.responseOpenBid = responseOpenBid;
        this.responseCloseBid = responseCloseBid;
        inputPage.addReplyBidListener(this);
        responseOpenBid.addActionListener(this);
        responseCloseBid.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton thisBtn = (JButton) e.getSource();

        // get the bid from bidId
        JSONObject data = new JSONObject(thisBtn.getName());
        HttpResponse<String> response = ApiRequest.get("/bid/" + data.get("bidId"));
        JSONObject bid = new JSONObject(response.body());

        // if submitting open bid
        // create a message to update bid
        if (thisBtn.getText().equals("Submit Open Bid")){

            JSONObject inputData = responseOpenBid.retrieveInputs();
            response = ApiRequest.post("/message", inputData.toString());

            if (response.statusCode() == 201) { // success
                JOptionPane.showMessageDialog(new JFrame(), "Success", "Bid Send Success", JOptionPane.INFORMATION_MESSAGE);

            } else { // failed API call
                String msg = "Error: " + new JSONObject(response.body()).get("message");
                JOptionPane.showMessageDialog(new JFrame(), msg, "Bad Request", JOptionPane.ERROR_MESSAGE);
            }

        } else if (thisBtn.getText().equals("Submit Close Bid")) { // if submitting open bid

            JSONObject inputData = responseCloseBid.retrieveInputs();
            response = ApiRequest.post("/message", inputData.toString());

            if (response.statusCode() == 201) { // success
                JOptionPane.showMessageDialog(new JFrame(), "Success", "Bid Send Success", JOptionPane.INFORMATION_MESSAGE);

            } else { // failed API call
                String msg = "Error: " + new JSONObject(response.body()).get("message");
                JOptionPane.showMessageDialog(new JFrame(), msg, "Bad Request", JOptionPane.ERROR_MESSAGE);
            }


        }else {
            // if bid button in find bids detail page is clicked check if open or close bid then go to appriopriate page
            // go to either responseOpenBid or responseCloseBid
            if (bid.get("type").equals("open")){
                responseOpenBid.update(thisBtn.getName());
                ApplicationManager.loadPage(ApplicationManager.RESPONSE_OPEN_BID);
            } else {
                responseCloseBid.update(thisBtn.getName());
                ApplicationManager.loadPage(ApplicationManager.RESPONSE_CLOSE_BID);
            }

        }


    }
}
