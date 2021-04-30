package listeners;

import application.ApplicationManager;
import application.bid_pages.FindBidsDetail;
import controller.ApplicationController;
import controller.ObserverInputInterface;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BidderListener implements ActionListener {

    FindBidsDetail inputPage;
    ApplicationController applicationController;

    public BidderListener(FindBidsDetail inputPage, ApplicationController applicationController) {
        this.inputPage = inputPage;
        this.applicationController = applicationController;
//        inputPage.addActionListener(this);
//        inputPage.addFindBidListener(this); // add listener for find bid button
//        inputPage.addSeeBidListener(this);  // add listener for see bid button
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
