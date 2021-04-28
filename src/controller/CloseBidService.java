package controller;

import api.ApiRequest;
import application.Application;
import application.ApplicationManager;
import org.json.JSONObject;

import javax.swing.*;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Utility class to automatically close bids
 */
public class CloseBidService {

    private long counter;
    private TimerTask openBidTask, closeBidTask;
    private Timer timer = new Timer();

    public void setDuration(int minutes) {
        this.counter = minutes * 60 * 1000;
    }

    public void closeOpenBidService() {
        openBidTask = new TimerTask() {
            @Override
            public void run() {
                String bidId = "b94f7177-14a5-44b1-bee8-03b1fb2f6f62";
                HttpResponse<String> response = ApiRequest.get("/bid/" + bidId);
                JSONObject bid = new JSONObject(response.body());
                Instant now = Instant.now();

                // if type is open
                if (bid.get("type").equals("open")){
                    Instant bidStart = Instant.parse(bid.getString("dateCreated"));
                    Instant expireTime = bidStart.plus(counter, ChronoUnit.SECONDS);

                    System.out.println("Expired Time: " + expireTime);
                    System.out.println("Close Time: " + Instant.now());

                    // if expire time greater than now close the bid
                    if (expireTime.compareTo(Instant.now()) > 0){

                        JSONObject closeDate = new JSONObject();
                        closeDate.put("dateClosedDown", Instant.now());
                        System.out.println(closeDate);
                        response =  ApiRequest.post("/bid/" + bidId +"/close-down", closeDate.toString()); // pass empty json object since this API call don't need it
                        String msg;

                        if (response.statusCode() == 200){
                            msg = "Bid closed successfully at: " + closeDate;
                            JOptionPane.showMessageDialog(new JFrame(), msg, "Bid Closed Success", JOptionPane.INFORMATION_MESSAGE);
                            ApplicationManager.loadPage(ApplicationManager.DASHBOARD_PAGE);
                        } else {
                            msg = "Error: " + new JSONObject(response.body()).get("message");
                            JOptionPane.showMessageDialog(new JFrame(), msg, "Bad request", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        };

        timer.schedule(openBidTask, 10, counter);
    }


}
