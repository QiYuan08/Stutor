package listeners;

import services.ApiRequest;
import abstractions.ObserverInputInterface;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.http.HttpResponse;

/**
 * Sends a message (aka response) and leads back to the FindBidDetails page.
 */
public class MessageListener implements ActionListener {

    private ObserverInputInterface inputPage;

    public MessageListener(ObserverInputInterface inputPage) {
        this.inputPage = inputPage;
        inputPage.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        JSONObject data = inputPage.retrieveInputs();
        HttpResponse<String> response = ApiRequest.post("/message", data.toString());

        if (response.statusCode() == 201){
            JOptionPane.showMessageDialog(new JFrame(), "Message Send", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            String msg = "Error: " + new JSONObject(response.body()).get("message");
            JOptionPane.showMessageDialog(new JFrame(), msg, "Bad request", JOptionPane.ERROR_MESSAGE);
        }
    }
}
