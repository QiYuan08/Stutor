package utils;

import api.ApiRequest;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.net.http.HttpResponse;
import java.time.Instant;

/***
 * Utility class to perform the backend logic for the ALlBid UI class
 */
public class AllBidUtil {

    /***
     * Function to get all available bid to show the user
     */
    private HttpResponse<String> response;

    public JSONArray getAllBid(String studentId){
        response = ApiRequest.get("/bid");
        JSONArray bids = new JSONArray(response.body());
        for (int i=0; i < bids.length(); i++){
            JSONObject bid = bids.getJSONObject(i);
            JSONObject initiator = bid.getJSONObject("initiator");

            // if the poster of the bid is the tutor itself don't show the bid
            if (initiator.get("id").equals(studentId)){
                bids.remove(i);
            }
        }

        return bids;
    }

    public void viewBidDetail(){
        // call response class for tutor to close response
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
