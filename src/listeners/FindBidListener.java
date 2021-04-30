package listeners;

import application.ApplicationManager;
import application.bid_pages.FindBidPage;
import application.bid_pages.FindBidsDetail;
import controller.ApplicationController;
import controller.ObserverOutputInterface;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FindBidListener implements ObserverOutputInterface, ActionListener {

    private FindBidsDetail inputPage;

    public FindBidListener(FindBidsDetail inputPage) {
        this.inputPage = inputPage;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton thisBtn = (JButton) e.getSource();
        String bidId = thisBtn.getName();
        ApplicationManager.loadPage(ApplicationManager.FIND_TUTOR_BID_DETAIL);
    }

    // this method is called whenever there is an update in bids to display to tutor
    // to add listener for all the list of bids;
    @Override
    public void update(String data) {
//        inputPage.addViewBidListener(this);
        inputPage.addLinkListener(this);
    }
}
