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

    public JSONArray getAllBid(){
        response = ApiRequest.get("/bid");

        return new JSONArray(response.body());
    }

    public void viewBidDetail(){
        // call response class for tutor to close response
    }

    /***
     * Function for a tutor to close a bid immideately if he agree to a tutor bid
     */
    public void closeBid(String bidId){
        response =  ApiRequest.post("/bid/" + bidId +"/close-down", ""); // pass empty json object since this API call don't need it
        String msg;

        if (response.statusCode() == 200){
            msg = "Bid closed at: " + new JSONObject(response.body()).get("dateClosedDown");
            JOptionPane.showMessageDialog(new JFrame(), msg, "Bid Closed Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            msg = "Error: " + new JSONObject(response.body()).get("message");
            JOptionPane.showMessageDialog(new JFrame(), msg, "Bad request", JOptionPane.ERROR_MESSAGE);
        }

    }

    protected void createBid(String bidType, String initiatorId, String subjectId, JSONObject additionalInfo){

        HttpResponse<String> response;
        Instant dateNow = Instant.now(); // date time to store inside dateCreated

        // create json object to creating a new bid
        JSONObject responseBody = new JSONObject();
        responseBody.put("type", bidType);
        responseBody.put("initiatorId", initiatorId);
        responseBody.put("dateCreated", dateNow);
        responseBody.put("subjectId",subjectId);
        responseBody.put("additionalInfo", additionalInfo);

        response = ApiRequest.post("/bid", responseBody.toString());

        if (response.statusCode() == 200){
            System.out.println(response.body());
        } else {
            System.out.println(response.statusCode());
        }
    }
}
