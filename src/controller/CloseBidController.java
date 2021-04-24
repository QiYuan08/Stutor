package controller;

import api.ApiRequest;
import interfaces.InputInterface;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.http.HttpResponse;
import java.time.Instant;

public class CloseBidController extends Controller{

    CloseBidController(InputInterface inputPage) {
        super(inputPage);

    }

    @Override
    public void initListener(){
        this.inputPage.setListener(new CloseBidListener());
    }

    class CloseBidListener implements ActionListener{

        HttpResponse<String> response;

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton thisBtn = (JButton) e.getSource();
            closeBid(thisBtn.getName());
        }

        /***
         * Function for a tutor to close a bid immideately if he agree to a tutor bid
         */
        public void closeBid(String bidId){

            JSONObject closeDate = new JSONObject();
            closeDate.put("dateCloseDown", Instant.now());
            response =  ApiRequest.post("/bid/" + bidId +"/close-down", closeDate.toString()); // pass empty json object since this API call don't need it
            String msg;

            if (response.statusCode() == 200){
                msg = "Bid closed at: " + new JSONObject(response.body()).get("dateClosedDown");
                JOptionPane.showMessageDialog(new JFrame(), msg, "Bid Closed Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                msg = "Error: " + new JSONObject(response.body()).get("message");
                JOptionPane.showMessageDialog(new JFrame(), msg, "Bad request", JOptionPane.ERROR_MESSAGE);
            }

        }
    }
}
