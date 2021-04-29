package controller;

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
import java.util.Timer;
import java.util.TimerTask;

/**
 * Utility class to automatically close bids
 */
public class ExpireBidService implements ObserverInputInterface {

    private long counter; //counter of timer in miliseconds
    private TimerTask openBidTask, closeBidTask;
    private Timer timer = new Timer();
    private ActionListener actionListener;

    public void setDuration(int minutes) {
        this.counter = minutes * 60000;
    }

    public void expireBidService() {
        openBidTask = new TimerTask() {

            @Override
            public void run() {

                HttpResponse<String> bidResponse = ApiRequest.get("/bid?fields=messages");
                JSONArray bids = new JSONArray(bidResponse.body());

                // check every bid to see if they already expired
                for (int i=0; i<bids.length(); i++){
                    JSONObject bid = bids.getJSONObject(i);
                    String bidId = bid.getString("id");
                    // if bid type is open
                    if (bid.get("type").equals("open") && bid.isNull("dateClosedDown")){
                        Instant bidStart = Instant.parse(bid.getString("dateCreated"));
                        Instant expireTime = bidStart.plus(counter, ChronoUnit.MILLIS);
                        Timestamp ts = Timestamp.from(ZonedDateTime.now().toInstant());
                        Instant now = ts.toInstant();

                        // if expire time greater than now close the bid
                        if (now.compareTo(expireTime) > 0){

                            // TODO: remove the JOptionPANEL later
                            JSONObject closeDate = new JSONObject();
                            closeDate.put("dateClosedDown", now);
                            JSONArray messages = bid.optJSONArray("messages");

                            if (messages.length() == 0) {
                                HttpResponse<String> bidCloseDownResponse =  ApiRequest.post("/bid/" + bidId + "/close-down", closeDate.toString()); // pass empty json object since this API call don't need it
                                if (bidCloseDownResponse.statusCode() == 200) {
                                    JOptionPane.showMessageDialog(new JFrame(), "Bid expired at " + now, "Bid Expired", JOptionPane.INFORMATION_MESSAGE);
                                } else { // failed to close bid
                                    JOptionPane.showMessageDialog(new JFrame(), "Bid expired but failed to close: Error " + bidCloseDownResponse.statusCode(), "Bad request", JOptionPane.ERROR_MESSAGE);
                                }
                            } else {
                                JSONObject bidInfo = new JSONObject();
                                bidInfo.put("bidId", bidId);
                                String tutorId = messages.getJSONObject(messages.length()-1).getJSONObject("poster").getString("id");
                                bidInfo.put("tutorId", tutorId);

                                JButton expireBid = new JButton();
                                expireBid.setName(bidId);
                                expireBid.addActionListener(actionListener);
                                ActionEvent actionEvent = new ActionEvent(expireBid, ActionEvent.ACTION_PERFORMED, "Expire Bid");
                                actionListener.actionPerformed(actionEvent);
                            }
                        }
                    }
                }
            }
        };

        timer.schedule(openBidTask, 10, 30000);
    }


    @Override
    public JSONObject retrieveInputs() {
        return null;
    }

    @Override
    public void addActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }
}
