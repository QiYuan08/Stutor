package application.bid;

import api.ApiRequest;
import org.json.JSONObject;

import javax.swing.*;
import java.net.http.HttpResponse;
import java.time.Instant;

public class CreateBid extends JPanel {

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
