package controller;

import api.ApiRequest;
import org.json.JSONArray;
import org.json.JSONObject;

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
public class ExpireBidService implements ObserverInputInterface, ObserverOutputInterface {

    private long counter; //counter of timer in miliseconds
    private TimerTask openBidTask, closeBidTask;
    private Timer openTimer = new Timer();
    private Timer closeTimer = new Timer();
    private ActionListener actionListener;
    JSONObject expiredBid;
    Instant currentTime;
    String expiredBidId, userId;

    public void setDuration(int minutes) {
        this.counter = minutes * 60000L;
    }

    public void expireOpenBidService() {
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
                    if (bid.get("type").equals("open") && bid.get("dateClosedDown").equals(null)){
                        Instant bidStart = Instant.parse(bid.getString("dateCreated"));
                        Instant expireTime = bidStart.plus(counter, ChronoUnit.MILLIS);
                        Timestamp ts = Timestamp.from(ZonedDateTime.now().toInstant());
                        Instant now = ts.toInstant();

                        // if expire time greater than now close the bid
                        if (now.compareTo(expireTime) > 0){

                            // TODO: remove the JOptionPANEL later
                            // TODO: add the userId to this class so it would only expire the bids owned by the user?
                            expiredBid = bid;
                            expiredBidId = bidId;
                            currentTime = now;

                            ActionEvent actionEvent = new ActionEvent(ExpireBidService.this, ActionEvent.ACTION_PERFORMED, "Expire Bid");
                            actionListener.actionPerformed(actionEvent);

                        }
                    }
                }
            }
        };

        openTimer.schedule(openBidTask, 10, 30000);
    }

    public void expireCloseBidService() {
        closeBidTask = new TimerTask() {

            @Override
            public void run() {

                HttpResponse<String> bidResponse = ApiRequest.get("/bid?fields=messages");
                JSONArray bids = new JSONArray(bidResponse.body());

                // check every bid to see if they already expired
                for (int i=0; i<bids.length(); i++){
                    JSONObject bid = bids.getJSONObject(i);
                    String bidId = bid.getString("id");

                    // if bid type is close
                    if (bid.get("type").equals("close") && bid.get("dateClosedDown").equals(null)){
                        Instant bidStart = Instant.parse(bid.getString("dateCreated")); // bidStartTime

                        Timestamp ts = Timestamp.from(ZonedDateTime.now().toInstant());
                        Instant now = ts.toInstant();  // current Time

                        // bidExpire Time
                        LocalDateTime time = LocalDateTime.ofInstant(bidStart, ZoneOffset.ofHours(0));
                        time = time.plus(1, ChronoUnit.MINUTES);
                        Instant expiryDate = time.atZone(ZoneOffset.ofHours(0)).toInstant();

                        System.out.println(now);
                        System.out.println(expiryDate);

                        // if expire time greater than now close the bid
                        if (now.compareTo(expiryDate) > 0){

                            System.out.println("closed bid closd");
                            // TODO: remove the JOptionPANEL later
                            // TODO: add the userId to this class so it would only expire the bids owned by the user?
                            expiredBid = bid;
                            expiredBidId = bidId;
                            currentTime = now;

                            ActionEvent actionEvent = new ActionEvent(ExpireBidService.this, ActionEvent.ACTION_PERFORMED, "Expire Bid");
                            actionListener.actionPerformed(actionEvent);

                        }
                    }
                }
            }
        };

        closeTimer.schedule(closeBidTask, 10, 30000);
    }

    @Override
    public JSONObject retrieveInputs() {
        JSONArray messages = expiredBid.optJSONArray("messages");
        JSONObject closeDate = new JSONObject();
        closeDate.put("dateClosedDown", currentTime);

        JSONObject bidInfo = new JSONObject();
        bidInfo.put("bidId", expiredBidId);
        bidInfo.put("messageId", "");

        if (messages.length() == 0) {
            bidInfo.put("hasExpired", true);
            bidInfo.put("tutorId", "");
        } else {
            bidInfo.put("hasExpired", false);
            String tutorId = messages.getJSONObject(messages.length()-1).getJSONObject("poster").getString("id");
            bidInfo.put("tutorId", tutorId);
        }
        return bidInfo;
    }

    @Override
    public void addActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    @Override
    public void update(String data) {
        this.userId = data;
    }

    @Override
    public void addBackBtnListener(ActionListener listener) {

    }


}
