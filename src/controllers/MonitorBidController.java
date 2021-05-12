package controllers;

import abstractions.ObserverInputInterface;
import abstractions.Publisher;
import org.json.JSONArray;
import org.json.JSONObject;
import services.ApiRequest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.http.HttpResponse;

public class MonitorBidController extends Publisher implements ActionListener {

    ObserverInputInterface inputPage;

    public MonitorBidController(ObserverInputInterface inputPage) {
        super();
        this.inputPage = inputPage;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton btn = (JButton) e.getSource();
        inputPage = (ObserverInputInterface) btn.getParent();
        JSONObject context = inputPage.retrieveInputs();
        String userId = context.getString("userId");
        String bidId = context.getString("bidId");

        HttpResponse<String> userResponse = ApiRequest.get("/user/" + userId);

        if (userResponse.statusCode() == 200) {
            JSONObject additionalInfo = new JSONObject(userResponse.body()).getJSONObject("additionalInfo");

            if (!additionalInfo.has("monitoredBids")) {
                additionalInfo.put("monitoredBids", new JSONArray());
            }
            JSONObject newBid = new JSONObject().put("bidId", bidId);
            additionalInfo.getJSONArray("monitoredBids").put(newBid);

            JSONObject patchRequest = new JSONObject().put("additionalInfo", additionalInfo);
            HttpResponse<String> pathResponse = ApiRequest.patch("/user/" + userId, patchRequest.toString());

            if (pathResponse.statusCode() == 200) {
                JOptionPane.showMessageDialog(new JFrame(), "Bid successfully added into your monitoring list!", "Bid Monitored", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}
