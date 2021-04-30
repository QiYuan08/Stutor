package listeners;

import application.ApplicationManager;
import application.bid_pages.FindBidPage;
import controller.ApplicationController;
import controller.ObserverOutputInterface;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FindBidListener implements ObserverOutputInterface, ActionListener {

    private FindBidPage inputPage;
    private ApplicationController applicationController;

    public FindBidListener(FindBidPage inputPage, ApplicationController applicationController) {
        this.inputPage = inputPage;
        this.applicationController = applicationController;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton thisBtn = (JButton) e.getSource();
        String bidId = thisBtn.getName();
        applicationController.notifySubscribers(bidId);
        System.out.println("called");
        ApplicationManager.loadPage(ApplicationManager.FIND_BID_DETAIL);
    }

    // this method is called whenever there is an update in bids to display to tutor
    // to add listener for all the list of bids;
    @Override
    public void update(String data) {
        inputPage.addActionListener(this);
    }
}
