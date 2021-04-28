package listeners;

import api.ApiRequest;
import application.Application;
import controller.ApplicationController;
import controller.ObserverInputInterface;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.http.HttpResponse;
import java.time.Instant;

public class OpenBidListener implements ActionListener {

    private ObserverInputInterface inputPage;
    private ApplicationController applicationController;

    public OpenBidListener(ObserverInputInterface inputPage, ApplicationController applicationController) {
        this.inputPage = inputPage;
        this.applicationController = applicationController;
        inputPage.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JSONObject bidDetails = inputPage.retrieveInputs();
        createBid(bidDetails);
    }

    protected void createBid(JSONObject bidDetails){
        String initiatorId = bidDetails.getString("initiatorId");
        HttpResponse<String> response = ApiRequest.get("/user/" + initiatorId + "?fields=competencies.subject");
        JSONObject user = new JSONObject(response.body());
        JSONArray competencies = new JSONArray(user.getJSONArray("competencies"));

        // compares bid minimum competency level with the student's competency level to check if it is two levels above
        for (int i = 0; i < competencies.length(); i++) {
            JSONObject competency =  (JSONObject) competencies.get(i);
            if (competency.getJSONObject("subject").getString("id").equals(bidDetails.getString("subjectId"))) {
                if (competency.getInt("level") + 2 > (bidDetails.getJSONObject("additionalInfo").getInt("minCompetency"))) {
                    System.out.println(true);
                    JOptionPane.showMessageDialog(new JFrame(), "The minimum competency level must be at least 2 levels higher than your subject level. Please try again.",
                        "Invalid Minimum Competency Level", JOptionPane.ERROR_MESSAGE);
                } else {
                    response = ApiRequest.post("/bid", bidDetails.toString());
                    if (response.statusCode() == 201){
                        JOptionPane.showMessageDialog(new JFrame(), "Success", "Bid Send Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        String msg = "Error: " + new JSONObject(response.body()).get("message");
                        JOptionPane.showMessageDialog(new JFrame(), msg, "Bad Request", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }

    }
}
