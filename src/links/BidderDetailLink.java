package links;

import application.ApplicationManager;
import application.bid_pages.FindBidsDetail;
import controller.ObserverInputInterface;
import controller.ObserverOutputInterface;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BidderDetailLink implements ActionListener, ObserverOutputInterface {

    private FindBidsDetail inputPage;
    private ObserverOutputInterface outputPage;

    public BidderDetailLink(FindBidsDetail inputPage, ObserverOutputInterface outputPage) {
        this.inputPage = inputPage;
        this.outputPage = outputPage;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton thisBtn = (JButton) e.getSource();
        String bidId = thisBtn.getName();
        outputPage.update(bidId);
        ApplicationManager.loadPage(ApplicationManager.TUTOR_BID_DETAIL);
    }

    @Override
    public void update(String data) {
        System.out.println("Bidder Link update called");
        inputPage.addViewBidListener(this);
    }
}
