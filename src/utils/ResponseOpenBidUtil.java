package utils;

import api.ApiRequest;
import org.json.JSONObject;

import javax.swing.*;
import java.net.http.HttpResponse;

public class ResponseOpenBidUtil implements ResponseUtil{

    private HttpResponse<String> response;

    @Override
    public void update(String bidId, JSONObject msgBody) {
        response = ApiRequest.patch("/bid/"+ bidId, msgBody.toString());

        if (response.statusCode() == 200){ // api call success
            JOptionPane.showMessageDialog(new JFrame(), "Success", "Bid Send Success", JOptionPane.INFORMATION_MESSAGE);
        } else { // api call failed
            String msg = "Error: " + new JSONObject(response.body()).get("message");
            JOptionPane.showMessageDialog(new JFrame(), msg, "Bad Request", JOptionPane.ERROR_MESSAGE);
        }
    }
}
