package links;

import api.ApiRequest;
import services.ViewManagerService;
import views.tutor_responds.ClosedBidResponse;
import views.tutor_responds.FindBidDetails;
import views.main_pages.MessagesPage;
import views.tutor_responds.ResponseOpenBid;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.http.HttpResponse;

// TODO: might want to refactor into controller cuz too many subsribers
/**
 * A listener that listen to two classes
 * To avoid having another controller with only one subsriber in application.java
 * Main function is to pass data
 */
public class ResponseBidLink implements ActionListener {

    private FindBidDetails findBidDetails;
    private ResponseOpenBid responseOpenBid;
    private ClosedBidResponse closedBidResponse;
    private MessagesPage messagesPage;

    public ResponseBidLink(FindBidDetails findBidDetails, ResponseOpenBid responseOpenBid, ClosedBidResponse closedBidResponse, MessagesPage messagesPage){
        this.findBidDetails = findBidDetails;
        this.responseOpenBid = responseOpenBid;
        this.closedBidResponse = closedBidResponse;
        findBidDetails.addLinkListener(this);
        this.messagesPage = messagesPage;
        responseOpenBid.addActionListener(this);
        closedBidResponse.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton thisBtn = (JButton) e.getSource();

        // get the bid from bidId
        JSONObject data = new JSONObject(thisBtn.getName().trim());
        HttpResponse<String> response = ApiRequest.get("/bid/" + data.get("bidId"));
        JSONObject bid = new JSONObject(response.body());

        // if submitting open bid
        // create a message to update bid
        if (thisBtn.getText().equals("Submit Open Bid")){

            JSONObject inputData = responseOpenBid.retrieveInputs();
            response = ApiRequest.post("/message", inputData.toString());

            if (response.statusCode() == 201) { // success
                JOptionPane.showMessageDialog(new JFrame(), "Success", "Bid Send Success", JOptionPane.INFORMATION_MESSAGE);

            } else { // failed API call
                String msg = "Error: " + new JSONObject(response.body()).get("message");
                JOptionPane.showMessageDialog(new JFrame(), msg, "Bad Request", JOptionPane.ERROR_MESSAGE);
            }

        } else if (thisBtn.getText().equals("Submit Close Bid")) { // if submitting open bid

            JSONObject inputData = closedBidResponse.retrieveInputs();
            response = ApiRequest.post("/message", inputData.toString());

            if (response.statusCode() == 201) { // success
                JOptionPane.showMessageDialog(new JFrame(), "Success", "Bid Send Success", JOptionPane.INFORMATION_MESSAGE);

            } else { // failed API call
                String msg = "Error: " + new JSONObject(response.body()).get("message");
                JOptionPane.showMessageDialog(new JFrame(), msg, "Bad Request", JOptionPane.ERROR_MESSAGE);
            }


        }else {

            // if message button is clicked for close bid
            if (thisBtn.getText().equals("Message")){
                this.messagesPage.update(thisBtn.getName());
                ViewManagerService.loadPage(ViewManagerService.MESSAGES_PAGE);

            } else { // if bid button is clicked

                // if bid button in find bids detail page is clicked check if open or close bid then go to appriopriate page
                // go to either responseOpenBid or closedBidResponse
                if (bid.get("type").equals("open")){
                    responseOpenBid.update(thisBtn.getName());
                    ViewManagerService.loadPage(ViewManagerService.RESPONSE_OPEN_BID);
                } else {
                    closedBidResponse.update(thisBtn.getName());
                    ViewManagerService.loadPage(ViewManagerService.RESPONSE_CLOSE_BID);
                }
            }


        }


    }
}
