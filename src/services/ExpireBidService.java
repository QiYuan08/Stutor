package services;

import abstractions.ObserverInputInterface;
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
public class ExpireBidService implements ObserverInputInterface {

    private long openCounter, closedCounter; //counter of timer in minutes and days
    private TimerTask openBidTask, closeBidTask;
    private Timer openTimer;
    private Timer closeTimer;
    private Timer bidClosingTimer;
    private TimerTask bidClosingTask;
    private ActionListener actionListener;
    private JSONObject expiredBid;
    private Instant currentTime;
    private String expiredBidId;

    public ExpireBidService() {
        openTimer = new Timer();
        closeTimer = new Timer();
        bidClosingTimer = new Timer();
    }

    public void setDuration(int minutes, int days) {
        this.openCounter = minutes * 60000L;
        this.closedCounter = days;
    }

//    public void expireOpenBidService() {
//        openBidTask = new TimerTask() {
//
//            @Override
//            public void run() {
//
//                HttpResponse<String> bidResponse = ApiRequest.get("/bid?fields=messages");
//                JSONArray bids = new JSONArray(bidResponse.body());
//
//                // check every bid to see if they already expired
//                for (int i=0; i<bids.length(); i++){
//                    JSONObject bid = bids.getJSONObject(i);
//                    String bidId = bid.getString("id");
//                    // if bid type is open
//                    if (bid.get("type").equals("open") && bid.isNull("dateClosedDown")){
//                        Instant bidStart = Instant.parse(bid.getString("dateCreated"));
//                        Instant expireTime = bidStart.plus(openCounter, ChronoUnit.MILLIS);
//                        Timestamp ts = Timestamp.from(ZonedDateTime.now().toInstant());
//                        Instant now = ts.toInstant();
//
//                        // if expire time greater than now close the bid
//                        if (now.compareTo(expireTime) > 0){
//
//                            expiredBid = bid;
//                            expiredBidId = bidId;
//                            currentTime = now;
//
//                            ActionEvent actionEvent = new ActionEvent(ExpireBidService.this, ActionEvent.ACTION_PERFORMED, "Expire Open Bid");
//                            actionListener.actionPerformed(actionEvent);
//
//                        }
//                    }
//                }
//            }
//        };
//
//        openTimer.schedule(openBidTask, 10, 30000);
//    }
//
//    public void expireCloseBidService() {
//        closeBidTask = new TimerTask() {
//
//            @Override
//            public void run() {
//
//                HttpResponse<String> bidResponse = ApiRequest.get("/bid?fields=messages");
//                JSONArray bids = new JSONArray(bidResponse.body());
//
//                // check every bid to see if they already expired
//                for (int i=0; i<bids.length(); i++){
//                    JSONObject bid = bids.getJSONObject(i);
//                    String bidId = bid.getString("id");
//
//                    // if bid type is close
//                    if (bid.get("type").equals("close") && bid.isNull("dateClosedDown")){
//                        Instant bidStart = Instant.parse(bid.getString("dateCreated")); // bidStartTime
//
//                        Timestamp ts = Timestamp.from(ZonedDateTime.now().toInstant());
//                        Instant now = ts.toInstant();  // current Time
//
//                        // bidExpire Time
//                        LocalDateTime time = LocalDateTime.ofInstant(bidStart, ZoneOffset.ofHours(0));
//                        time = time.plus(closedCounter, ChronoUnit.DAYS);
//                        Instant expiryDate = time.atZone(ZoneOffset.ofHours(0)).toInstant();
//
//                        // if expire time greater than now close the bid
//                        if (now.compareTo(expiryDate) > 0){
//
//                            expiredBid = bid;
//                            expiredBidId = bidId;
//                            currentTime = now;
//
//                            ActionEvent actionEvent = new ActionEvent(ExpireBidService.this, ActionEvent.ACTION_PERFORMED, "Expire Closed Bid");
//                            actionListener.actionPerformed(actionEvent);
//
//                        }
//                    }
//                }
//            }
//        };
//
//        closeTimer.schedule(closeBidTask, 10, 30000);
//    }

    public void expireBidService() {
        bidClosingTask = new TimerTask() {

            @Override
            public void run() {

                HttpResponse<String> bidResponse = ApiRequest.get("/bid?fields=messages");
                JSONArray bids = new JSONArray(bidResponse.body());

                // check every bid to see if they already expired
                for (int i=0; i<bids.length(); i++){
                    JSONObject bid = bids.getJSONObject(i);
                    String bidId = bid.getString("id");

                    if (bid.isNull("dateClosedDown")) {
                        Instant bidStart = Instant.parse(bid.getString("dateCreated")); // bidStartTime
                        Timestamp ts = Timestamp.from(ZonedDateTime.now().toInstant());
                        Instant now = ts.toInstant();  // current Time
                        Instant expiryTime = null;
                        String command = null;

                        if (bid.get("type").equals("open")) {
                            expiryTime = bidStart.plus(openCounter, ChronoUnit.MILLIS);
                            command = "Expire Open Bid";
                        }
                        else if (bid.get("type").equals("close")) {
                            // bidExpire Time
                            LocalDateTime time = LocalDateTime.ofInstant(bidStart, ZoneOffset.ofHours(0));
                            time = time.plus(closedCounter, ChronoUnit.DAYS);
                            expiryTime = time.atZone(ZoneOffset.ofHours(0)).toInstant();
                            command = "Expire Closed Bid";
                        }

                        // if expire time greater than now close the bid
                        if (now.compareTo(expiryTime) > 0) {

                            expiredBid = bid;
                            expiredBidId = bidId;
                            currentTime = now;

                            ActionEvent actionEvent = new ActionEvent(ExpireBidService.this, ActionEvent.ACTION_PERFORMED, command);
                            actionListener.actionPerformed(actionEvent);

                        }
                    }
                }
            }
        };
        bidClosingTimer.schedule(bidClosingTask, 10, 30000);
    }

    @Override
    public JSONObject retrieveInputs() {
        JSONArray messages = expiredBid.optJSONArray("messages");
        JSONObject closeDate = new JSONObject();
        closeDate.put("dateClosedDown", currentTime);

        JSONObject bidInfo = new JSONObject();
        bidInfo.put("bidId", expiredBidId);

        if (messages.length() == 0 || expiredBid.getString("type").equals("closed")) {
            bidInfo.put("hasExpired", true);
            bidInfo.put("tutorId", "");
            bidInfo.put("messageId", "");
        } else {
            bidInfo.put("hasExpired", false);
            JSONObject message = messages.getJSONObject(messages.length()-1);
            bidInfo.put("tutorId", message.getJSONObject("poster").getString("id"));
            bidInfo.put("messageId", message.getString("id"));
        }
        return bidInfo;
    }

    @Override
    public void addActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

}
