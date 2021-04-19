package utils;

import api.ApiRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.net.http.HttpResponse;

public class ResponseCloseBidUtil implements ResponseUtil{

    private HttpResponse<String> response;

    @Override
    public void update(String bidId, JSONObject msgBody) {
        response = ApiRequest.get("/bid/" + bidId);
        JSONObject bid = new JSONObject(response.body());
        JSONObject additionalInfo = (JSONObject) bid.get("additionalInfo");
        System.out.println(additionalInfo);
        JSONArray messages;
        boolean isEmpty = false;

        // add messages to existing bid
        // check if there is available messages already
        try{
            messages = new JSONArray(bid.get("messages"));
        } catch(JSONException e) {
            // if there is no existing message
            // create a new attribute and add to  it
            isEmpty = true;

            JSONArray messageArr = new JSONArray().put(msgBody);
            additionalInfo.put("messages", messageArr);

            response = ApiRequest.patch("/bid/" + bidId, bid.toString());
        }

        // if there is another message before this, add this new message inside
        if (isEmpty) {
            messages = (JSONArray) additionalInfo.get("messages");
            System.out.println(messages);
            messages.put(msgBody);

            // patch the new additional info back into the server
            response = ApiRequest.patch("/bid/"+ bidId, messages.toString());
        }
    }

    /***
     * Function to create message for tutor to send their bid to the student
     * @param messageBody content of the message
     * @param recipientId id of the recipient
     */
    public void createMessage(JSONObject messageBody, String recipientId) {
        response = ApiRequest.post("/message", messageBody.toString());

        if (response.statusCode() != 201) {
            String msg = "Error: " + new JSONObject(response.body()).get("message");
            JOptionPane.showMessageDialog(new JFrame(), msg, "Bad Request", JOptionPane.ERROR_MESSAGE);
        }
    }
}
