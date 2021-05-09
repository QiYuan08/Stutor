package links;

import abstractions.Publisher;
import services.ApiRequest;
import abstractions.ObserverInputInterface;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.http.HttpResponse;

/**
 * Sends a message (aka response) but instead of leading to another page, refreshes itself with the newest message.
 */
public class SendMessageLink extends Publisher implements ActionListener {

    private ObserverInputInterface inputPage;

    public SendMessageLink(ObserverInputInterface inputPage) {
        this.inputPage = inputPage;
        inputPage.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        JSONObject message = inputPage.retrieveInputs();
        HttpResponse<String> response = ApiRequest.post("/message", message.toString());
        JSONObject data = new JSONObject();
        data.put("messageId", message.getString("bidId"));
        data.put("userId", message.getString("posterId"));
        notifySubscribers(data.toString());

        if (response.statusCode() == 201){
            JOptionPane.showMessageDialog(new JFrame(), "Message Send", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            String msg = "Error: " + new JSONObject(response.body()).get("message");
            JOptionPane.showMessageDialog(new JFrame(), msg, "Bad request", JOptionPane.ERROR_MESSAGE);
        }
    }
}
