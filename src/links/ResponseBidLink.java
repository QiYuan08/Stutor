package links;

import api.ApiRequest;
import controller.ListenerLinkInterface;
import controller.ObserverInputInterface;
import controller.ObserverOutputInterface;
import services.ViewManagerService;
import views.tutor_responds.ClosedBidResponse;
import views.tutor_responds.FindBidDetails;
import views.main_pages.MessagesPage;
import views.tutor_responds.OpenBidResponse;
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

    private ListenerLinkInterface inputPage;
    private OpenBidResponse openBidResponse;
    private ClosedBidResponse closedBidResponse;
    private MessagesPage messagesPage;

    public ResponseBidLink(ListenerLinkInterface inputPage, OpenBidResponse openBidResponse, ClosedBidResponse closedBidResponse, MessagesPage messagesPage){
        this.inputPage = inputPage;
        this.openBidResponse = openBidResponse;
        this.closedBidResponse = closedBidResponse;
        inputPage.addLinkListener(this);
        openBidResponse.addActionListener(this);
        closedBidResponse.addActionListener(this);
        this.messagesPage = messagesPage;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton thisBtn = (JButton) e.getSource();

        // get the bid from bidId
        JSONObject data = new JSONObject(thisBtn.getName().trim());
        HttpResponse<String> response = ApiRequest.get("/bid/" + data.get("bidId"));
        JSONObject bid = new JSONObject(response.body());

        // if submitting open bid, create a message to update bid
        if (thisBtn.getText().equals("Submit Open Bid")){

            responseBidPage(openBidResponse);

        } else if (thisBtn.getText().equals("Submit Close Bid")) { // if submitting open bid

            responseBidPage(closedBidResponse);

        } else {

            // if message button is clicked for close bid
            if (thisBtn.getText().equals("Message")){
                messagesPage.update(thisBtn.getName());
                ViewManagerService.loadPage(ViewManagerService.MESSAGES_PAGE);

            } else { // if bid button is clicked

                // if bid button in find bids detail page is clicked check if open or close bid then go to appropriate page
                // go to either openBidResponse or closedBidResponse
                if (bid.get("type").equals("open")){
                    openBidResponse.update(thisBtn.getName());
                    ViewManagerService.loadPage(ViewManagerService.OPEN_BID_RESPONSE);
                } else {
                    closedBidResponse.update(thisBtn.getName());
                    ViewManagerService.loadPage(ViewManagerService.CLOSED_BID_RESPONSE);
                }
            }
        }
    }

    private void responseBidPage(ObserverInputInterface observerInputInterface) {

        JSONObject inputData = observerInputInterface.retrieveInputs();
        HttpResponse<String> response = ApiRequest.post("/message", inputData.toString());

        if (response.statusCode() == 201) { // successfully posted message
            JOptionPane.showMessageDialog(new JFrame(), "Success", "Response Sent Successfully", JOptionPane.INFORMATION_MESSAGE);

        } else { // failed API call
            String msg = "Error: " + new JSONObject(response.body()).get("message");
            JOptionPane.showMessageDialog(new JFrame(), msg, "Bad Request", JOptionPane.ERROR_MESSAGE);
        }
    }
}
