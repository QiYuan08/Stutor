package controller;

import api.ApiRequest;
import application.Application;
import application.ApplicationManager;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.net.http.HttpResponse;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Utility class to automatically close bids
 */
public class CloseBidService {

    private long counter; //counter of timer in miliseconds
    private TimerTask openBidTask, closeBidTask;
    private Timer timer = new Timer();

    public void setDuration(int minutes) {
        this.counter = minutes * 60000;
    }

    public void closeOpenBidService() {
        openBidTask = new TimerTask() {
            @Override
            public void run() {

                HttpResponse<String> response = ApiRequest.get("/bid");
                String a = response.body();
                JSONArray bids = new JSONArray(response.body());

                // check every bid to see if they already expired
                for (int i=0; i<bids.length(); i++){
                    JSONObject bid = bids.getJSONObject(i);
                    // if bid type is open
                    if (bid.get("type").equals("open") && bid.get("dateClosedDown").equals(null)){
                        Instant bidStart = Instant.parse(bid.getString("dateCreated"));
                        Instant expireTime = bidStart.plus(counter, ChronoUnit.MILLIS);
                        Timestamp ts = Timestamp.from(ZonedDateTime.now().toInstant());
                        Instant now = ts.toInstant();

                        System.out.println(bidStart);
                        System.out.println(expireTime);
                        System.out.println(now);

                        // if expire time greater than now close the bid
                        if (now.compareTo(expireTime) > 0){

                            //TODO: remove the JOptionPANEL later
                            JSONObject closeDate = new JSONObject();
                            closeDate.put("dateClosedDown", now);
                            response =  ApiRequest.post("/bid/" + bid.get("id") +"/close-down", closeDate.toString()); // pass empty json object since this API call don't need it
                            String msg;

                            if (response.statusCode() == 200){
                                msg = "Bid expire at: " + closeDate;
                                JOptionPane.showMessageDialog(new JFrame(), msg, "Bid Closed Success", JOptionPane.INFORMATION_MESSAGE);
                                ApplicationManager.loadPage(ApplicationManager.DASHBOARD_PAGE);
                            } else {
                                msg = "Error: " + new JSONObject(response.body()).get("message");
                                JOptionPane.showMessageDialog(new JFrame(), msg, "Bad request", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                }

            }
        };

        timer.schedule(openBidTask, 10, 30000);
    }


}
