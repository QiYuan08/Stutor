package listeners;

import application.ApplicationManager;
import controller.ApplicationController;
import controller.ListenerLinkInterface;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/***
 * Listener for find bid button in dashboard
 */
public class BidUpdateListener implements ActionListener {

    ListenerLinkInterface inputPage;
    ApplicationController applicationController;

    public BidUpdateListener(ListenerLinkInterface inputPage, ApplicationController applicationController) {
        this.inputPage = inputPage;
        this.applicationController = applicationController;
        inputPage.addLinkListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        applicationController.notifySubscribers(null);

        JButton thisBtn = (JButton) e.getSource();
        if (thisBtn.getText() == "See Your Bids"){
            ApplicationManager.loadPage(ApplicationManager.SEE_BIDS_PAGE);
        } else if (thisBtn.getText() == "Find Bids"){
            ApplicationManager.loadPage(ApplicationManager.FIND_BID);
        }


    }
}
